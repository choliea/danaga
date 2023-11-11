package com.danaga.dao.product;

import org.springframework.stereotype.Repository;

import com.danaga.dto.product.ProductSaveDto;
import com.danaga.entity.Product;
import com.danaga.exception.product.FoundNoObjectException.FoundNoProductException;
import com.danaga.repository.product.ProductRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductDaoImpl implements ProductDao{
	private final ProductRepository repository;

	@Override
	public Product findById(Long id) throws FoundNoProductException {
		return repository.findById(id).orElseThrow(() -> new FoundNoProductException());
	}

	@Override
	public Product findByOptionSetId(Long optionSetId) throws FoundNoProductException {
		Product find=repository.findByOptionSets_Id(optionSetId);
		if(find==null) {
			throw new FoundNoProductException();
		}
		return find;
	}

	@Override
	public Product create(ProductSaveDto dto) {
		return repository.save(dto.toEntity());
	}

	@Override
	public void deleteById(Long id) throws FoundNoProductException {
		Product find = repository.findById(id).orElseThrow(() -> new FoundNoProductException());
		repository.deleteById(find.getId());
	}

//	private void validate(final Product entity) {// entity의 조작(재할당)을 막기 위해 final
//		if (entity == null) {
//			log.warn("entity cannot be null");
//			throw new RuntimeException("entity cannot be null");
//		}
//		if (entity.getId() == null) {
//			log.warn("unknown user");
//			throw new RuntimeException("unknown user");
//		}
//	}
}
