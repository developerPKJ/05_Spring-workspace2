package com.kh.email.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EmailController {
	
	// 필드부 (전역변수)
	// > 인증번호를 담아둘 해시맵 생성
	//   key : 인증할 email 주소, value : 인증번호
	// > 단, 동기화 문제 때문에 다음과 같이 생성해야 한다.
	//   (동시에 여러 사람이 인증 요청을 보낼 경우 인증정보를 동시에 put 할 수 있도록)
	private Map<String, String> certNoList = Collections.synchronizedMap(new HashMap<>());

	// 만약, 인증번호 발급 후 3분 이내로만 인증 가능하게끔 만료시간을 설정하고 싶다면?
	// > 이메일과, 발급된 인증번호를 DB 테이블에 저장하면 됨!!
	//   CERT 테이블에 EMAIL 컬럼, CERT_NO 컬럼, CREATE_DATE 컬럼 (발급시간 SYSDATE)
	// > 발급시간과 상관 없이 실제 프로젝트 구현 시에는 반드시 테이블로 쓸 것!!
	//   (synchronizedMap 은 동시성을 갖는 대신 속도가 엄청 느림)
	
	@Autowired
	private JavaMailSender mailSender;
	
	@ResponseBody
	@PostMapping("send")
	public String sendCertNo(String email) {
		
		// System.out.println(email);
		// > 받는사람으로 셋팅해줄 예정!!
		
		// 6자리의 랜덤 1회성 인증번호 발급 (100000 ~ 999999)
		// > OTP : One Time Password
		int random = (int)(Math.random() * 900000 + 100000);
		
		// 위의 OTP 를 email 로 전송하기
		// > 단, 그냥 넘기는게 아니라 이따 대조를 위해 어딘가에 OTP 를 저장도 해둬야함!!
		//   Controller 의 전역변수로 OTP 를 저장할 수 있는 Map 을 정의한 뒤 put
		certNoList.put(email, String.valueOf(random));
		
		// > CERT 테이블에 INSERT (EMAIL, CERT_NO, SYSDATE)
		
		// System.out.println(certNoList);
		
		// SimpleMailMessage 로 전송해보기
		SimpleMailMessage message = new SimpleMailMessage();
		
		// 메세지 정보 담기 : 제목, 내용, 받는사람
		message.setSubject("[KH정보교육원] 이메일 인증 번호입니다");
		message.setText("인증번호 : " + random);
		message.setTo(email);
		
		mailSender.send(message);
		
		return "인증번호 전송이 완료되었습니다.";
		
	}
	
	@ResponseBody
	@PostMapping("validate")
	public String validateCertNo(String email, String checkNo) {
		
		// System.out.println(email);
		// System.out.println(checkNo);
		
		String result = "";
		
		// email 과 checkNo 세트가 certNoList 에 있는지 대조 후 결과에 따른 응답데이터 넘기기
		if((certNoList.get(email) != null) && (certNoList.get(email).equals(checkNo))) {
			// > 인증번호 발급 정보가 있다면
			
			// > CERT 테이블로부터 SELECT 
			//   SELECT * FROM CERT 
			//   WHERE EMAIL 일치, CERT_NO 일치, SYSDATE <= CREATE_DATE + 3분
			// > 3분 이내라면 한개의 행이 조회, 3분 이후라면 EMAIL, CERT_NO 이 일치해도 NULL 조회
			
			result = "success";
			
		} else {
			
			result = "fail";
		}

		// 1회성인 만큼 인증이 성공하든 실패하든 간에 무조건 발급 정보를 삭제해줄 것!!
		certNoList.remove(email);
		
		// > CERT 테이블로부터 DELETE (1회성)
		
		return result;
	}
	
}






