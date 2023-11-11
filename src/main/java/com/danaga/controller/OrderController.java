package com.danaga.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.danaga.dao.OrderDao;
import com.danaga.dto.CartDto;
import com.danaga.dto.CartOrderDto;
import com.danaga.dto.DeliveryDto;
import com.danaga.dto.MemberResponseDto;
import com.danaga.dto.OrderGuestDto;
import com.danaga.dto.OrderItemDto;
import com.danaga.dto.OrderMemberBasicDto;
import com.danaga.dto.OrderTotalDto;
import com.danaga.dto.OrdersDto;
import com.danaga.dto.OrdersGuestDetailDto;
import com.danaga.dto.OrdersProductDto;
import com.danaga.dto.ResponseDto;
import com.danaga.dto.product.OptionSetUpdateDto;
import com.danaga.dto.product.ProductDto;
import com.danaga.entity.Member;
import com.danaga.repository.MemberRepository;
import com.danaga.service.CartService;
import com.danaga.service.MemberService;
import com.danaga.service.OrderService;
import com.danaga.service.product.OptionSetService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;
	private final CartService cartService;
	private final OptionSetService optionSetService;
	private final MemberService memberService;
	private final MemberRepository memberRepository;
	private final OrderDao orderDao;

	/*
	 * 주문완료알림페이지에서 index.html로 돌아가기 session으로 회원일 떄 비회원일 때
	 */
	@GetMapping("/order_complete_to_index")
	public String orderCompleteToIndex() {
		return "/index";
	}

	/******************************* 회원 ****************************/
	/*
	 * 주문+주문아이템 목록(회원)
	 */
	@LoginCheck
	@GetMapping("/member_order_List")
	public String memberOrderList(Model model, HttpSession session) {// model은 데이터를 담아서 넘겨주는역활

		try {
//		   String sUserId = "User1";
			String loginUser = (String) session.getAttribute("sUserId");
			System.out.println("@@@@@@@@@"+loginUser);
			List<OrdersDto> orderDtoList = orderService.memberOrderList(loginUser);
			// orderDtoList를 id 기준으로 오름차순 정렬
			orderDtoList.sort(Comparator.comparing(OrdersDto::getId));
			model.addAttribute("orderDtoList", orderDtoList);
			System.out.println("%%%%%%%%%%%"+orderDtoList);
			Member member = Member.toResponseEntity(memberService.getMemberBy(loginUser));
			model.addAttribute("loginUser", member);
			System.out.println("@@@@@@@@@"+member);
			return "orders/order_member_list";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("주문목록이 없습니다?", e.getMessage());
			return "/index";
		}
	}

	/*
	 * 상품에서 주문(form)(공통)
	 */
	@GetMapping("/product_order_form")
	public String memberProductOrderAddForm(@ModelAttribute("cartDto") CartDto cartDto, Model model,
			HttpSession session) throws Exception {
		String sUserId = (String) session.getAttribute("sUserId");

		if (sUserId == null) {// 비회원

			ResponseDto<?> responseDto = optionSetService.findById(cartDto.getOptionSetId());
			List<ProductDto> productDtoList = (List<ProductDto>) responseDto.getData();
			ProductDto productDto = productDtoList.get(0);

			List<CartOrderDto> sUserCartOrderDtoList = new ArrayList<>();
			CartOrderDto sUserCartOrderDto = CartOrderDto.builder().id(cartDto.getOptionSetId()).qty(cartDto.getQty())
					.productName(productDto.getName()).totalPrice(productDto.getTotalPrice())
					.build();
			sUserCartOrderDtoList.add(sUserCartOrderDto);

			Integer realTotalPrice = 0;
			for (int i = 0; i < sUserCartOrderDtoList.size(); i++) {
				realTotalPrice += sUserCartOrderDtoList.get(i).getTotalPrice() * sUserCartOrderDtoList.get(i).getQty();
				System.out.println(realTotalPrice);
			}
			OrderMemberBasicDto orderMemberBasicDto = new OrderMemberBasicDto("", "ex) 010-1111-1111","");
			model.addAttribute("orderMemberBasicDto", orderMemberBasicDto);
			model.addAttribute("realTotalPrice", realTotalPrice);
			model.addAttribute("sUserCartOrderDto", sUserCartOrderDtoList);
			session.setAttribute("orderMemberBasicDto", orderMemberBasicDto);
			session.setAttribute("realTotalPrice", realTotalPrice);
			session.setAttribute("sUserCartOrderDto", sUserCartOrderDtoList);
			return "orders/order_save_form";
		} else { // 회원
			MemberResponseDto memberResponseDto = memberService.getMemberBy(sUserId);
			ResponseDto<?> responseDto = optionSetService.findById(cartDto.getOptionSetId());
			List<ProductDto> productDtoList = (List<ProductDto>) responseDto.getData();
			
			ProductDto productDto = productDtoList.get(0);

			Integer discountRate = gradePoint(memberResponseDto.getGrade());
			List<CartOrderDto> sUserCartOrderDtoList = new ArrayList<>();
			CartOrderDto sUserCartOrderDto = CartOrderDto.builder().id(cartDto.getOptionSetId()).qty(cartDto.getQty())
					.productName(productDto.getName()).totalPrice(productDto.getTotalPrice()-(productDto.getTotalPrice()*discountRate/100)).build();
			sUserCartOrderDtoList.add(sUserCartOrderDto);

			Integer realTotalPrice = 0;
			for (int i = 0; i < sUserCartOrderDtoList.size(); i++) {
				realTotalPrice += (sUserCartOrderDtoList.get(i).getTotalPrice()* sUserCartOrderDtoList.get(i).getQty());
				System.out.println(realTotalPrice);
			}
			OrderMemberBasicDto orderMemberBasicDto = new OrderMemberBasicDto(memberResponseDto.getName(),
					memberResponseDto.getPhoneNo(),memberResponseDto.getEmail());
			model.addAttribute("orderMemberBasicDto", orderMemberBasicDto);
			model.addAttribute("realTotalPrice", realTotalPrice);
			model.addAttribute("sUserCartOrderDto", sUserCartOrderDtoList);
			session.setAttribute("sUserCartOrderDto", sUserCartOrderDtoList);
			session.setAttribute("realTotalPrice", realTotalPrice);
			session.setAttribute("orderMemberBasicDto", orderMemberBasicDto);
			return "orders/order_save_form";
		}
	}
	
	
	
	
	
	
	/*
	 * 상품에서 주문(action)(공통)
	 */
	@PostMapping("/product_order_action") 
	public String memberProductOrderAddAction(@ModelAttribute("ordersProductDto") OrderTotalDto orderTotalDto,
			 Model model, HttpSession session) {

		String sUserId = (String) session.getAttribute("sUserId");
		int countCarts = 0;
		if (sUserId == null) { // 비회원주문
			try {
				List<CartOrderDto> sUserCartOrderDtoList = (List<CartOrderDto>) session
						.getAttribute("sUserCartOrderDto");
				List<CartDto> fUserCarts = new ArrayList<>();
				List<OptionSetUpdateDto> optionSetUpdateDtoList = new ArrayList<>();
				for (int i = 0; i < sUserCartOrderDtoList.size(); i++) {
					CartDto cartDto = CartDto.builder().optionSetId(sUserCartOrderDtoList.get(i).getId())
							.qty(sUserCartOrderDtoList.get(i).getQty()).build();
					fUserCarts.add(cartDto);

					List<ProductDto> productDtoList = (List<ProductDto>) optionSetService
							.findById(cartDto.getOptionSetId()).getData();

					OptionSetUpdateDto optionSetUpdateDto = OptionSetUpdateDto.builder().id(cartDto.getOptionSetId())
							.stock(productDtoList.get(0).getStock() - cartDto.getQty()).build();
					optionSetService.updateStock(optionSetUpdateDto);
					optionSetUpdateDtoList.add(optionSetUpdateDto);
				}

				DeliveryDto deliveryDto = new DeliveryDto();
				deliveryDto.setName(orderTotalDto.getReceiverName());
				deliveryDto.setPhoneNumber(orderTotalDto.getReceiverPhoneNo());
				deliveryDto.setAddress(orderTotalDto.getReceiverAddress());
				deliveryDto.setDetailAddress(orderTotalDto.getReceiverDetailAddress());
				deliveryDto.setPostCode(orderTotalDto.getReceiverPostCode());
				OrderGuestDto orderGuestDto = new OrderGuestDto();
				orderGuestDto.setName(orderTotalDto.getOrdererName());
				orderGuestDto.setPhoneNo(orderTotalDto.getOrdererPhoneNo());
				orderGuestDto.setEmail(orderTotalDto.getOrdererEmail());

				OrdersDto ordersDto = orderService.guestCartSelectOrderSave(deliveryDto, fUserCarts, orderGuestDto);

				List<CartDto> cartDtos = (List<CartDto>) session.getAttribute("fUserCarts");
				if(cartDtos!=null) {
					for (int i = 0; i < sUserCartOrderDtoList.size(); i++) {
						for (int j = 0; j < cartDtos.size(); j++) {
							if (cartDtos.get(j).getOptionSetId() == sUserCartOrderDtoList.get(i).getId()) {
								cartDtos.remove(cartDtos.get(j));
							}
						}
					}
					// size=0 이면 전체주문 null 세션에 넣기 or size !=0 이면 선택주문 삭제된 cartDtos
					if (cartDtos.size() == 0) {
						cartDtos = null;
					} else {
						countCarts = cartDtos.size();
					}
					System.out.println(">>>>> order cart 조건문 끝 " + cartDtos + countCarts);
					System.out.println("$$$$" + sUserCartOrderDtoList.size());
					session.setAttribute("fUserCarts", cartDtos);
					System.out.println(cartDtos);
					System.out.println("$$$$$" + cartDtos);
				}
				session.setAttribute("sUserCartOrderDtoList", sUserCartOrderDtoList);
				session.setAttribute("realTotalPrice", 0);
				session.setAttribute("countCarts", countCarts);
				OrderMemberBasicDto orderMemberBasicDto = (OrderMemberBasicDto) session
						.getAttribute("orderMemberBasicDto");
				model.addAttribute("orderId", ordersDto.getId());
				model.addAttribute("email",orderMemberBasicDto.getEmail());
				orderMemberBasicDto.setUserName("");
				orderMemberBasicDto.setPhoneNo("");
				orderMemberBasicDto.setEmail("");
				session.setAttribute("orderMemberBasicDto", orderMemberBasicDto);
				return "orders/order_complete";
			} catch (Exception e) {
				model.addAttribute("msg", e.getMessage());
				e.printStackTrace();
				return "/index";
			}
		} else { // 회원주문
			try {
				List<CartOrderDto> sUserCartOrderDtoList = (List<CartOrderDto>) session
						.getAttribute("sUserCartOrderDto");
				List<CartDto> fUserCarts = new ArrayList<>();
				List<OptionSetUpdateDto> optionSetUpdateDtoList = new ArrayList<>();
				for (int i = 0; i < sUserCartOrderDtoList.size(); i++) {
					CartDto cartDto = CartDto.builder().optionSetId(sUserCartOrderDtoList.get(i).getId())
							.qty(sUserCartOrderDtoList.get(i).getQty()).build();
					fUserCarts.add(cartDto);

					List<ProductDto> productDtoList = (List<ProductDto>) optionSetService
							.findById(cartDto.getOptionSetId()).getData();

					OptionSetUpdateDto optionSetUpdateDto = OptionSetUpdateDto.builder().id(cartDto.getOptionSetId())
							.stock(productDtoList.get(0).getStock() - cartDto.getQty()).build();
					optionSetUpdateDtoList.add(optionSetUpdateDto);
					optionSetService.updateStock(optionSetUpdateDto);
				}
				DeliveryDto deliveryDto = new DeliveryDto();
				deliveryDto.setName(orderTotalDto.getReceiverName());
				deliveryDto.setPhoneNumber(orderTotalDto.getReceiverPhoneNo());
				deliveryDto.setAddress(orderTotalDto.getReceiverAddress());
				deliveryDto.setDetailAddress(orderTotalDto.getReceiverDetailAddress());
				deliveryDto.setPostCode(orderTotalDto.getReceiverPostCode());
				OrdersDto ordersDto = orderService.memberCartSelectOrderSave(sUserId, deliveryDto, fUserCarts);

				sUserCartOrderDtoList.clear();
				System.out.println("$$$$" + sUserCartOrderDtoList.size());
				session.setAttribute("sUserCartOrderDtoList", sUserCartOrderDtoList);
				session.setAttribute("realTotalPrice", 0);
				session.setAttribute("countCarts", cartService.countCarts(sUserId));
				OrderMemberBasicDto orderMemberBasicDto = (OrderMemberBasicDto) session
						.getAttribute("orderMemberBasicDto");
				model.addAttribute("orderId", ordersDto.getId());
				orderMemberBasicDto.setUserName("");
				orderMemberBasicDto.setPhoneNo("");
				orderMemberBasicDto.setEmail("");
				session.setAttribute("orderMemberBasicDto", orderMemberBasicDto);
				return "orders/order_complete";
			} catch (Exception e) {
				model.addAttribute("msg", e.getMessage());
				e.printStackTrace();
				return "/index";
			}
		}
	}
	
	/*
	 * 카트에서 보내온 데이터로 주문(form)(공통)
	 */
	@PostMapping("/cart_order_form")
	public String memberCartOrderAddForm(@RequestBody List<CartOrderDto> sUserCartOrderDtoList, Model model,
			HttpSession session) throws Exception {
		System.out.println("###########" + sUserCartOrderDtoList.size());
		System.out.println(sUserCartOrderDtoList);

		String sUserId = (String) session.getAttribute("sUserId");
		if (sUserId != null) { // 회원
			MemberResponseDto memberResponseDto = memberService.getMemberBy(sUserId);
			Integer discountRate = gradePoint(memberResponseDto.getGrade());
			Integer realTotalPrice = 0;
			for (int i = 0; i < sUserCartOrderDtoList.size(); i++) {
				sUserCartOrderDtoList.get(i).setTotalPrice(
						(sUserCartOrderDtoList.get(i).getTotalPrice() * sUserCartOrderDtoList.get(i).getQty())
								- (sUserCartOrderDtoList.get(i).getTotalPrice() * sUserCartOrderDtoList.get(i).getQty()
										* discountRate / 100));
				realTotalPrice += sUserCartOrderDtoList.get(i).getTotalPrice() * sUserCartOrderDtoList.get(i).getQty();
				System.out.println(realTotalPrice);
			}
			OrderMemberBasicDto orderMemberBasicDto = new OrderMemberBasicDto(memberResponseDto.getName(),
					memberResponseDto.getPhoneNo(),memberResponseDto.getEmail());
			System.out.println("333333333" + realTotalPrice);
			model.addAttribute("orderMemberBasicDto", orderMemberBasicDto);
			model.addAttribute("sUserCartOrderDto", sUserCartOrderDtoList);
			model.addAttribute("realTotalPrice", realTotalPrice);
			session.setAttribute("orderMemberBasicDto", orderMemberBasicDto);
			session.setAttribute("sUserCartOrderDto", sUserCartOrderDtoList);
			session.setAttribute("realTotalPrice", realTotalPrice);
			return "orders/order_save_form";
		} else { // 비회원
			Integer realTotalPrice = 0;
			for (int i = 0; i < sUserCartOrderDtoList.size(); i++) {
				realTotalPrice += sUserCartOrderDtoList.get(i).getTotalPrice() * sUserCartOrderDtoList.get(i).getQty();
				System.out.println(realTotalPrice);
			}

			OrderMemberBasicDto orderMemberBasicDto = new OrderMemberBasicDto("", "ex) 010-1111-1111","");
			model.addAttribute("orderMemberBasicDto", orderMemberBasicDto);
			model.addAttribute("sUserCartOrderDto", sUserCartOrderDtoList);
			model.addAttribute("realTotalPrice", realTotalPrice);
			session.setAttribute("orderMemberBasicDto", orderMemberBasicDto);
			session.setAttribute("sUserCartOrderDto", sUserCartOrderDtoList);
			session.setAttribute("realTotalPrice", realTotalPrice);
			return "orders/order_save_form";
		}

	}

	@GetMapping("/order_save_form")
	public String orderSaveForm(Model model, HttpSession session) {
		model.addAttribute("orderMemberBasicDto", session.getAttribute("orderMemberBasicDto"));
		model.addAttribute("sUserCartOrderDto", session.getAttribute("sUserCartOrderDto"));
		model.addAttribute("realTotalPrice", session.getAttribute("realTotalPrice"));
		return "orders/order_save_form";

	}

	/*
	 * 카트에서 보내온 데이터로 주문(action)(공통)
	 */
	@PostMapping("/cart_order_action")
	public String memberCartSelectOrderAddAction(@ModelAttribute("orderTotalDto") OrderTotalDto orderTotalDto,
			Model model, HttpSession session) {
		String sUserId = (String) session.getAttribute("sUserId");
		int countCarts = 0;
		if (sUserId == null) { // 비회원주문
			try {
				List<CartOrderDto> sUserCartOrderDtoList = (List<CartOrderDto>) session
						.getAttribute("sUserCartOrderDto");
				List<CartDto> fUserCarts = new ArrayList<>();
				List<OptionSetUpdateDto> optionSetUpdateDtoList = new ArrayList<>();
				for (int i = 0; i < sUserCartOrderDtoList.size(); i++) {
					CartDto cartDto = CartDto.builder().optionSetId(sUserCartOrderDtoList.get(i).getId())
							.qty(sUserCartOrderDtoList.get(i).getQty()).build();
					fUserCarts.add(cartDto);

					List<ProductDto> productDtoList = (List<ProductDto>) optionSetService
							.findById(cartDto.getOptionSetId()).getData();

					OptionSetUpdateDto optionSetUpdateDto = OptionSetUpdateDto.builder().id(cartDto.getOptionSetId())
							.stock(productDtoList.get(0).getStock() - cartDto.getQty()).build();
					optionSetService.updateStock(optionSetUpdateDto);
					optionSetUpdateDtoList.add(optionSetUpdateDto);
				}

				DeliveryDto deliveryDto = new DeliveryDto();
				deliveryDto.setName(orderTotalDto.getReceiverName());
				deliveryDto.setPhoneNumber(orderTotalDto.getReceiverPhoneNo());
				deliveryDto.setAddress(orderTotalDto.getReceiverAddress());
				deliveryDto.setDetailAddress(orderTotalDto.getReceiverDetailAddress());
				deliveryDto.setPostCode(orderTotalDto.getReceiverPostCode());
				OrderGuestDto orderGuestDto = new OrderGuestDto();
				orderGuestDto.setName(orderTotalDto.getOrdererName());
				orderGuestDto.setPhoneNo(orderTotalDto.getOrdererPhoneNo());
				orderGuestDto.setEmail(orderTotalDto.getOrdererEmail());
				System.out.println("999999999999999999999999"+orderGuestDto);
				OrdersDto ordersDto = orderService.guestCartSelectOrderSave(deliveryDto, fUserCarts, orderGuestDto);
				System.out.println("$$$$$$$$$$$$$$$$$$$$$$$"+ordersDto);
				
				model.addAttribute("orderId", ordersDto.getId());
				model.addAttribute("email", orderGuestDto.getEmail());
				List<CartDto> cartDtos = (List<CartDto>) session.getAttribute("fUserCarts");
				if(cartDtos!=null) {
					for (int i = 0; i < sUserCartOrderDtoList.size(); i++) {
						for (int j = 0; j < cartDtos.size(); j++) {
							if (cartDtos.get(j).getOptionSetId() == sUserCartOrderDtoList.get(i).getId()) {
								cartDtos.remove(cartDtos.get(j));
							}
						}
					}
					// size=0 이면 전체주문 null 세션에 넣기 or size !=0 이면 선택주문 삭제된 cartDtos
					if (cartDtos.size() == 0) {
						cartDtos = null;
					} else {
						countCarts = cartDtos.size();
					}
					System.out.println(">>>>> order cart 조건문 끝 " + cartDtos + countCarts);
					System.out.println("$$$$" + sUserCartOrderDtoList.size());
					session.setAttribute("fUserCarts", cartDtos);
					System.out.println(cartDtos);
					System.out.println("$$$$$" + cartDtos);
				}
				session.setAttribute("sUserCartOrderDtoList", sUserCartOrderDtoList);
				session.setAttribute("realTotalPrice", 0);
				session.setAttribute("countCarts", countCarts);
				OrderMemberBasicDto orderMemberBasicDto = (OrderMemberBasicDto) session
						.getAttribute("orderMemberBasicDto");
				orderMemberBasicDto.setUserName("");
				orderMemberBasicDto.setPhoneNo("");
				session.setAttribute("orderMemberBasicDto", orderMemberBasicDto);
				return "orders/order_complete";
			} catch (Exception e) {
				model.addAttribute("msg", e.getMessage());
				e.printStackTrace();
				return "/404";
			}
		} else { // 회원주문
			try {
				List<CartOrderDto> sUserCartOrderDtoList = (List<CartOrderDto>) session
						.getAttribute("sUserCartOrderDto");
				List<CartDto> fUserCarts = new ArrayList<>();
				List<OptionSetUpdateDto> optionSetUpdateDtoList = new ArrayList<>();
				for (int i = 0; i < sUserCartOrderDtoList.size(); i++) {
					CartDto cartDto = CartDto.builder().optionSetId(sUserCartOrderDtoList.get(i).getId())
							.qty(sUserCartOrderDtoList.get(i).getQty()).build();
					fUserCarts.add(cartDto);

					List<ProductDto> productDtoList = (List<ProductDto>) optionSetService
							.findById(cartDto.getOptionSetId()).getData();

					OptionSetUpdateDto optionSetUpdateDto = OptionSetUpdateDto.builder().id(cartDto.getOptionSetId())
							.stock(productDtoList.get(0).getStock() - cartDto.getQty()).build();
					optionSetUpdateDtoList.add(optionSetUpdateDto);
					optionSetService.updateStock(optionSetUpdateDto);
				}
				DeliveryDto deliveryDto = new DeliveryDto();
				deliveryDto.setName(orderTotalDto.getReceiverName());
				deliveryDto.setPhoneNumber(orderTotalDto.getReceiverPhoneNo());
				deliveryDto.setAddress(orderTotalDto.getReceiverAddress());
				deliveryDto.setDetailAddress(orderTotalDto.getReceiverDetailAddress());
				deliveryDto.setPostCode(orderTotalDto.getReceiverPostCode());
				OrdersDto ordersDto = orderService.memberCartSelectOrderSave(sUserId, deliveryDto, fUserCarts);
				for (CartDto cartDto : fUserCarts) {
					if(cartService.findCart(sUserId,cartDto.getOptionSetId())!=null) {
						cartService.deleteCart(cartDto.getOptionSetId(), sUserId);
					}
				}
				sUserCartOrderDtoList.clear();
				System.out.println("$$$$" + sUserCartOrderDtoList.size());
				session.setAttribute("sUserCartOrderDtoList", sUserCartOrderDtoList);
				session.setAttribute("realTotalPrice", 0);
				session.setAttribute("countCarts", cartService.countCarts(sUserId));
				OrderMemberBasicDto orderMemberBasicDto = (OrderMemberBasicDto) session
						.getAttribute("orderMemberBasicDto");
				orderMemberBasicDto.setUserName("");
				orderMemberBasicDto.setPhoneNo("");
				session.setAttribute("orderMemberBasicDto", orderMemberBasicDto);
				model.addAttribute("orderId", ordersDto.getId());
				model.addAttribute("email",orderMemberBasicDto.getEmail());
				return "orders/order_complete";
			}  catch (Exception e) {
				model.addAttribute("msg", e.getMessage());
				e.printStackTrace();
				return "/index";
			}
		}
	}
	
	
	/******************************* 비회원 ****************************/

	/*
	 * 로그아웃 상태에서 메인페이지에서 상단에서 페이지버튼 클릭 후 FIND ORDER GUEST를 클릭하면 비회원 찾기 폼으로 넘어가는 거
	 */

	@GetMapping("/find_order_guest")
	public String findOrderGuest(Model model) {
		OrdersGuestDetailDto ordersGuestDetailDto = new OrdersGuestDetailDto();
		model.addAttribute("ordersGuestDetailDto", ordersGuestDetailDto);
		return "orders/find_order_guest";
	}

	/*
	 * 주문+주문아이템 목록(비회원) 주문List보기(비회원) 비회원 찾는 폼에서 데이터를 보내줘서 이 url로 받으면 list뿌려주고 디테일까지
	 */
	@GetMapping("/order_guest_detail")
	public String guestOrderList(@ModelAttribute("ordersGuestDetailDto") OrdersGuestDetailDto ordersGuestDetailDto,
			Model model) {
		try {
			log.info("orderNo={}", "name={}", "phoneNumber={}", ordersGuestDetailDto.getOrderNo(),
					 ordersGuestDetailDto.getPhoneNumber());
			log.info("ordersGuestDetailDto={}", ordersGuestDetailDto);
			System.out.println("@@@@@@getOrderNo: " + ordersGuestDetailDto.getOrderNo());
	

				if (ordersGuestDetailDto.getPhoneNumber()
						.equals(orderDao.findById(ordersGuestDetailDto.getOrderNo()).getMember().getPhoneNo())) {

					List<OrdersDto> ordersDtoList = orderService.guestOrderList(ordersGuestDetailDto.getOrderNo(),
							ordersGuestDetailDto.getPhoneNumber());

					System.out.println("@@@@@@@@@@@@@@@@ordersDtoList: " + ordersDtoList);
					System.out.println(
							"@@@@@@@@@@@@@@@@ordersItemDtoList: " + ordersDtoList.get(0).getOrderItemDtoList());
					List<OrderItemDto> orderItemDtoList = ordersDtoList.get(0).getOrderItemDtoList();
					model.addAttribute("ordersDtoList", ordersDtoList);
					model.addAttribute("orderItemDtoList", orderItemDtoList);
					return "orders/order_guest_detail";
				} else {
					System.out.println("일치하지않는 주문번호 또는 전화번호입니다.");
		            model.addAttribute("notSame", "일치하지 않는 주문번호 또는 전화번호입니다.");
		            return "orders/find_order_guest";
				}
			

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", e.getMessage());
			return "orders/find_order_guest";
		}
	}

	static Integer gradePoint(String value) {
		String grade = value;
		Integer gradePoint = 0;
		switch (grade) {
		case "Rookie": {
			gradePoint = 1;
			break;
		}
		case "Bronze": {
			gradePoint = 4;
			break;
		}
		case "Silver": {
			gradePoint = 7;
			break;
		}
		case "Gold": {
			gradePoint = 10;
			break;
		}
		case "Platinum": {
			gradePoint = 13;
			break;
		}
		case "Diamond": {
			gradePoint = 16;
			break;
		}
		}
		return gradePoint;
	}

}