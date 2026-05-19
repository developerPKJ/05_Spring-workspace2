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
public class Attachment {

	// 필드부
	private int fileNo;				 //	FILE_NO	NUMBER
	private int refNo;				 //	REF_BNO	NUMBER
	private String originName;		 //	ORIGIN_NAME	VARCHAR2(255 BYTE)
	private String changeName;		 //	CHANGE_NAME	VARCHAR2(255 BYTE)
	private String filePath;		 //	FILE_PATH	VARCHAR2(1000 BYTE)
	private Date uploadDate;		 //	UPLOAD_DATE	DATE
	private int fileLevel;			 //	FILE_LEVEL	NUMBER
	private String status;			 //	STATUS	VARCHAR2(1 BYTE)
}
