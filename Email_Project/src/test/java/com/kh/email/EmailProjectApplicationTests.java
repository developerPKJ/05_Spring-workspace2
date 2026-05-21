package com.kh.email;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/*
	애플리케이션 테스트
	- 애플리케이션이 정상적으로 구동되는지 테스트하는 클래스

	종류
	- 단위 테스트
		: 개발하는 과정에서 메소드 단위 코드가 동작하는지 테스트
	- 통합 테스트
		: 모든 코드들을 취합 후 기능별로 동작하는지 테스트
	- 시스템 테스트
		: 모든 코드들을 취합 후 비기능적인 요소들도 문제없는지 테스트(성능, 보안 등)
	- 인수 테스트
		: 실제 사용자 입장에서 테스트

	TDD (Test Driven Development, 테스트 주도 개발)
	- 본 기능 구현 전 메소드 단위로 테스트코드를 먼저 작성해보고(단위테스트) 제대로 동작하면
	  실제 기능 구현에 해당 코드를 적용하는 방식
	- 자바에서는 대표적인 테스트용 프레임워크인 JUnit을 사용
	  단위테스트 용도이며, 스프링에 기본적으로 같이 연동되서 프로젝트가 생성됨
*/

// Spring에서는 기본적으로 단위테스트용 src/test/java 폴더 생성됨
// 단위테스트용 패키지와 클래스도 생성됨
@SpringBootTest // 테스트용 클래스 명시 어노테이션
class EmailProjectApplicationTests {
	// 필드부
	// - 어느 방법이든 메일을 전송하려면 JavaMailSender 객체가 필요함
	// - DI(Dependency Injection, 의존성 주입) 방식으로 객체를 얻어야함
	// > 빈으로 기본적으로 이미 등록되어 있어 주입 받으면 됨
	@Autowired
	private JavaMailSender mailSender;

	@Test // 테스트용 메소드 명시 어노테이션
	void contextLoads() {
		// System.out.println("테스트 코드 실행됨"); // ctrl + f11

		// 이메일 전송 테스트 코드 작성(TDD)
		// 이메일 관련 설정은 application.properties에 작성되어 있음

		// 1. 단순 텍스트만 이메일로 보내는 경우
		// SimpleMailMessage 객체 이용
		// SimpleMailMessage 객체에 메일에 대한 정보 담고
		// JavaMailSender 객체를 통해 메일 전송

		// SimpleMailMessage
		// - 가장 기본적인 메세지 전송 기능 지원 (메세지 정보 담는 용도)
		// > 제목, 내용, 받는사람, 참조, 숨은 참조
		SimpleMailMessage message = new SimpleMailMessage();

		// 제목
		message.setSubject("테스트 메일의 제목");

		// 내용
		message.setText("테스트 메일의 내용");

		// 받는사람
		// message.setTo("kjpark0102@naver.com"); // 한명인 경우
		String[] to = {"kjpark0102@naver.com", "siteblod@gmail.com"};
		message.setTo(to); // 여러명인 경우

		// 참조
		// message.setCc("cc@example.com");
		String[] cc = {"cc1@example.com", "cc2@example.com"};
		message.setCc(cc);

		// 숨은 참조
		// message.setBcc("bcc@example.com");
		String[] bcc = {"bcc1@example.com", "bcc2@example.com"};
		message.setBcc(bcc);

		mailSender.send(message);
	}

}
