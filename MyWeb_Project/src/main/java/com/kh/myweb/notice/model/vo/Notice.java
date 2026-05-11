package com.kh.myweb.notice.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*
 * * 매 번 VO 클래스의 필드부, 생성자부, 메소드부를 직접 작성하기 너무 귀찮음!!
 * - 이클립스의 자동완성 기능이 있다 하더라도 귀찮기 때문에 VO 클래스 코드를 자동완성 해주는 라이브러리를 이용해보자!!
 * 
 * * Lombok (롬복)
 * - VO 클래스의 코드 등을 자동으로 생성해줄 수 있는 라이브러리
 * - 주로 반복되는 생성자, getter / setter, toString 등의 메소드 작성 구문을 줄여준다.
 * - 코드 다이어트 라이브러리
 * 
 * * Lombok 라이브러리 연동 방법
 * 1. 라이브러리 파일 (.jar) 다운로드 후 적용 - Maven 의 pom.xml 에서 처리
 * 2. 다운로드된 jar 파일을 찾아서 실행 후 설치 (Run As - Java Application)
 * 3. IDE 재실행
 * 
 * * Lombok 라이브러리 사용 방법 & 주의사항
 * - 필드부는 직접 작성할 것
 * - 필드부를 작성한 후 롬복에서 제공하는 어노테이션들을 이용해서 코드를 자동완성하는 원리
 * - 롬복에서 제공하는 어노테이션들은 클래스 선언부 상단에 작성한다!!
 * 
 * - 협업 시 롬복을 사용하고 싶다면 모든 팀원이 다 같이 사용해야함!!
 *   (롬복은 사실 엄밀히 말하면 코드를 자동완성 해주는 게 아니라 코드가 있다고 STS 와 같은 IDE 툴을 속이는 개념이라)
 * - 필드명 작성 시 앞글자가 외자인 필드명은 만들지 말 것!!
 *   uId (X) --> userId (O)
 *   nNo (X) --> noticeNo (O)
 *   uId 라는 필드의 setter 는 setUId 가 아니라 setuId, 
 *   			  getter 는 getUId 가 아니라 getuId 로 이름짓는게 명명규칙 상 맞음
 *   그런데 롬복은 setter, getter 명을 지을 때 setUId, getUId 로 이름지어준다!!
 *   후에 EL, 다른 프레임워크들이 내부적으로 setter, getter 메소드를 호출할 때
 *   명명규칙에 맞는 메소드를 찾지 못해 오류가 발생할 수 있다!!
 * - @Data 어노테이션은 사용하지 말 것
 *   모든 코드를 알아서 다 자동완성 해주는 어노테이션임!! 
 *   내가 필요없는 코드까지 다 만들어줘서 충돌 나서 오류가 발생하는 경우가 종종 있음
 */

@NoArgsConstructor // 기본생성자를 자동완성 해주는 어노테이션
@AllArgsConstructor // 모든 필드에 대한 매개변수 생성자를 자동완성 해주는 어노테이션
@Setter // setter 메소드들을 자동완성 해주는 어노테이션
@Getter // getter 메소드들을 자동완성 해주는 어노테이션
@ToString // toString 메소드를 오버라이딩 해주는 어노테이션
public class Notice {

	// 필드부
	private int noticeNo;		 	 //	NOTICE_NO	NUMBER
	private String noticeTitle;		 //	NOTICE_TITLE	VARCHAR2(100 BYTE)
	private String noticeContent;	 //	NOTICE_CONTENT	VARCHAR2(4000 BYTE)
	private String noticeWriter;		 //	NOTICE_WRITER	NUMBER
	// "admin" (조회 시 아이디로) / "1" (작성 시 회원 번호로)
	// > 문자열 형식의 아이디, 숫자 형식의 회원 번호 둘 다 받아칠 용도로 noticeWriter 필드는 String 타입으로 두기!!
	
	private int count;				 //	COUNT	NUMBER
	private Date createDate;		 //	CREATE_DATE	DATE
	private String status;			 //	STATUS	VARCHAR2(1 BYTE)
	
}
