<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

	<!-- 항상 모든 페이지 상단에는 menubar.jsp 가 보여지게끔 include -->
	<jsp:include page="common/menubar.jsp" />

<%-- 
	* CRUD
	- 대부분의 컴퓨터 소프트웨어 (프로그램) 가 가지는 기본적인 데이터 처리 기능들을 묶어서 CRUD 라고 부른다.
	- 즉, 프로그램이라면 응당 가져야 하는 데이터 처리 기능들
	
	C : Create (생성) - "데이터" 를 생성하겠다. 즉 INSERT 구문을 나타냄
	R : Read (읽기) - "데이터" 를 읽겠다. 조회하겠다. 즉 SELECT 구문을 나타냄
	U : Update (갱신) - "데이터" 를 갱신하겠다. 수정하겠다. 즉 UPDATE 구문을 나타냄
	D : Delete (삭제) - "데이터" 를 삭제하겠다. 즉 DELETE 구문을 나타냄
	
	* 목적이 없는 웹 사이트 만들어보기!!
	- 웹 사이트라면 응당 가져야할 기능들은 모두 만들어볼 것!!

	- 회원 관련 기능
						C (INSERT)	| 	R (SELECT)	|	U (UPDATE)	|	D (DELETE)
	==================================================================================
	로그인			|				|		O		|				|
	회원가입			|		O		|				|				|
	[아이디 중복확인]	|				|		O		|				|
	마이페이지			|				|		O		|				|
	내정보변경			|				|				|		O		|
	회원탈퇴			|				|				|				|		O
	
	단, 회원탈퇴 기능은 의미상 D (DELETE) 이지만, DELETE 구문이 아닌 UPDATE 구문으로 구현할 것!!
	회원 정보 복구가 가능하고, 회원 정보를 부모데이터로 삼는 것들이 많기 때문!!
	
	- 공지사항 관련 기능 (제일 간단한 형태의 게시판)
	공지사항 리스트 조회 - R (SELECT)
	공지사항 작성 - C (INSERT)
	공지사항 상세 조회 - R (SELECT)
	공지사항 수정 - U (UPDATE)
	공지사항 삭제 - D (DELETE) : UPDATE 구문으로 구현할 것!!
	
	- 일반게시판 관련 기능 (조금 더 어려운 형태의 게시판)
	일반게시판 리스트 조회 - R (SELECT) + 페이징처리
	일반게시판 검색 - R (SELECT)
	일반게시판 작성 - C (INSERT) + 첨부파일 1개 업로드
	일반게시판 상세 조회 - R (SELECT)
	일반게시판 수정 - U (UPDATE)
	일반게시판 삭제 - D (DELETE) : UPDATE 구문으로 구현할 것!!
	[댓글 리스트 조회 - R (SELECT)]
	[댓글 작성 - C (INSERT)]
	
	- 사진게시판 관련 기능 (썸네일 이미지 형태의 게시판)
	사진게시판 리스트 조회 - R (SELECT) + 썸네일 이미지 형식
	사진게시판 상세 조회 - R (SELECT) 
	사진게시판 작성 - C (INSERT) + 다중 파일 업로드
	
	단, [] 기능들은 맨 마지막에 만들 것!! 
	AJAX 기술을 활용해서 구현하는게 더 좋은 기능들이기 때문!!
	
	- 기능적인 요소 말고도 비기능적인 요소들도 고려해야함!!
	예) 시큐어코딩
	
	- 우리는 이 웹사이트에서 로그인하지 않은 사용자가 url 주소를 통해
	  여기저기 페이지 이동을 해서 로그인할 경우에만 보여져야 하는 페이지에
	  접속할 수 있는것을 확인함!!
	  (이걸 막으려면 스프링의 "Interceptor" 를 이용해서 막아야 한다)
--%>

</body>
</html>




