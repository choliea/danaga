package com.danaga.service.product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.danaga.dao.product.OptionSetDao;
import com.danaga.dao.product.RecentViewDao;
import com.danaga.dto.ResponseDto;
import com.danaga.dto.product.ProductDto;
import com.danaga.dto.product.ProductListOutputDto;
import com.danaga.dto.product.RecentViewDto;
import com.danaga.exception.product.AlreadyExistsException.ExistsRecentViewException;
import com.danaga.exception.product.FoundNoObjectException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoMemberException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoOptionSetException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoRecentViewException;
import com.danaga.exception.product.ProductSuccessMsg;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class RecentViewServiceImpl implements RecentViewService{
	private final RecentViewDao recentViewDao;
	private final OptionSetDao optionSetDao;
	
	//product detail 조회시 recentView 추가 
	@Transactional
	public ResponseDto<?> addRecentView(@Valid RecentViewDto dto) throws FoundNoMemberException, FoundNoOptionSetException{
		try {
			recentViewDao.save(dto.toEntity(dto));
		} catch (ExistsRecentViewException e) {
			e.printStackTrace();
			return ResponseDto.builder().msg(e.getMsg()).build();
		}
		return ResponseDto.builder().msg(ProductSuccessMsg.ADD_RECENTVIEW).build();
	}
	
	//나의 최근본 상품 전체 삭제 
	@Transactional()
	public ResponseDto<?> removeMyRecentViews(Long memberId){
		try {
			recentViewDao.deleteAll(memberId);
		} catch (FoundNoRecentViewException e) {
			e.printStackTrace();
			return ResponseDto.builder().msg(e.getMsg()).build();
		}
		return ResponseDto.<ProductDto>builder().msg(ProductSuccessMsg.REMOVE_MY_RECENTVIEWS).build();
	}
	
	//최근본상품 하나 삭제 
	@Transactional
	public ResponseDto<?> removeRecentView(@Valid RecentViewDto dto){
		try {
			recentViewDao.delete(dto.toEntity(dto));
		} catch (FoundNoRecentViewException e) {
			e.printStackTrace();
			return ResponseDto.builder().msg(e.getMsg()).build();
		}
		return ResponseDto.builder().msg(ProductSuccessMsg.REMOVE_RECENTVIEW).build();
	}
	
	//나의 최근 본 상품 전체 조회 
	public ResponseDto<ProductListOutputDto> myAllRecentViews(Long memberId) throws FoundNoMemberException{
		List<ProductListOutputDto> myRecentViews=new ArrayList<>();
			myRecentViews = optionSetDao.findAllByRecentView_MemberId(memberId).stream().map(t -> new ProductListOutputDto(t)).collect(Collectors.toList());
		return ResponseDto.<ProductListOutputDto>builder().data(myRecentViews).msg(ProductSuccessMsg.FIND_MY_RECENTVIEWS).build();
	}
	
	//30일 지난 상품 삭제 
	@Transactional
	public ResponseDto<?> removeOldRecents(){
		recentViewDao.removeOldRecents();
		return ResponseDto.<String>builder().msg(ProductSuccessMsg.REMOVE_OLD_RECENTVIEWS).build();
	}
	
}
