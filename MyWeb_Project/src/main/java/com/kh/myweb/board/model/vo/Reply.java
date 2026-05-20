package com.kh.myweb.board.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
// @AllArgsConstructor
// > 스프링에서는 개발자인 내가 직접 객체를 생성할 일이 왠만해서는 없음!!
//   그리고 심지어 스프링이 객체를 생성해서 나한테 주입해줄 때에도 기본생성자 + Setter 조합으로 만들어서 준다.
// > 주로, VO 클래스 최초 정의 시에는 안썼다가, 나중에 내가 직접 매개변수 생성자로 객체를 생성할 일이 생기면
//   그 때 어노테이션을 붙여서 쓰면 된다!!
@Setter
@Getter
@ToString
public class Reply {
	
	private int replyNo;		 //	REPLY_NO	NUMBER
	private String replyContent; //	REPLY_CONTENT	VARCHAR2(400 BYTE)
	private int refBoardNo;		 //	REF_BNO	NUMBER
	private String replyWriter;  //	REPLY_WRITER	NUMBER // "1" / "admin"
	private String createDate;	 //	CREATE_DATE	DATE
	// > 이번에는 날짜 타입을 String 으로 받아 처리해볼 것!!
	//   (댓글 같은 경우는 보통 작성 시간까지 나오는 경우가 많기 때문)
	private String status;		 //	STATUS	VARCHAR2(1 BYTE)
}




