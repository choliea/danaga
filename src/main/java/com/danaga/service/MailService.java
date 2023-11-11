package com.danaga.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.danaga.dao.OrderDao;
import com.danaga.dto.MemberResponseDto;
import com.danaga.dto.MemberUpdateDto;
import com.danaga.entity.Member;
import com.danaga.entity.Orders;
import com.danaga.generator.RandomStringGenerator;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final MemberService memberService;
    //private static final String senderEmail= "danaga@gmail.com";
    private static int number;
    private static String randomString;
    private final OrderDao orderDao;

    public static void createNumber(){
       number = (int)(Math.random() * (90000)) + 100000;// (int) Math.random() * (최댓값-최소값+1) + 최소값
    }
    public static void createRandomPass() throws NoSuchAlgorithmException{
    	randomString = RandomStringGenerator.generateRandomString();
    }

    public MimeMessage joinCreateMail(String mail){
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();
        
        try {
            //message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("다나가 이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + number + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }
    public MimeMessage orderCreateMail(String mail)throws Exception{
    	MimeMessage message = javaMailSender.createMimeMessage();
    	List<Orders> orderList= orderDao.findOrdersByMember_Email(mail);
    	String orderId= Long.toString(orderList.get(orderList.size()-1).getId());
    	try {
    		//message.setFrom(new InternetAddress(senderEmail));
    		message.setRecipients(MimeMessage.RecipientType.TO, mail);
    		message.setSubject("다나가 이메일 인증");
    		String body = "";
    		body += "<h3>" + "주문하신 번호는" + "</h3>";
    		body += "<h1>" + orderId + "</h1>";
    		body += "<h3>" + "입니다 감사합니다." + "</h3>";
    		message.setText(body,"UTF-8", "html");
    	} catch (MessagingException e) {
    		e.printStackTrace();
    	}
    	return message;
    }
    public int joinSendMail(String mail){

        MimeMessage message = joinCreateMail(mail);

        javaMailSender.send(message);

        return number;
    }
    public MimeMessage findPassCreateMail(String mail) throws Exception{
    	createRandomPass();
    	MimeMessage message = javaMailSender.createMimeMessage();
    	
    	try {
    		MemberResponseDto dto = memberService.getMemberBy(mail);
    		dto.setPassword(randomString);
    		memberService.updateMember(MemberUpdateDto.toDto(Member.toResponseEntity(dto)));
    		//message.setFrom(new InternetAddress(senderEmail));
    		message.setRecipients(MimeMessage.RecipientType.TO, mail);
    		message.setSubject("다나가 임시 비밀번호 발급");
    		String body = "";
    		body += "<h3>" + "요청하신 임시 비밀번호입니다." + "</h3>";
    		body += "<h1>" + randomString + "</h1>";
    		body += "<h3>" + "감사합니다." + "</h3>";
    		message.setText(body,"UTF-8", "html");
    	} catch (MessagingException e) {
    		e.printStackTrace();
    	}
    	return message;
    }

    
    public String findPassSendMail(String mail) throws Exception{
    	
    	MimeMessage message = findPassCreateMail(mail);
    	
    	javaMailSender.send(message);
    	
    	return randomString;
    }
    public MimeMessage findIdCreateMail(String mail) throws Exception{
    	createNumber();
    	MimeMessage message = javaMailSender.createMimeMessage();
    	
    	try {
    		//message.setFrom(new InternetAddress(senderEmail));
    		message.setRecipients(MimeMessage.RecipientType.TO, mail);
    		message.setSubject("다나가 아이디 찾기");
    		String body = "";
    		body += "<h3>" + "요청하신 아이디 입니다." + "</h3>";
    		body += "<h1>" + memberService.getMemberBy(mail).getUserName() + "</h1>";
    		body += "<h3>" + "감사합니다." + "</h3>";
    		message.setText(body,"UTF-8", "html");
    	} catch (MessagingException e) {
    		e.printStackTrace();
    	}
    	return message;
    }
    
    public String findIdSendMail(String mail) throws Exception{
    	
    	MimeMessage message = findIdCreateMail(mail);
    	
    	javaMailSender.send(message);
    	
    	return memberService.getMemberBy(mail).getUserName();
    }
    public String findOrderIdSendMail(String mail) throws Exception{
    	
    	MimeMessage message = orderCreateMail(mail);
    	
    	javaMailSender.send(message);
    	List<Orders> orderList= orderDao.findOrdersByMember_Email(mail);
    	String orderId= Long.toString(orderList.get(orderList.size()-1).getId());
    	return orderId;
    }
}