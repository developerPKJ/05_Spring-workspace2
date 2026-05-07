package com.kh.myweb.notice.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*
    VO 클래스 코드를 자동완성 해주는 라이브러리 : Lombok

    설치 방법
    1) Lombok 라이브러리 추가
        - pom.xml에 Lombok 라이브러리 추가
    2) 라이브러리에 있는 Lombok.jar를 run as java application으로 실행해 설치
    3) IDE 재실행

    사용 방법
    1) 필드부는 직접 작성
    2) 필드부를 작성한 후 롬복에서 제공하는 어노테이션들을 이용해 코드 자동완성
    3) 롬복에서 제공하는 어노테이션들은 클래스 선언부 상단에 작성
    
    주의 사항
    - 협업 시 롬복을 사용하고 싶으면 모든 개발자가 롬복을 사용해야 한다.
    > 실제로 코드를 자동완성 해주는 것이 아닌 코드가 있다고 IDE가 인식하게 해주는 라이브러리이기 때문
    - 필드명 작성 시 앞글자가 외자인 필드명은 만들면 안됨
    > 롬복이 getter/setter 메소드를 자동으로 만들어주는데, 이때 필드명 첫글자를 대문자로 바꿈
      -> 다른 프레임워크에서 getter/setter 메소드를 인식하지 못할 수 있음
    - @Data 어노테이션 사용하지 않기
    > 모든 코드를 알아서 다 자동완성 해주는 어노테이션이라 필요없는 코드까지 만들어 충돌 및 오류 발생 가능성 있음
*/

@NoArgsConstructor	// 기본 생성자 자동 생성
@AllArgsConstructor	// 모든 필드를 매개변수로 하는 생성자 자동 생성
@Setter				// setter 메소드 자동 생성
@Getter				// getter 메소드 자동 생성
@ToString			// toString 메소드 자동 생성
public class Notice {
    // field
    // NOTICE_NO	NUMBER
    // NOTICE_TITLE	VARCHAR2(100 BYTE)
    // NOTICE_CONTENT	VARCHAR2(4000 BYTE)
    // NOTICE_WRITER	NUMBER
    // COUNT	NUMBER
    // CREATE_DATE	DATE
    // STATUS	VARCHAR2(1 BYTE)
    private int noticeNo;
    private String noticeTitle;
    private String noticeContent;
    private String noticeWriter;  // 관리자 : admin(조회 시 아이디로), 일반 회원 : MEMBER_NO(조회 시 회원 번호로)
    private int count;
    private Date createDate;
    private String status;
}
