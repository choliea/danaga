package com.danaga.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.danaga.service.MailService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;
    
//    @GetMapping("/mailpage")
//    public String MailPage(){
//        return "Mail";
//    }
    @ResponseBody
    @PostMapping("/emailauthentication")
    public String joinMailSend(String mail){

       int number = mailService.joinSendMail(mail);

       String num = "" + number;

       return num;
    }
    @ResponseBody
    @PostMapping("/findpassemailauthentication")
    public Map findPassMailSend(String mail) throws Exception{
    	
    	String randomPass = null;
		randomPass = mailService.findPassSendMail(mail);
		HashMap map=new HashMap<>();
    	map.put("newPass", randomPass);
    	
    	return map;
    }
    @ResponseBody
    @PostMapping("/findidemailauthentication")
    public Map findidMailSend(String mail) throws Exception{
    	
    	String id = null;
		id = mailService.findIdSendMail(mail);
    	HashMap map=new HashMap<>();
    	map.put("sendId", id);
    	return map;
    }
    @ResponseBody
    @PostMapping("/sendOrderIdemailauthentication")
    public Map sendOrderIdMailSend(String mail) throws Exception{
    	
    	String id = null;
    	id = mailService.findOrderIdSendMail(mail);
    	HashMap map=new HashMap<>();
    	map.put("orderId", id);
    	return map;
    }

}