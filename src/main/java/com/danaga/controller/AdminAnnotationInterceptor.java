package com.danaga.controller;



import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.danaga.exception.UnauthorizedUserException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/*
①HandlerInterceptor 인터페이스
②HandlerInterceptorAdapter 추상클래스

	- public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler)
     	Controller 요청 전 실행
	
	- public boolean postHandle(HttpServletRequest request, HttpServletResponse response,Object handler, ModelAndVeiw modelAndVeiw)
     	Controller 처리가 끝나고 화면에 띄어주는 처리 직전에 수행
	
	- afterCompletion() : 모든 처리가 끝난 후 호출
 */

public class AdminAnnotationInterceptor implements HandlerInterceptor {
	public AdminAnnotationInterceptor() {
		//System.out.println("### AdminAnnotationInterceptor()생성자");
	}

	// preHandle() : 컨트롤러보다 먼저 수행되는 메서드
	@Override
	public boolean preHandle(HttpServletRequest request, 
			HttpServletResponse response, 
			Object handler)
			throws Exception {
		//System.out.println("### AdminAnnotationInterceptor.preHandle()메써드");
		/*
		[ 핸들러 메소드(HandlerMethod)란? ]
			HandlerMethod는 @RequestMapping이 붙은 메소드의 정보를 추상화한 객체이다. 
			HandlerMethod는 그 자체가 실행가능한 객체가 아니라 메소드를 실행하기 위해 필요한 
			참조정보를 담고 있는 객체 로써 다음과 같은 정보들을 가지고 있다.
				- @Controller가붙은 컨트롤러 빈정보
				- @RequestMapping[@GetMapping,@PostMapping] 이 붙은 메소드 메타정보
				- @RequestMapping[@GetMapping,@PostMapping] 이 붙은 메소드 파라미터 메타정보
				- @RequestMapping[@GetMapping,@PostMapping] 이 붙은 메소드 어노테이션 메타정보
				- @RequestMapping[@GetMapping,@PostMapping] 이 붙은 메소드 리턴 값 메타정보
 
			디스패처 서블릿은 애플리케이션이 실행될 때 모든 컨트롤러 빈의 메소드를 살펴서 
			매핑 후보가 되는 메소드들을 추출한 뒤, 이를 HandlerMethod 형태로 저장해둔다. 
			그리고 실제 요청이 들어오면 저장해 둔 목록에서 요청 조건에 맞는 
			HandlerMethod를 참조해서 매핑되는 메소드를 실행한다.
		 */
		/***********************************************************************************
		1. handler객체 종류 확인
		    우리가 관심 있는 것은 @Controller객체에 있는 매핑된(@RequestMapping이 붙은) 메서드이므로 
		    HandlerMethod 타입인지 체크
		*************************************************************************************/   
		if (handler instanceof HandlerMethod == false) {
			/*
			 * @ Controller객체에 @RequestMapping이 붙은메쏘드 :  HandlerMethod
			 */
			//return true이면 그대로 컨트롤러로 진행
			return true;
		}
		
		/*********************************
		 2.HandlerMethod 타입으로 형 변환
		 ********************************/
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		
		/***************************
		 3.HandlerMethod객체 로부터 @LoginCheck 어노테이션 객체얻기
		***************************/
		AdminCheck adminCheck = handlerMethod.getMethodAnnotation(AdminCheck.class);
		/***************************
		4. HandlerMethod객체에 @LoginCheck어노테이션 이없는 경우, 
		   즉 인증이 필요 없는 요청
		***************************/
		if (adminCheck == null) {
			//System.out.println("### AdminAnnotationInterceptor.preHandle()메써드 @AdminCheck 없는경우");
			return true;
		}
		/***************************
		4. HandlerMethod객체에 @LoginCheck어노테이션 이있는 경우, 
		   세션이 있는지 체크
		***************************/
		System.out.println("### AdminAnnotationInterceptor.preHandle()메써드 @AdminCheck 있는경우");
		//session 객체를 가져옴
		HttpSession session = request.getSession();
		//login처리를 담당하는 사용자 정보를 담고 있는 객체를 가져옴
		String role = (String) session.getAttribute("role");
	
		
		if (role == null) {
			// 로그인이 안되어 있는 상태임으로 로그인 폼으로 다시 돌려보냄(redirect)
			response.sendRedirect("404");
			return false;
			// 더이상 컨트롤러 요청으로 가지 않도록 false로 반환함
		}
		// preHandle의 return은 컨트롤러 요청 uri로 가도 되냐 안되냐를 허가하는 의미임
		// 따라서 true로하면 컨트롤러 uri로 가게 됨.
		return true;
	}

	
	

	
}