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
	/*
	FILE_NO	NUMBER
	REF_BNO	NUMBER
	ORIGIN_NAME	VARCHAR2(255 BYTE)
	CHANGE_NAME	VARCHAR2(255 BYTE)
	FILE_PATH	VARCHAR2(1000 BYTE)
	UPLOAD_DATE	DATE
	FILE_LEVEL	NUMBER
	STATUS	VARCHAR2(1 BYTE)
	*/
	// field
	private int fileNo;
	private int refno;
	private String originName;
	private String changeName;
	private String filePath;
	private Date uploadDate;
	private int fileLevel;
	private String status;
}
