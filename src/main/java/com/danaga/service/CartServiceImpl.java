package com.danaga.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Service;
import com.danaga.dao.MemberDao;
import com.danaga.dao.product.OptionSetDao;
import com.danaga.dto.CartDto;
import com.danaga.dto.FUserCartResponseDto;
import com.danaga.dto.ResponseDto;
import com.danaga.dto.SUserCartResponseDto;
import com.danaga.dto.product.ProductDto;

import lombok.RequiredArgsConstructor;
import com.danaga.entity.Cart;
import com.danaga.entity.OptionSet;
import com.danaga.exception.MemberNotFoundException;
import com.danaga.repository.CartRepository;

/*****************************************************************************************/
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
	private final MemberDao memberDao;
	private final CartRepository cartRepository;
	private final OptionSetDao optionSetDao;

	@Override
	public Cart findCart(String value, Long optionSetId) throws Exception {
		Long memberId = memberDao.findMember(value).getId();
		return cartRepository.findByOptionSetIdAndMemberId(optionSetId, memberId);
	}

	@Override
	public Integer isDuplicateProduct(String value, Long optionsetId) throws Exception {
		Long findId = memberDao.findMember(value).getId();
		Cart findCart = cartRepository.findByOptionSetIdAndMemberId(optionsetId, findId);
		if (findCart != null) {
			return findCart.getQty();
		} else {
			return 0;
		}
	}

	// 유저 카트 리스트
	@Override
	public List<SUserCartResponseDto> findsUserCartList(String sUserId) throws Exception {
		Long memberId = findMemberId(sUserId);
		List<Cart> findCarts = cartRepository.findByMemberId(memberId);
		List<SUserCartResponseDto> list = new ArrayList<>();
		for (int i = 0; i < findCarts.size(); i++) {
			list.add(SUserCartResponseDto.toDto(findCarts.get(i)));
		}
		return list;
	}

	// 예외잡기
	@Override
	public FUserCartResponseDto findfUserCartList(CartDto dto) throws Exception {
		OptionSet findOptionset = optionSetDao.findById(dto.getOptionSetId());
		return FUserCartResponseDto.toDto(findOptionset, dto.getQty());
	}

	// 카트에 동일제품 체크 후 장바구니 수량 업데이트 or 카트 인서트
	@Override
	public void addCart(CartDto dto, String value) throws Exception {
		// 로그인 유저 아이디와 OptionSetId 로 동일제품 찾기
		Cart findCart = cartRepository.findByOptionSetIdAndMemberId(dto.getOptionSetId(),
				memberDao.findMember(value).getId());
		// 인서트할 카트
		Cart addCart = Cart.builder().member(memberDao.findMember(value))
				.optionSet(optionSetDao.findById(dto.getOptionSetId())).qty(dto.getQty()).build();
		if (findCart == null) {
			cartRepository.save(addCart);
		} else {
			findCart.setQty(findCart.getQty() + dto.getQty());
			cartRepository.save(findCart);
		}
	}

	// 제품 수량 변경
	@Override
	public CartDto updateCartQty(CartDto dto, String value) throws Exception {
		Long memberId = memberDao.findMember(value).getId();
		Cart findCart = cartRepository.findByOptionSetIdAndMemberId(dto.getOptionSetId(), memberId);
		findCart.setQty(dto.getQty());
		cartRepository.save(findCart);
		return CartDto.builder().optionSetId(findCart.getId()).qty(dto.getQty()).build();
	}

	@Override
	public Cart findCart(Long id) {
		return cartRepository.findById(id).get();
	}

	// 옵션셋 변경
	@Override
	public List<SUserCartResponseDto> updateCartOptionSet(List<Long> ids, String sUserId) throws Exception {
		List<SUserCartResponseDto> updateCarts = new ArrayList<>();
		Long memberId = memberDao.findMember(sUserId).getId(); 
		Long oldOptionsetId = ids.get(0); 
		Long changeOptionsetId = ids.get(1);
		// 옵션셋 변경할 카트
		Cart findCart = cartRepository.findByOptionSetIdAndMemberId(oldOptionsetId, memberId); // 기존 옵션셋이 담긴 카트

		Cart newCart = Cart.builder().member(findCart.getMember()).optionSet(optionSetDao.findById(ids.get(1))).qty(1)
				.build(); // 변경된 옵션셋

		// 변경하고자하는 옵션셋과 멤버아이디로 이미 존재하는지 체크
		Cart findDuplicateCart = cartRepository.findByOptionSetIdAndMemberId(changeOptionsetId, memberId);

		if (findDuplicateCart == null) {
			// 변경할 카트 수량 1일시 
			if (findCart.getQty() == 1) {
				findCart.setOptionSet(optionSetDao.findById(changeOptionsetId));
				cartRepository.save(findCart);
				SUserCartResponseDto sUserCartResponseDto = SUserCartResponseDto.toDto(findCart);
				updateCarts.add(sUserCartResponseDto);
			} else if (findCart.getQty() >= 2) {
				// 기존 카트의 수량이 2 이상 , 중복 옵션셋 카트 X 
				findCart.setQty(findCart.getQty() - 1);
				cartRepository.save(findCart);
				cartRepository.save(newCart);
				updateCarts.add(SUserCartResponseDto.toDto(findCart));
				updateCarts.add(SUserCartResponseDto.toDto(newCart));
			}
		} else {
			if (findCart.getQty() == 1) {
				// 기존 카트의 수량 1 , 중복 옵션셋 카트 O 
				findDuplicateCart.setQty(findDuplicateCart.getQty() + 1);
				cartRepository.save(findDuplicateCart);
				cartRepository.deleteById(findCart.getId());
				updateCarts.add(SUserCartResponseDto.toDto(findDuplicateCart));
			} else if (findCart.getQty() >= 2) {
				// 기존 카트의 수량 2 이상 , 중복 옵션셋 카트 O 
				findCart.setQty(findCart.getQty() - 1);
				findDuplicateCart.setQty(findDuplicateCart.getQty() + 1);
				cartRepository.save(findCart);
				cartRepository.save(findDuplicateCart);
				updateCarts.add(SUserCartResponseDto.toDto(findDuplicateCart));
				updateCarts.add(SUserCartResponseDto.toDto(findCart));
			}
		}
		return updateCarts;
	}

	/*
	 * 카트 1개 삭제
	 */
	@Override
	public void deleteCart(Long optionSetId, String value) {
		Long memberId;
		try {
			memberId = memberDao.findMember(value).getId();
			Cart findCart = cartRepository.findByOptionSetIdAndMemberId(optionSetId, memberId);
			cartRepository.deleteById(findCart.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 카트 전체삭제 [세션(Controller) -> sUserId -> memberId -> delete ]
	public void deleteCarts(String sUserId) throws Exception {
		Long memberId = findMemberId(sUserId);
		cartRepository.deleteByMemberId(memberId);
	};

	// 헤더영역 장바구니 아이콘에 몇개 담긴지 숫자 표시
	@Override
	public int countCarts(String sUserId) throws Exception {
		Long memberId = findMemberId(sUserId);
		return cartRepository.countByMemberId(memberId);
	}

	Long findMemberId(String sUserId) throws Exception {
		return memberDao.findMember(sUserId).getId();
	};

}
/*****************************************************************************************/