package com.kh.email;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

/*
 * * 애플리케이션 테스트
 * - 소프트웨어가 제대로 동작하는지 미리 검증하는 과정
 * - 개발 단계 별 크게 네가지 종류가 있다.
 * 
 * * 테스트 종류
 * 1. 단위테스트
 * 개발하는 과정에서 이루어지는 "메소드 단위" 의 코드가 잘 동작하는지를 테스트 하는 개념
 * 예) MemberController 클래스의 loginMember 메소드 하나가 잘 동작하냐
 * 
 * 2. 통합테스트
 * 모든 코드들을 취합 후 기능 별로 잘 그 기능이 제대로 잘 작동하는지 테스트 하는 개념
 * 예) MemberController 의 loginMember, MemberService 의 loginMember, 
 *    MemberDao 의 loginMember 의 코드를 다 이어붙인 로그인 "기능" 하나가 제대로 작동하는지 
 * 
 * 3. 시스템테스트
 * 모든 코드들을 취합 후 "비기능적인 요소" 들이 제대로 잘 작동하는지 테스트 하는 개념
 * 예) 사용상의 편의, 보안적 측면, 고객의 요구사항 반영 여부 등
 * 
 * 4. 인수테스트
 * 실제 사용자의 입장에서 테스트를 수행하는 개념
 * 예) 테스트케이스들 (경우의 수들) 을 쭉 나열 후 검사하는 업무 - QA 업무
 * 
 * * TDD (Test Driven Development, 테스트 주도 개발)
 * - 본 기능 구현 전 메소드 단위로 테스트코드를 먼저 작성해보고 (단위테스트) 제대로 동작하면,
 *   실제 기능 구현에 해당 코드를 적용해 나가는 방식
 * - 자바에서는 대표적인 테스트용 프레임워크인 JUnit 을 사용한다. (기능 및 지원 프레임워크)
 *   단위테스트 용도이며, 스프링에 기본적으로 같이 연동되서 프로젝트가 생성된다.
 */

// Spring 에서는 기본적으로 단위테스트용으로 src/test/java 폴더를 만들어 준다.
// 그 안에 단위테스트용 패키지와 클래스도 미리 만들어 줌!! 

@SpringBootTest // 테스트용 클래스임을 알려주는 어노테이션
class EmailProjectApplicationTests {
	
	// 필드부
	// > 어느 방법이든 간에 메일을 전송하려면 JavaMailSender 객체가 필요하다!!
	//   DI (의존성 주입) 방식으로 객체를 얻어내기 - 빈으로 기본적으로 이미 등록되어있기 때문에 바로 주입만 받으면 된다.
	@Autowired
	private JavaMailSender mailSender;

