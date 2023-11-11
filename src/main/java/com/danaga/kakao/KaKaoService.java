package com.danaga.kakao;

import java.io.IOException;
import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KaKaoService {
	/***************************************************
	 * 인가코드로 토큰받기
	 *****************************************************/
	@Value("${api.kakao.rest_api_key}")
	private String kakaoRestApiKey;

	@Value("${api.kakao.javascript_key}")
	private String kakaoJavascriptApiKey;
	
	@Value("${api.kakao.redirect_url}")
	private String redirect_url;
	
	public OAuthToken getToken(String code) {
		String kakaoAuthUrl = "https://kauth.kakao.com";
	    String reqUrl = "/oauth/token";
	    URI uri = URI.create(kakaoAuthUrl + reqUrl);
		RestTemplate rt = new RestTemplate();
		// HttpHeaders 오브젝트 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/x-www-form-urlencoded");
		// HttpBody 오브젝트 생성
		MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
	    parameters.set("grant_type", "authorization_code");
	    parameters.set("client_id", kakaoRestApiKey);
	    parameters.set("redirect_uri", redirect_url);
	    parameters.set("code", code);
	    // HttpHeader 와 HttpBody를 하나의 오브젝트에 담기
	    HttpEntity<MultiValueMap<String, Object>> kakaoTokenRequest = new HttpEntity<>(parameters, headers);
	    //Http 요청하기 - Post방식으로 - 그리고 response변수의 응답 받음.
	    ResponseEntity<String> response = 
	    		rt.exchange(uri,HttpMethod.POST,kakaoTokenRequest,String.class);
	    
	    // Gson, Json Simple, ObjectMapper
	    ObjectMapper objectMapper = new ObjectMapper();
	    OAuthToken oauthToken = null;
	    try {
			oauthToken = objectMapper.readValue(response.getBody(),OAuthToken.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	    System.out.println("카카오 액세스 토큰"+oauthToken.getAccess_token());
	    
	    return oauthToken;
	}

	/*********************************************
	 * 카카오유저정보얻기
	 *********************************************/
	public KakaoProfile getKakaoProfile(OAuthToken oauthToken) {
		String kakaoApiUrl = "https://kapi.kakao.com";
		String reqUrl = "/v2/user/me";
	    URI uri = URI.create(kakaoApiUrl + reqUrl);
		RestTemplate rt = new RestTemplate();
		// HttpHeaders 오브젝트 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer "+oauthToken.getAccess_token());
		headers.add("Content-type", "application/x-www-form-urlencoded");
	    // HttpHeader 와 HttpBody를 하나의 오브젝트에 담기
	    HttpEntity<MultiValueMap<String, Object>> kakaoProfileRequest = new HttpEntity<>(headers);
	    //Http 요청하기 - Post방식으로 - 그리고 response변수의 응답 받음.
	    ResponseEntity<String> response = 
	    		rt.exchange(uri,HttpMethod.POST,kakaoProfileRequest,String.class);
	    // Member Object
	    ObjectMapper objectMapper = new ObjectMapper();
	    KakaoProfile kakaoProfile = null;
	    try {
	    	kakaoProfile = objectMapper.readValue(response.getBody(),KakaoProfile.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	    return kakaoProfile;
	}
}
