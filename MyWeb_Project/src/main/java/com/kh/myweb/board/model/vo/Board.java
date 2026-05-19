package com.kh.myweb.board.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Board {
	
	// 필드부 (컬럼 정보와 유사하게)
	private int boardNo;			 //	BOARD_NO	NUMBER
	private int boardType;			 //	BOARD_TYPE	NUMBER
	private String category;		 //	CATEGORY_NO	NUMBER
	// > "10" (작성 또는 수정 시 숫자 형태의 카테고리 번호) / "공통" (조회 기능 구현 시 문자열 형태의 카테고리명)
	
	private String boardTitle;		 //	BOARD_TITLE	VARCHAR2(100 BYTE)
	private String boardContent;	 //	BOARD_CONTENT	VARCHAR2(4000 BYTE)
	private String boardWriter;		 //	BOARD_WRITER	NUMBER
	// > "1" (작성 시 회원번호) / "admin" (조회 시 회원 아이디)
	
	private int count;				 //	COUNT	NUMBER
	private Date createDate;		 //	CREATE_DATE	DATE
	private String status;			 //	STATUS	VARCHAR2(1 BYTE)
	
	// 사진게시글 목록 조회 시 필요한 썸네일 이미지의 경로 + 수정파일명을 담을 필드를 추가
	private String titleImg;
	
}
