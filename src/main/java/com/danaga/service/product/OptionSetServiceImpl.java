package com.danaga.service.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.danaga.dao.product.CategoryDao;
import com.danaga.dao.product.OptionSetDao;
import com.danaga.dao.product.OptionsDao;
import com.danaga.dao.product.ProductDao;
import com.danaga.dto.ResponseDto;
import com.danaga.dto.product.CategoryDto;
import com.danaga.dto.product.OptionDto;
import com.danaga.dto.product.OptionSetUpdateDto;
import com.danaga.dto.product.OtherOptionSetDto;
import com.danaga.dto.product.ProductDto;
import com.danaga.dto.product.ProductListOutputDto;
import com.danaga.dto.product.ProductSaveDto;
import com.danaga.dto.product.ProductUpdateDto;
import com.danaga.dto.product.QueryStringDataDto;
import com.danaga.dto.product.UploadProductDto;
import com.danaga.entity.Category;
import com.danaga.entity.OptionSet;
import com.danaga.entity.Options;
import com.danaga.entity.Product;
import com.danaga.exception.product.FoundNoObjectException.FoundNoMemberException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoOptionSetException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoOptionsException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoProductException;
import com.danaga.exception.product.ProductExceptionMsg;
import com.danaga.exception.product.ProductSuccessMsg;
import com.danaga.repository.product.OptionNamesValues;
import com.danaga.repository.product.OptionSetQueryData;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OptionSetServiceImpl implements OptionSetService {

	private final OptionSetDao optionSetDao;
	private final OptionsDao optionDao;
	private final ProductDao productDao;
	private final CategoryDao categoryDao;

	// 프로덕트 삭제해서 옵션셋도 같이 삭제되게
	@Override
	@Transactional
	public ResponseDto<?> deleteProduct(Long productId) throws FoundNoProductException {
			productDao.deleteById(productId);
		return ResponseDto.builder().msg(ProductSuccessMsg.REMOVE_PRODUCT).build();
	}

	// 옵션셋 삭제시 옵션들도 삭제
	@Override
	@Transactional
	public ResponseDto<?> deleteOptionSet(Long optionSetId) throws FoundNoOptionSetException {
			optionSetDao.deleteById(optionSetId);
		return ResponseDto.builder().msg(ProductSuccessMsg.REMOVE_OPTIONSET).build();
	}

	// 옵션 삭제하고 옵션이 붙어있던 오리진 옵션셋 반환
	@Override
	@Transactional
	public ResponseDto<?> deleteOption(Long optionId) throws FoundNoOptionsException {
			optionDao.deleteById(optionId);
		return ResponseDto.builder().msg(ProductSuccessMsg.REMOVE_OPTION).build();
	}

	// 오더하면 옵션셋 재고 -1, 환불하거나 취소하면 +1
	// +1, -1은 컨트롤러에서 get+1로 하고 여기서는 그냥 지정한 숫자로 변경
	@Override
	@Transactional
	public ResponseDto<?> updateStock(@Valid OptionSetUpdateDto dto) throws FoundNoOptionSetException {
		OptionSet optionset;
			optionset = optionSetDao.updateStock(dto);
		List<ProductDto> data = new ArrayList<>();
		data.add(new ProductDto(optionset));
		return ResponseDto.<ProductDto>builder().data(data).msg(ProductSuccessMsg.UPDATE_OPTIONSET).build();
	}

	// 주문했을때 주문수 업뎃, +,-는 컨트롤러에서 get+1
	@Override
	@Transactional
	public ResponseDto<?> updateOrderCount(Long optionSetId, Integer orderCount) throws FoundNoOptionSetException {
		OptionSet optionset = optionSetDao.updateOrderCount(optionSetId, orderCount);
		List<ProductDto> data = new ArrayList<>();
		data.add(new ProductDto(optionset));
		return ResponseDto.<ProductDto>builder().data(data).msg(ProductSuccessMsg.UPDATE_OPTIONSET).build();
	}

	// 클릭했을때 조회수 업뎃
	@Override
	@Transactional
	public ResponseDto<?> updateViewCount(Long optionSetId) throws FoundNoOptionSetException {
		OptionSet optionset = optionSetDao.updateViewCount(optionSetId);
		List<ProductDto> data = new ArrayList<>();
		data.add(new ProductDto(optionset));
		return ResponseDto.<ProductDto>builder().data(data).msg(ProductSuccessMsg.UPDATE_OPTIONSET).build();
	}

	// 같은 카테고리 인기상품
		@Override
		@Transactional
		public ResponseDto<ProductListOutputDto> displayHitProducts(Long optionSetId,Integer firstResult) {
			List<Category> findCategory = categoryDao.findByOptionSetId(optionSetId);
			if(findCategory==null) {
				return ResponseDto.<ProductListOutputDto>builder().msg(ProductExceptionMsg.FOUND_NO_CATEGORY).build();
			}
			String orderType = OptionSetQueryData.BY_VIEW_COUNT;

			List<ProductListOutputDto> searchResult = optionSetDao
					.findByFilter(QueryStringDataDto.builder().orderType(orderType)
							.category(CategoryDto.builder().name(findCategory.get(findCategory.size() - 1).getName())
									.id(findCategory.get(findCategory.size() - 1).getId()).build())
							.build(),firstResult);
			return ResponseDto.<ProductListOutputDto>builder().data(searchResult).msg(ProductSuccessMsg.SEARCH_PRODUCTS).build();
		}

	@Override
	@Transactional
	public ResponseDto<ProductListOutputDto> displayHitProductsForMember(Long optionSetId, String username,Integer firstResult) {
		List<Category> findCategory = categoryDao.findByOptionSetId(optionSetId);
		if(findCategory==null) {
			return ResponseDto.<ProductListOutputDto>builder().msg(ProductExceptionMsg.FOUND_NO_CATEGORY).build();
		}
		String orderType = OptionSetQueryData.BY_VIEW_COUNT;
		List<ProductListOutputDto> searchResult= new ArrayList<>();
		try {
		searchResult = optionSetDao
				.findForMemberByFilter(QueryStringDataDto.builder().orderType(orderType)
						.category(CategoryDto.builder().name(findCategory.get(findCategory.size() - 1).getName())
								.id(findCategory.get(findCategory.size() - 1).getId()).build())
						.build(), username,firstResult)
				.stream().filter(t -> t.getOsId()!=optionSetId).collect(Collectors.toList());
		}catch(FoundNoMemberException e) {
			e.printStackTrace();
			return ResponseDto.<ProductListOutputDto>builder().msg(e.getMsg()).build();
		}
		return ResponseDto.<ProductListOutputDto>builder().data(searchResult).msg(ProductSuccessMsg.SEARCH_PRODUCTS).build();
	}

	// 카테고리에 해당하는 리스트 전체 조회
	// 조건에 해당하는 리스트 전체 조회
	@Override
	@Transactional
	public ResponseDto<ProductListOutputDto> searchProducts(@Valid QueryStringDataDto dto,Integer firstResult) {
		List<ProductListOutputDto> data = optionSetDao.findByFilter(dto,firstResult);
		return ResponseDto.<ProductListOutputDto>builder().data(data).msg(ProductSuccessMsg.SEARCH_PRODUCTS).build();
	}

	@Override
	@Transactional
	public ResponseDto<ProductListOutputDto> searchProductsForMember(@Valid QueryStringDataDto dto, String username,Integer firstResult) {
		List<ProductListOutputDto> data = new ArrayList<>();
		try {
		data = optionSetDao.findForMemberByFilter(dto, username,firstResult);
		}catch(FoundNoMemberException e) {
			e.printStackTrace();
			return ResponseDto.<ProductListOutputDto>builder().msg(e.getMsg()).build();
		}
		return ResponseDto.<ProductListOutputDto>builder().data(data).msg(ProductSuccessMsg.SEARCH_PRODUCTS).build();
	}

	// 프로덕트 아이디로 옵션셋 찾기
	@Override
	@Transactional
	public ResponseDto<OtherOptionSetDto> showOtherOptionSets(Long optionSetId) {
		Product product;
		try {
			product = productDao.findByOptionSetId(optionSetId);
		} catch (FoundNoProductException e) {
			e.printStackTrace();
			return ResponseDto.<OtherOptionSetDto>builder().msg(e.getMsg()).build();
		}
		List<OptionSet> findOptionSets = optionSetDao.findAllByProductId(product.getId());
		List<OtherOptionSetDto> productDtos = findOptionSets.stream()
				.map(t -> new OtherOptionSetDto(t)).collect(Collectors.toList());
		return ResponseDto.<OtherOptionSetDto>builder().data(productDtos).msg(ProductSuccessMsg.FIND_OTHER_OPTIONSETS).build();
	}

	// 최하위 카테고리 선택하고 나면 어떤 옵션 필터들 있는지 옵션 명과 옵션값 나열
	@Override
	@Transactional
	public ResponseDto<?> showOptionNameValues(Long categoryId) {
		List<OptionNamesValues> optionNameValue = optionDao.findOptionNameValueMapByCategoryId(categoryId);
		if(optionNameValue==null||optionNameValue.isEmpty()) {
			log.warn("no children categories found");
			return ResponseDto.builder().msg(ProductExceptionMsg.FOUND_NO_OPTIONS).build();
		}
		Map<String, Set<String>> dto = optionNameValue.stream().collect(Collectors.groupingBy(
				OptionNamesValues::getName, Collectors.mapping(OptionNamesValues::getValue, Collectors.toSet())));
		List<Map<String, Set<String>>> data = new ArrayList<>();
		data.add(dto);
		return ResponseDto.<Map<String, Set<String>>>builder().data(data).msg(ProductSuccessMsg.FIND_OPTION_NAME_VALUES).build();
	}

	// 노트북 카테고리의 전체 옵션들 찾기
	// 부모 아이디를 받아서 자식들 찾아서 자식들의 카테고리 옵션들 구해서 그걸 하나의 map으로 합치기
	@Override
	@Transactional
	public ResponseDto<?> showAllOptionNameValues(Long categoryId) {
		List<Long> childrenIds = categoryDao.findChildTypesByParentId(categoryId).stream().map(t -> t.getId())
				.collect(Collectors.toList());
		if(childrenIds.isEmpty()) {
			log.warn("no children categories found");
			return ResponseDto.builder().msg(ProductExceptionMsg.FOUND_NO_CATEGORY).build();
		}
		Map<String, Set<String>> dto = new HashMap<String, Set<String>>();
		for (int i = 0; i < childrenIds.size(); i++) {
			List<OptionNamesValues> optionNameValue = optionDao.findOptionNameValueMapByCategoryId(childrenIds.get(i));
			if(optionNameValue==null||optionNameValue.isEmpty()) {
				log.warn("no children categories found");
				return ResponseDto.builder().msg(ProductExceptionMsg.FOUND_NO_OPTIONS).build();
			}
			Map<String, Set<String>> result = optionNameValue.stream().collect(Collectors.groupingBy(
					OptionNamesValues::getName, Collectors.mapping(OptionNamesValues::getValue, Collectors.toSet())));
			result.forEach((key, value) -> dto.merge(key, value, (v1, v2) -> {
				v1.addAll(v2);
				return v1;
			}));
		}
		List<Map<String, Set<String>>> data = new ArrayList<>();
		data.add(dto);
		return ResponseDto.<Map<String, Set<String>>>builder().data(data).msg(ProductSuccessMsg.FIND_OPTION_NAME_VALUES).build();
	}

	// option update
	@Override
	@Transactional
	public ResponseDto<?> update(@Valid OptionDto dto) throws FoundNoOptionsException {
		Options created = optionDao.update(dto);
		List<Options> data = new ArrayList<>();
		data.add(created);
		return ResponseDto.<Options>builder().data(data).msg(ProductSuccessMsg.UPDATE_OPTION).build();
	}

	// 토탈프라이스도 계산
	// 프로덕트, 옵션, 옵션셋, 추가
	@Override
	@Transactional
	public ResponseDto<?> uploadProduct(@Valid UploadProductDto dto) {
		Product createdProduct = productDao.create(dto.getProduct());
		OptionSet createdOptionSet = optionSetDao.create(dto.getOptionSet());
		createdOptionSet.setProduct(createdProduct);
		int productPrice = createdProduct.getPrice();
		for (OptionDto option : dto.getOptions()) {
			Options createdOption = optionDao.save(option.toSaveDto());
			productPrice += option.getExtraPrice();
			createdOption.setOptionSet(createdOptionSet);
		}
		createdOptionSet.setTotalPrice(productPrice);
		List<ProductDto> data = new ArrayList<>();
		data.add(new ProductDto(createdOptionSet));
		return ResponseDto.<ProductDto>builder().data(data).msg(ProductSuccessMsg.UPLOAD_PRODUCT).build();
	}

	// 옵션셋 아이디로 옵션셋 찾기 디테일 들어갈때사용
	@Override
	public ResponseDto<ProductDto> findById(Long optionSetId) {
		OptionSet optionSet=null;
		try {
			optionSet = optionSetDao.findById(optionSetId);
		} catch (FoundNoOptionSetException e) {
			e.printStackTrace();
			return ResponseDto.<ProductDto>builder().msg(e.getMsg()).build();
		}
		List<ProductDto> data = new ArrayList<>();
		data.add(new ProductDto(optionSet));
		return ResponseDto.<ProductDto>builder().data(data).msg(ProductSuccessMsg.FIND_OPTIONSET_BY_ID).build();
	}

}
