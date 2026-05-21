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
    // field
    // - OTP 인증번호 발급 후 저장할 Map 객체
    // > key : 이메일 주소, value : OTP 인증번호
    // Map<String, String> certNoList = new HashMap<>();
    // 위와 같은 방법은 동기화 문제가 있음 - 동시에 여러 사람이 인증 요청 보낼 경우, 동시에 put 위험
    private Map<String, String> certNoList = Collections.synchronizedMap(new HashMap<>());

    // 인증 번호 만료시간 설정하려면
    // 이메일과 인증번호를 DB 테이블에 저장하면됨
    // email 컬럼, cert_no 컬럼, create_date 컬럼으로 관리
    // 위의 synchronizedMap 방식은 느려서 실제 서비스에서는 DB에 저장하는 방식으로 구현하는 편

    @Autowired
    private JavaMailSender mailSender;

    @ResponseBody
    @PostMapping("send")
    public String SendCertNo(String email) {
        // System.out.println("이메일 : " + email);
        // > 받는사람

        // 6자리 랜덤 1회성 OTP 인증번호 발급
        int random = (int)(Math.random() * 900000) + 100000;

        // 위 OTP를 email 전송 + 이 후 대조를 위해 Controller의 전역변수로 정의한 Map에 저장
        certNoList.put(email, String.valueOf(random));

        // System.out.println(certNoList);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("[KH정보교육원] 인증번호 발급 메일입니다.");
        message.setText("인증번호 : " + random);
        message.setTo(email);

        mailSender.send(message);

        return "인증번호가 발송 완료.";
    }
    
    @ResponseBody
    @PostMapping("validate")
    public String validateCheckNo(String email, String checkNo) {
        // System.out.println("이메일 : " + email + ", 인증번호 : " + checkNo);

        String result = "fail";

        if(certNoList.get(email) != null && certNoList.get(email).equals(checkNo)) {
            result = "success";
        }

        certNoList.remove(email); // 인증번호는 1회용이므로, 대조 후 Map에서 제거

        return result;
    }
    

}
