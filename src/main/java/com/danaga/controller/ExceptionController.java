package com.danaga.controller;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.danaga.dto.ResponseDto;
import com.danaga.exception.product.FoundNoObjectException;
import com.danaga.exception.product.NeedLoginException;
import com.danaga.exception.product.ProductExceptionMsg;
import com.danaga.exception.product.FoundNoObjectException.FoundNoMemberException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoOptionSetException;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class ExceptionController {
	@ExceptionHandler(value = {FoundNoObjectException.FoundNoOptionSetException.class,NeedLoginException.class
			,FoundNoMemberException.class,NoSuchElementException.class})
	protected String defaultException(Exception e, HttpSession session) {
		    if (e instanceof FoundNoOptionSetException) {
		    	return "redirect:404"; //없는 상품 조회
			} else if (e instanceof NoSuchElementException) {
				return "redirect:404";
			} else if (e instanceof NeedLoginException) {
				return "redirect:member_login_form";
			} else if (e instanceof FoundNoMemberException) {
				return "redirect:member_login_form";
			}
		    return "redirect:404"; //없는 상품 조회
		}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus
	protected String exception (Exception e) {
		return "redirect:404";
	}
	
}
