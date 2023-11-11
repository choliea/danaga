package com.danaga.service.product;

import com.danaga.dto.ResponseDto;
import com.danaga.dto.product.InterestDto;
import com.danaga.dto.product.ProductListOutputDto;
import com.danaga.exception.product.FoundNoObjectException.FoundNoMemberException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoOptionSetException;

public interface InterestService {
	//제품에서 하트 누르면 관심제품 추가
	//제품에서 하트 누르면 관심제품 삭제
	ResponseDto<?> clickHeart(InterestDto dto) throws FoundNoMemberException, FoundNoOptionSetException;
	//나의 관심상품 전체 조회 
	ResponseDto<ProductListOutputDto> myInterestingList(Long memberId) throws FoundNoMemberException;
	//나의 관심상품 전체 삭제
	ResponseDto<?> emptyMyInterestingList(Long memberId) throws FoundNoMemberException;
	ResponseDto<?> deleteHeart(InterestDto dto) throws FoundNoMemberException, FoundNoOptionSetException;
	ResponseDto<?> isMyInterest(Long optionSetId, String username) throws FoundNoMemberException, FoundNoOptionSetException;
}
