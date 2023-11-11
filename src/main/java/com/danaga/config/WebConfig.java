package com.danaga.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.danaga.controller.AdminAnnotationInterceptor;
import com.danaga.controller.AuthLoginAnnotationInterceptor;


@Configuration
public class WebConfig implements WebMvcConfigurer{
	/*
	 * 인터셉터등록
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		AuthLoginAnnotationInterceptor authLoginAnnotationInterceptor=
				new AuthLoginAnnotationInterceptor();
		registry.addInterceptor(authLoginAnnotationInterceptor)
		.addPathPatterns("/**")
		.excludePathPatterns("/css/**")
		.excludePathPatterns("/js/**")
		.excludePathPatterns("/image/**");
		
		AdminAnnotationInterceptor adminAnnotationInterceptor =
				new AdminAnnotationInterceptor();
		registry.addInterceptor(adminAnnotationInterceptor)
		.addPathPatterns("/**")
		.excludePathPatterns("/css/**")
		.excludePathPatterns("/js/**")
		.excludePathPatterns("/image/**");
		
	}
	// 스프링 서버 전역적으로 CORS 설정
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
        	.allowedOrigins("http://localhost:5000") // 허용할 출처
            .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP method
            .allowCredentials(true) // 쿠키 인증 요청 허용
            .maxAge(3000); // 원하는 시간만큼 pre-flight 리퀘스트를 캐싱
    }
	
}