	@Test // 테스트용 메소드임을 알려주는 어노테이션
	void contextLoads() throws MessagingException {
		
		// System.out.println("잘 실행되나?");
		// > 테스트할 코드를 작성하고, Ctrl + F11 을 누르면 바로 이 메소드가 실행됨!! (main 메소드처럼 바로 실행시켜보겠다)
		
		// * 이메일 전송 테스트 코드 작성 (TDD)
		// > 이미 application.properties 에 이메일 관련 설정을 완료했음!!
		
		/*
		// 1. 단순 텍스트만 이메일로 보내는 경우
		// > SimpleMailMessage 객체를 이용하는 방법
		//   SimpleMailMessage 객체에 보낼 메일에 대한 정보를 담아주고,
		//   최종적으로 JavaMailSender 객체를 통해 해당 메일을 전송
		
		// SimpleMailMessage 
		// > 가장 기본적인 메세지 전송 기능을 지원함 (메세지 정보를 담는 용도)
		//   제목, 내용, 받는사람, 참조, 숨은참조
		SimpleMailMessage message = new SimpleMailMessage();
		
		// 제목
		message.setSubject("테스트 메일의 제목입니다.");
		
		// 내용
		message.setText("테스트 메일의 내용이 어쩌고 저쩌고..쭈절쭈절..");
		
		// 받는사람
		// 받는사람은 한명일 수도 있고, 여러명이 될 수도 있음!!
		
		// message.setTo("ua002@naver.com");
		// > 한명일 경우
		
		String[] to = {"ua002@naver.com", "masssal0501@gmail.com", "kgmlove230@gmail.com"};
		message.setTo(to);
		// > 여러명일 경우 (오버로딩된 형태 - 문자열 배열로 셋팅)
		
		// 참조
		// 참조 또한 한명일 수도 있고, 여러명이 될 수도 있음!!
		
		String[] cc = {"serominsockji9n9@gmail.com", "unk21059@naver.com"};
		message.setCc(cc);
		// > 여러명일 경우 (setTo 와 동일하게 오버로딩됨)
		
		// 숨은참조
		// 숨은참조 또한 한명일 수도 있고, 여러명이 될 수도 있음!!
		
		String[] bcc = {"hin2271@naver.com", "wbgn4033@gmail.com"};
		message.setBcc(bcc);
		// > 여러명일 경우 (setTo 와 동일하게 오버로딩됨)
		
		// 최종적으로 JavaMailSender 객체를 이용해서 메세지 전송
		mailSender.send(message);
		*/
		
		// 2. 이메일 내용 내부에 html 코드를 삽입해서 보내는 경우
		// > MimeMessage 객체를 이용하는 방법
		//   MimeMessage 관련 객체에 보낼 메일에 대한 정보를 담아주고,
		//   최종적으로 JavaMailSender 객체를 통해 해당 메일을 전송
		
		// MimeMessage
		// > 기존의 SimpleMailMessage 에 + a 기능을 제공
		//   추가적으로 첨부파일, html 템플릿 전송 기능을 포함함
		MimeMessage message = mailSender.createMimeMessage();
		// > 생성자가 아닌 JavaMailSender 객체로부터 얻어내야함!!
		
		// MimeMessage 객체에 곧바로 메일에 대한 정보를 담는게 아니라
		// Helper 역할을 해주는 객체를 또 만들어서 거기에 메일 정보를 담아줘야 한다!!
		// (MimeMessage 는 구조가 복잡하기 때문)
		// > MimeMessageHelper 객체 생성
		MimeMessageHelper mimeMessageHelper 
					= new MimeMessageHelper(message, true, "UTF-8");
									// MimeMessage 객체, 첨부파일여부, 인코딩값
		
		// MimeMessageHelper 객체에 정보 설정
		
		// 제목
		mimeMessageHelper.setSubject("마임메세지 테스트 메일의 제목입니다.");
		
		// 내용
		// > html 템플릿을 적용한 내용을 보낼 수 있다!!
		String content = """
				<html>
					<head></head>
					<body>
						<div align='center'>
							<h1>마임메세지 테스트 메일입니다</h1>
							
							<br>
							<hr>
							
							<div style='border : 1px solid gray;'>
								<h3>잘 되나..?</h3>
								
								<br>
								
								<p style='color : red;'>
									이 메일을 받는 순간 다른이에게..어쩌고..행운의 편지 어쩌고저쩌고..
								</p>
							</div>
							
						</div>
					</body>
				</html>
				""";
		
		mimeMessageHelper.setText(content, true);
		// > true 일 경우 html 태그가 해석되서 보여지겠다.
		//   false 일 경우 html 태그가 그냥 텍스트로 보여지겠다. (SimpleMailMessage 와 동일)
		
		// 받는사람
		String[] to = {"ghkim1121@naver.com", "ajrqh1022430@gmail.com", "ghdwn941108@gmail.com"};
		mimeMessageHelper.setTo(to);
		
		// 참조
		String[] cc = {"ansunghn@gmail.com", "ljh030505@naver.com"};
		mimeMessageHelper.setCc(cc);
		
		// 숨은참조
		String[] bcc = {"jihyelim331@gmail.com", "rltjd3064@gmail.com"};
		mimeMessageHelper.setBcc(bcc);
		// > 전반적인 사용법이 기존의 SimpleMailMessage 와 비슷 (setText 만 조금 다름)
		
		// MimeMessage 는 "첨부파일" 도 보낼 수 있다!! (옵션)
		DataSource dataSource 
			= new FileDataSource("C:/04_Frontend-workspace/1_html-workspace/resources/image/gifTest.gif");
		// > 첨부파일의 경로를 정확히 작성해야함!!
		
		mimeMessageHelper.addAttachment(dataSource.getName(), dataSource);
		// > 만약 첨부파일을 여러개 보내고 싶다면
		//   첨부파일의 갯수만큼 DataSource 객체 생성 후 addAttachment 해주면 됨!!
		// > 단, 내가 이용하는 메일 서비스에서 제공하는 파일 업로드 정책에 맞추기
		//   (최대 갯수, 용량, 확장자 종류 등)
		
		// 최종적으로 JavaMailSender 를 이용해서 메세지 전송
		mailSender.send(message);
	}

}



