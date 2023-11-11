package com.danaga.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.danaga.dto.CartCheckResponseDto;
import com.danaga.dto.CartDto;
import com.danaga.entity.Cart;
import com.danaga.service.CartService;
import com.danaga.service.product.OptionSetService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

/*****************************************************************************************/
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartRestController {
	private final CartService cartService;
	private final OptionSetService optionSetService;

	static List<CartDto> fUserCarts; // 비회원 장바구니(세션)
	static String sUserId; // 로그인 유저 아이디
	static final Integer CART_TUNG = 1000;
	static final Integer NOT_CART_TUNG = 2000;
	static final Integer CART_QTY_MAX = 2100;
	static final Integer NOT_CART_QTY_MAX = 2200;
	static final Integer OUT_OF_STOCK = 3000;

	@PostMapping("/check")
	public ResponseEntity<CartCheckResponseDto> isDuplicate(@RequestBody CartDto dto, HttpSession session)
			throws Exception {
		sUserId = (String) session.getAttribute("sUserId");
		fUserCarts = (List<CartDto>) session.getAttribute("fUserCarts");
		boolean check = false;
		Integer returnCode = 0;
		String message = "";
		CartCheckResponseDto response = new CartCheckResponseDto();
		Integer osStock = optionSetService.findById(dto.getOptionSetId()).getData().get(0).getStock();
		if (osStock >= dto.getQty()) {
			if (sUserId != null) {
				Cart findCart = cartService.findCart(sUserId, dto.getOptionSetId());
				if (findCart == null) {
					returnCode = CART_TUNG;
					message = "장바구니로 이동하시겠습니까?";
				} else if (findCart != null) {
					returnCode = NOT_CART_TUNG;
					message = " 이미 장바구니에 존재하는 제품입니다.\n 장바구니에 담으시겠습니까?";
				}
			} else if (sUserId == null && fUserCarts == null) {
				returnCode = CART_TUNG;
				message = "장바구니로 이동하시겠습니까?";
			} else if (sUserId == null && fUserCarts != null) {
				check = isDuplicate(dto.getOptionSetId(), fUserCarts);
				if (check == true) {
					returnCode = NOT_CART_TUNG;
					message = " 이미 장바구니에 존재하는 제품입니다.\n 장바구니에 담으시겠습니까?";
				} else {
					returnCode = CART_TUNG;
					message = " 장바구니로 이동하시겠습니까 ? ";
				}

			}
		} else {
			returnCode = OUT_OF_STOCK;
			message = "현재 상품 재고량은 " + osStock + "개 입니다. ";
		}
		response.setMessage(message);
		response.setNo(returnCode);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping
	public ResponseEntity<CartCheckResponseDto> addCart(@RequestBody CartDto dto, HttpSession session)
			throws Exception {
		sUserId = (String) session.getAttribute("sUserId");
		fUserCarts = (List<CartDto>) session.getAttribute("fUserCarts");
		Integer returnNo = NOT_CART_QTY_MAX;
		String message = " 장바구니로 이동하시겠습니까 ? ";
		CartCheckResponseDto response = new CartCheckResponseDto();
		// 회원 + 세션 장바구니 비어있음
		if (sUserId != null && fUserCarts == null) {
			int qty = cartService.isDuplicateProduct(sUserId, dto.getOptionSetId());
			if (qty + dto.getQty() > 5) {
				returnNo = CART_QTY_MAX;
				message = "장바구니에는 5개까지 담을 수 있습니다. \n 장바구니로 이동하시겠습니까?";
				dto.setQty(5 - qty);
				cartService.addCart(dto, sUserId);
			} else {
				cartService.addCart(dto, sUserId);
				countCarts(session, fUserCarts);
			}
		} else {
			// 비회원 + 세션 장바구니 X
			if (sUserId == null && fUserCarts == null) {
				fUserCarts = new ArrayList<>();
				fUserCarts.add(dto);
				session.setAttribute("fUserCarts", fUserCarts);
				countCarts(session, fUserCarts);
			} else if (sUserId == null && fUserCarts != null) {
				// 비회원 + 세션 장바구니 O
				int findIndex = findFUserCart(fUserCarts, dto);
				if (findIndex == -1) {
					fUserCarts.add(dto);
					countCarts(session, fUserCarts);
				} else {
					if (fUserCarts.get(findIndex).getQty() + dto.getQty() > 5) {
						message = "장바구니에는 5개까지 담을 수 있습니다. \n 장바구니로 이동하시겠습니까?";
						returnNo = CART_QTY_MAX;
						fUserCarts.get(findIndex).setQty(5);
					} else {
						fUserCarts.get(findIndex).setQty(fUserCarts.get(findIndex).getQty() + dto.getQty());
					}
					session.setAttribute("fUserCarts", fUserCarts);
					countCarts(session, fUserCarts);
				}
			}

		}
		response.setMessage(message);
		response.setNo(returnNo);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping("/optionset")
	public ResponseEntity<Integer> updateOptionset(@RequestBody List<Long> ids, HttpSession session) throws Exception {
		sUserId = (String) session.getAttribute("sUserId");
		fUserCarts = (List<CartDto>) session.getAttribute("fUserCarts");
		Long oldId = ids.get(0);
		Long changeId = ids.get(1);
		CartDto oldFUserCart = null;
		CartDto changeFUserCart = null;
		boolean isDuplicateId = false;
		Integer returnNo = NOT_CART_QTY_MAX;
		// 회원
		if (sUserId != null) {
			Cart findCart = cartService.findCart(sUserId, changeId);
			if (findCart != null) {
				if (findCart.getQty() >= 5) {
					returnNo = CART_QTY_MAX;
				} else {
					cartService.updateCartOptionSet(ids, sUserId);
				}
			} else {
				cartService.updateCartOptionSet(ids, sUserId);
			}
		} else if (fUserCarts != null) {
			// 비회원
			// 필요 데이타 구하기
			for (int i = 0; i < fUserCarts.size(); i++) {
				if (oldId == fUserCarts.get(i).getOptionSetId()) {
					oldFUserCart = fUserCarts.get(i);
				} else if (changeId == fUserCarts.get(i).getOptionSetId()) {
					isDuplicateId = true;
					changeFUserCart = fUserCarts.get(i);
				}
			}
			int oldFUserCartIndex = fUserCarts.indexOf(oldFUserCart);
			int ChangeFUserIndex = fUserCarts.indexOf(changeFUserCart);
			// [ 중복 제품 O ]
			if (isDuplicateId == true) {
				// 수량 제한 체크
				if (changeFUserCart.getQty() >= 5) {
					returnNo = CART_QTY_MAX;
				} else {
					// 변경 전 수량 1 --> remove , 수량 증가
					if (oldFUserCart.getQty() == 1) {
						changeFUserCart.setQty(changeFUserCart.getQty() + 1);
						fUserCarts.set(ChangeFUserIndex, changeFUserCart);
						fUserCarts.remove(oldFUserCart);
					} else if (oldFUserCart.getQty() >= 2) {
						// 변경 전 수량 >=2 --> 수량 감소 , 수량 증가
						oldFUserCart.setQty(oldFUserCart.getQty() - 1);
						changeFUserCart.setQty(changeFUserCart.getQty() + 1);
						fUserCarts.set(oldFUserCartIndex, oldFUserCart);
						fUserCarts.set(ChangeFUserIndex, changeFUserCart);
					}
				}
			} else if (isDuplicateId == false) { // 중복 제품 X
				// 변경 전 수량 1
				if (oldFUserCart.getQty() == 1) {
					fUserCarts.remove(oldFUserCart);
					CartDto newFUserCart = CartDto.builder().optionSetId(changeId).qty(1).build();
					fUserCarts.add(newFUserCart);
				} else if (oldFUserCart.getQty() >= 2) {
					// 변경 전 수량 >= 2
					oldFUserCart.setQty(oldFUserCart.getQty() - 1);
					CartDto newFUserCart = CartDto.builder().optionSetId(changeId).qty(1).build();
					fUserCarts.set(oldFUserCartIndex, oldFUserCart);
					fUserCarts.add(newFUserCart);
				}
			}
		} else {
			returnNo = 500;
			System.out.println("이거 보이면 나도모름 말도안되는 에러");
		}
		session.setAttribute("fUserCarts", fUserCarts);
		countCarts(session, fUserCarts);
		return ResponseEntity.status(HttpStatus.OK).body(returnNo);
	}

	@PutMapping("/qty")
	public ResponseEntity<CartDto> updateQty(@RequestBody CartDto dto, HttpSession session) throws Exception {
		// 로그인 유저 체크
		sUserId = (String) session.getAttribute("sUserId");
		fUserCarts = (List<CartDto>) session.getAttribute("fUserCarts");

		if (sUserId != null) {
			cartService.updateCartQty(dto, sUserId);
			return ResponseEntity.status(HttpStatus.OK).body(dto);
		} else {
			for (int i = 0; i < fUserCarts.size(); i++) {
				// 비회원일 경우 카트리스트를 돌리면서 dto의 optionsetId 와 동일한 옵션셋 아이디 체크
				if (dto.getOptionSetId().equals(fUserCarts.get(i).getOptionSetId())) {
					// 동일한 세션카트의 옵션셋 꺼내서 수량변경 후 세션에 다시 저장
					fUserCarts.get(i).setQty(dto.getQty());
					session.setAttribute("fUserCarts", fUserCarts);
					break;
				}
			}
			return ResponseEntity.status(HttpStatus.OK).body(dto);
		}

	}

	@DeleteMapping(value = "/deletecart")
	public void deleteCart(@RequestParam(name = "idlist[]") List<Long> idList, HttpSession session) throws Exception {
		sUserId = (String) session.getAttribute("sUserId");
		fUserCarts = (List<CartDto>) session.getAttribute("fUserCarts");
		// 회원일 경우
		if (sUserId != null) {
			for (int i = 0; i < idList.size(); i++) {
				cartService.deleteCart(idList.get(i), sUserId);
			}
		} else if (sUserId == null && fUserCarts != null) { // 비회원일 경우
			// 선택 optionsetId 와 카트리스트의 optionsetId 동일한 것 찾고 삭제 후 세션에 저장
			for (int i = 0; i < idList.size(); i++) {
				for (int j = 0; j < fUserCarts.size(); j++) {
					if (idList.get(i).equals(fUserCarts.get(j).getOptionSetId())) {
						fUserCarts.remove(j);
						break;
					}
				}
			}
			countCarts(session, fUserCarts);
			if (fUserCarts.isEmpty()) {
				session.setAttribute("fUserCarts", null);
			} else if (fUserCarts != null) {
				session.setAttribute("fUserCarts", fUserCarts);
			}
		}

	}

	// 장바구니에 몇개 담겼는지 숫자 체크
	void countCarts(HttpSession session, List<CartDto> list) throws Exception {
		sUserId = (String) session.getAttribute("sUserId");
		if (sUserId != null) {
			session.setAttribute("countCarts", cartService.countCarts(sUserId));
		} else if (list != null) { // 비회원 일시 장바구니 리스트의 사이즈
			session.setAttribute("countCarts", list.size());
		}
	};

	// 비회원 장바구니 아이템 넣기 [fUserCarts : 세션 장바구니 ,dto : 장바구니 담을 제품]
	int findFUserCart(List<CartDto> fUserCarts, CartDto dto) throws Exception {
		int findIndex = -1;
		for (int i = 0; i < fUserCarts.size(); i++) {
			if (dto.getOptionSetId().equals(fUserCarts.get(i).getOptionSetId())) {
				findIndex = i;
			}
		}
		return findIndex;
	}

	// 장바구니에 담겨있는지 체크
	static boolean isDuplicate(Long id, List<CartDto> lists) {
		boolean check = false;
		for (int i = 0; i < lists.size(); i++) {
			if (lists.get(i).getOptionSetId().equals(id)) {
				check = true;
			}
		}
		return check;
	}

}

/*****************************************************************************************/