package com.kh.myweb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.kh.myweb.common.interceptor.LoginInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
	// > 인터셉터 형식의 클래스를 빈으로 등록하려면
	//   반드시 WebMvcConfigurer 라는 인터페이스를 상속받아서 구현해야한다!!
	
	@Autowired
	private LoginInterceptor loginInterceptor;
	
	// WebMvcConfigurer 인터페이스에서 제공하는 추상메소드들 중에서
	// 인터페이스 등록용 메소드인 addInterceptors 라는 메소드를 오버라이딩
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		// * 매개변수 
		// - registry : 내가 가져다 쓸 인터셉터 객체들을 차곡차곡 보관할 객체
		//				인터셉터 객체들을 담는 List 와 같은 존재
		
		// LoginInterceptor 객체 정보 추가
		registry.addInterceptor(loginInterceptor)
				.addPathPatterns("/member/myPage")
				.addPathPatterns("/notice/enrollForm")
				.addPathPatterns("/board/enrollForm");
		// ...
		
		// * 참고
		// > 만약 로그인한 사용자들의 계정에 따라서도 접속 유무를 판별하고 싶다면
		//   AdminCheckInterceptor 클래스를 하나 더 구현 후 (com.kh.myweb.common.interceptor)
		//   preHandle 에서 session 얻어내고, loginUser 의 직급 체크 후 (ERP)
		//   직급에 따라 접속할 수 있는 url 을 여기서 가르면 됨!!
		
		// registry.addInterceptor(AdminCheckInterceptor객체);
		// registry.addInterceptor(XxxCheckInterceptor객체);
		// ...
		
	}
	
}





