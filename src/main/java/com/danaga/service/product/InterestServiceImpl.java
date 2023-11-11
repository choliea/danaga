package com.danaga.service.product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.danaga.dao.MemberDao;
import com.danaga.dao.product.InterestDao;
import com.danaga.dao.product.OptionSetDao;
import com.danaga.dto.ResponseDto;
import com.danaga.dto.product.InterestDto;
import com.danaga.dto.product.ProductListOutputDto;
import com.danaga.exception.product.AlreadyExistsException.ExistsInterestException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoInterestException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoMemberException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoOptionSetException;
import com.danaga.exception.product.ProductExceptionMsg;
import com.danaga.exception.product.ProductSuccessMsg;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@RequiredArgsConstructor
@Service
public class InterestServiceImpl implements InterestService {

	private final InterestDao interestDao;
	private final OptionSetDao optionSetDao;
	private final MemberDao memberDao;
	
	//상품이 내 관심상품인지 확인
	@Override
	@Transactional
	public ResponseDto<?> isMyInterest(Long optionSetId, String username) throws FoundNoMemberException,FoundNoOptionSetException {
		try {
			Long memberId = memberDao.findMember(username).getId();
			boolean isInterested = interestDao.isInterested(InterestDto.builder()
					.memberId(memberId).optionSetId(optionSetId).build());
				List<Boolean> answer=new ArrayList<Boolean>();
				answer.add(isInterested);
				return ResponseDto.<Boolean>builder().data(answer).msg(ProductSuccessMsg.IS_MY_INTEREST).build();
		} catch (Exception e) {
			throw new FoundNoMemberException();
		}
	}
	
	//제품에서 하트 누르면 관심제품 추가
	@Override
	@Transactional
	public ResponseDto<?> clickHeart(@Valid InterestDto dto) throws FoundNoMemberException, FoundNoOptionSetException {
			try {
				interestDao.save(dto);
			} catch (ExistsInterestException e) {
				e.printStackTrace();
				return ResponseDto.builder().msg(e.getMsg()).build();
			}
		return ResponseDto.builder().msg(ProductSuccessMsg.TAP_HEART).build();
	}
	//제품에서 하트 누르면 관심제품 삭제
	@Override
	@Transactional
	public ResponseDto<?> deleteHeart(@Valid InterestDto dto) throws FoundNoMemberException, FoundNoOptionSetException {
			try {
				if(interestDao.isInterested(dto)) {
					interestDao.delete(dto);
				}else {
					return ResponseDto.builder().msg(ProductExceptionMsg.IS_NOT_INTERESTEDD).build();
				}
			} catch (FoundNoInterestException e) {
				e.printStackTrace();
				return ResponseDto.builder().msg(e.getMsg()).build();
			}
		return ResponseDto.builder().msg(ProductSuccessMsg.UNTAP_HEART).build();
	}
	//나의 관심상품 리스트 전체 조회
	@Override
	public ResponseDto<ProductListOutputDto> myInterestingList(Long memberId) throws FoundNoMemberException {
		List<ProductListOutputDto> data=new ArrayList<>();
			data = optionSetDao.findAllByInterest_MemberId(memberId).stream().map(t -> new ProductListOutputDto(t)).collect(Collectors.toList());
		return ResponseDto.<ProductListOutputDto>builder().data(data).msg(ProductSuccessMsg.MY_INTERESTS).build();
	}
	//나의 관심상품 리스트 전체 삭제
	@Override
	@Transactional
	public ResponseDto<?> emptyMyInterestingList(Long memberId) throws FoundNoMemberException {
			interestDao.deleteAll(memberId);
		return ResponseDto.builder().msg(ProductSuccessMsg.REMOVE_MY_INTERESTS).build();
	}
	
}
