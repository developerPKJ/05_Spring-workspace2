package com.kh.myweb.board.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor // 기본생성자를 자동완성 해주는 어노테이션
@AllArgsConstructor // 모든 필드에 대한 매개변수 생성자를 자동완성 해주는 어노테이션
@Setter // setter 메소드들을 자동완성 해주는 어노테이션
@Getter // getter 메소드들을 자동완성 해주는 어노테이션
@ToString // toString 메소드를 오버라이딩 해주는 어노테이션
public class Board {
	// field
	private int boardNo;			// BOARD_NO	NUMBER
	private int boardType;			// BOARD_TYPE	NUMBER
	private String category;		// CATEGORY_NO	NUMBER
		// insert, update 시에는 카테고리 번호로, 조회시는 "공통"으로
	
	private String boardTitle;		// BOARD_TITLE	VARCHAR2(100 BYTE)
	private String boardContent;	// BOARD_CONTENT	VARCHAR2(4000 BYTE)
	private String boardWriter;		// BOARD_WRITER	NUMBER
		// 회원은 번호로 저장, 관리자는 "admin"으로 저장
	
	private int count;				// COUNT	NUMBER
	private Date createDate;		// CREATE_DATE	DATE
	private String status;			// STATUS	VARCHAR2(1 BYTE)
}
