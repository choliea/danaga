package com.danaga.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.danaga.dto.ResponseDto;
import com.danaga.exception.product.FoundNoObjectException;
import com.danaga.exception.product.NeedLoginException;
import com.danaga.exception.product.ProductExceptionMsg;
import com.danaga.exception.product.FoundNoObjectException.FoundNoMemberException;
import com.danaga.exception.product.FoundNoObjectException.FoundNoOptionSetException;
@RestControllerAdvice
public class ExceptionRestController {
	@ResponseBody
	@ExceptionHandler(value = {NeedLoginException.class,FoundNoOptionSetException.class,FoundNoMemberException.class,MethodArgumentNotValidException.class})
	protected ResponseEntity<?> defaultRestException(Exception e) {
		 ProductExceptionMsg errorMsg=null;
		    if (e instanceof NeedLoginException) {
		        errorMsg = ((NeedLoginException) e).getMsg();
		        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDto.builder().msg(errorMsg).build());
		    } else if (e instanceof FoundNoObjectException.FoundNoMemberException) {
		        errorMsg = ((FoundNoObjectException.FoundNoMemberException) e).getMsg();
		        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseDto.builder().msg(errorMsg).build());
		    }  else if (e instanceof FoundNoOptionSetException) {
		        errorMsg = ((FoundNoOptionSetException) e).getMsg();
		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseDto.builder().msg(errorMsg).build());
		    } else if (e instanceof MethodArgumentNotValidException) {
		        errorMsg = ProductExceptionMsg.WRONG_PARAMETER;
		    } 
		    return ResponseEntity.badRequest().body(ResponseDto.builder().msg(errorMsg).build());
		}

}
