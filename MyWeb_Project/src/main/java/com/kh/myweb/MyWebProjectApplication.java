package com.kh.myweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;

/*
 * * 이 프로젝트에 스프링 시큐리티 모듈을 연동하는 순간
 *   기본적으로 까다롭게 웹 보안 설정이 잡히면서 메인페이지에 접속이 안됨!! (계속 이상한 로그인 페이지로 리다이렉트됨)
 * 
 * 해결방법)
 * @SpringBootApplication 어노테이션에
 * 까다로운 웹 보안 설정을 제외하고 이 프로젝트를 실행시키겠다. 라고 설정을 잡아줘야함!!
 * 이것은 스프링 부트 3.x.x 버전대에서 쓸 수 있는 방식!!
 * (우리는 4 점대 버전을 썼기 때문에 설정 수정했음)
 * 
 * 단, Spring Security 모듈에서 제공하는 클래스를 가져다 쓰겠다고 Bean 등록까지 이뤄져야 이용 가능함!!
 */
@SpringBootApplication // (exclude={SecurityAutoConfiguration.class})
// > SecurityAutoConfiguration 클래스 내의 까다로운 웹 보안 설정 코드를 제외하고 실행시키겠다.
public class MyWebProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyWebProjectApplication.class, args);
	}

}
