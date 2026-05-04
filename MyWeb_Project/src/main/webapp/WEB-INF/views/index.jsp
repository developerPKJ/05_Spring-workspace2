<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%--
		CRUD : Create, Read, Update, Delete
		: 프로그램에 필요한 기본적인 요소들
		
		- 회원 관련 기능
		로그인 : R
		회원가입 : C
		[아이디 중복확인] : R
		마이페이지 : R
		정보 수정 : U
		회원 탈퇴 : D(하지만 우린 U로 처리할 예정 - 탈퇴한 회원은 휴면 계정으로 전환)
		> 회원 정보 복구가 가능함, 회원 정보를 부모데이터로 가지고 있는 데이터들을 유지 가능(ex. 게시글)
		
		- 공지사항 관련 기능
		전체 리스트 조회 : R
		공지사항 작성 : C
		공지사항 상세 조회
		공지사항 수정 : U
		공지사항 삭제 : D(하지만 우린 U로 처리할 예정 - 삭제된 공지사항은 숨김 처리)
		
		- 일반 게시판 관련 기능
		전체 리스트 조회 : R
		게시글 작성 : C
		게시글 검색 : R
		게시글 상세 조회 : R
		게시글 수정 : U
		게시글 삭제 : D(하지만 우린 U로 처리할 예정 - 삭제된 게시글은 숨김 처리)
		
		- 사진 게시판 관련 기능
		                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
	--%>
</body>
</html>