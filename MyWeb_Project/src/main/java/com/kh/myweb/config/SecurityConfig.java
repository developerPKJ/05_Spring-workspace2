package com.kh.myweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/*
 * * Bean
 * - 스프링에서 쓰이는 자바 객체를 Bean 이라고 부른다.
 * - 클래스 별로 Bean 으로 등록하면 내가 필요할 때 마다 
 *   알아서 (@Autowired) 그 객체를 스프링이 생성해서 주입해줌!! (DI)
 * 
 * * 스프링에서의 Bean 등록 방법
 * 1. 클래스 선언부 상단에 @Component 어노테이션을 붙여 등록하기
 *    (상황에 따라 @Controller, @Service, @Repository 으로 대체될 수 있음) 
 *    단, 내가 직접 만든 코드에만 적용 가능한 방법임!!
 * 2. xml 파일 형식으로 등록하기
 * 	  길고 복잡하기 때문에 Spring Boot 에서는 안쓰임 (Spring Legacy 방식)
 * 3. 자바 코드 형식으로 등록하기
 * 	  Bean 등록용 클래스를 만들고, 클래스 선언부 상단에 @Configuration 어노테이션을 작성
 * 	  그 안에 메소드 형식으로 Bean 등록 구문을 작성한다. (단, 메소드 상단에도 @Bean 어노테이션 작성)
 * 	  xml 방식과 자바 코드 방식은 남이 만들어준 코드에 적용 가능한 방법임!!
 */

@Configuration // 이 클래스는 빈 등록용 (환경설정용) 클래스임을 알려줌
public class SecurityConfig {
	
	// 스프링 시큐리티 모듈에서 제공하는 BCryptPasswordEncoder 클래스를 빈으로 등록 (자바 메소드 형식)
	@Bean // Bean 등록용 메소드임을 알려줌
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	// > 리턴 타입은 해당 클래스 타입으로,
	//   메소드명이 곧 빈 (객체) 의 이름이 된다!!
	// > 메소드 형식으로 빈을 등록하면 이 메소드를 스프링이 내부적으로 호출해서 객체를 생성해 뒀다가
	//   해당 객체가 필요할 때 마다 (@Autowired) 나에게 주입해줌!!
	
	// 원래 여기까지 하면 스프링 부트 3.x.x 버전대에서는 충분히 돌아감!!
	// > 부트 4 버전대 부터는 추가적인 설정을 해줘야함!!
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		return http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
				   .csrf(csrf -> csrf.disable())
				   .build();
	}
	
}





