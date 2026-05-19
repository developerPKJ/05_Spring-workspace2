<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>My Web</title>
<style>
	#login-form, #user-info {
		float : right;
	}
	#login-form input {
		padding : 5px;
	}
	#user-info a {
		text-decoration : none;
		color : black;
		font-size : 12px;
	}

	.nav-area {
		background-color : black;
	}
	.menu {
		display : table-cell; /* 블록요소들을 가로로 배치 (가로길이가 짧아지더라도 옆으로 배치해줌) */
		height : 50px;
		width : 150px;
	}
	.menu>a {
		text-decoration : none;
		color : white;
		font-size : 17px;
		font-weight : 560;
		width : 100%;
		height : 100%;
		display : block;
		line-height : 50px;
		text-align : center;
	}
	.menu>a:hover {
		font-size : 18px;
		text-decoration : none;
		color : white;
	}

	/*
		* 모든 페이지마다 .outer 에 대한 스타일은 공통스타일이기 때문에
		  매 페이지마다 일일이 적는게 아니라
		  매 페이지마다 include 되야하는 menubar.jsp 에 한번만 정의해두고 계속 가져다 쓰는 구조로 수정
	*/
	.outer {
		width : 1000px;
		border : 1px dotted lightgray;
		margin : auto;
		margin-top : 50px;
	}
</style>

<!-- alertify 라이브러리 연동 구문 -->
<!-- JavaScript -->
<script src="//cdn.jsdelivr.net/npm/alertifyjs@1.14.0/build/alertify.min.js"></script>

<!-- CSS -->
<link rel="stylesheet" href="//cdn.jsdelivr.net/npm/alertifyjs@1.14.0/build/css/alertify.min.css"/>
<!-- Default theme -->
<link rel="stylesheet" href="//cdn.jsdelivr.net/npm/alertifyjs@1.14.0/build/css/themes/default.min.css"/>
<!-- Semantic UI theme -->
<link rel="stylesheet" href="//cdn.jsdelivr.net/npm/alertifyjs@1.14.0/build/css/themes/semantic.min.css"/>

<!-- JSP 에서 부트스트랩 연동도 가능함!! -->
<!-- 부트스트랩 CDN 방식으로 연동하는 구문들 -->
<!-- 예쁘게 정의된 스타일들이 들어 있는 CSS 파일 -->
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">

<!-- 간단한 동작들을 정의해둔 JS 파일 -->
<!-- 온라인 방식 -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

<!-- Popper JS -->
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>

<!-- Latest compiled JavaScript -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>

	<%-- 
		* menubar.jsp 에 공통 코드 작업을 해볼 것!!
		- 1회성 alert 기능
		- script 태그 내에서는 JSP Action Tag 들이 사용 불가함!! (자바스크립트 영역이기 때문)
	--%>
	<c:if test="${ not empty sessionScope.alertMsg }">
		<script>
			
			let alertMsg = "${ sessionScope.alertMsg }";
			
			// alert(alertMsg);
			alertify.alert(alertMsg, function(){ alertify.success('Ok'); });
			
		</script>
		<c:remove var="alertMsg" scope="session" />
	</c:if>
	

	<%-- 모든 페이지마다 상단에 include 될 menubar 만들기 --%>
	
	<br>
	
	<h1 align="center">Welcome to H Class</h1>
	
	<!-- 로그인 관련 영역 -->
	<div class="login-area">

		<c:choose>
		
			<c:when test="${ empty sessionScope.loginUser }">
			
				<!-- case1. 로그인 전 -->
				<%-- 
					* 로그인 기능 구현
					- 아이디와 비밀번호를 입력한 후 로그인 버튼을 클릭
					
					- 로그인 요청 시 http://localhost:8006/myweb/member/login 로 요청
					- url 주소 상에 특히 비밀번호가 노출되면 안됨!! (POST 방식)
				--%>
				<form id="login-form" action="/myweb/member/login" method="post"> 
		
					<table>
						<tr>
							<th>아이디</th>
							<td>
								<input type="text" name="userId" required>
							</td>
						</tr>
						<tr>
							<th>비밀번호</th>
							<td>
								<input type="password" name="userPwd" required>
							</td>
						</tr>
						<tr align="center">
							<th colspan="2">
								<button type="submit" class="btn btn-secondary btn-sm">로그인</button>
								<button type="button" class="btn btn-secondary btn-sm"
													  onclick="enrollPage();">회원가입</button>
							</th>
						</tr>
					</table>
		
				</form>
				
				<script>
					function enrollPage() {
						
						// 회원가입페이지로 이동
						location.href = "/myweb/member/enrollForm";
						// > a 태그 또는 location.href 는 GET 방식임!!
					}
				</script>
				
			</c:when>
			<c:otherwise>

				<!-- case2. 로그인 후 -->
				<div id="user-info">
					<b>${ sessionScope.loginUser.userName }</b> 님 환영합니다. <br><br>
		
					<div align="center">
						<a href="/myweb/member/myPage">마이페이지</a>
						<a href="/myweb/member/logout">로그아웃</a>
					</div>
				</div>
		
			</c:otherwise>
		
		</c:choose>

	</div>

	<br clear="both"> <!-- float 속성 해제 -->
	<br>

	<!-- 메뉴바 (navigator) 영역 -->
	<div class="nav-area" align="center">

		<div class="menu"><a href="/myweb">HOME</a></div>
		<div class="menu"><a href="/myweb/notice/list">NOTICE</a></div>
		<!--  
		<div class="menu"><a href="/myweb/board/list?cpage=1">BOARD</a></div>
		-->
		<!-- 
			* 학원 홈페이지 처럼 일반게시판 목록을 처음에 보게 될 때에는
			  쿼리스트링이 url 주소 상에 딱히 없어도 자동으로 1번 페이지로 향하게끔 하고싶다면
			  Controller 메소드에서 전달값을 매개변수로 받을 때
			  @RequestParam 어노테이션의 defaultValue 속성을 활용하면 된다!!
			  (cpage 값이 안넘어오면 1로 간주되게끔)
		-->
		<div class="menu"><a href="/myweb/board/list">BOARD</a></div> <!-- 1번 페이지로 -->
		<!-- 
			* 우리는 테이블 설계 및 구축 시 일반게시글과 사진게시글을 동일한 BOARD 테이블에 저장하기로 했었음!!
			  BOARD 테이블의 BOARD_TYPE 속성이 1 이면 일반게시글, 
			  			   BOARD_TYPE 속성이 2 면 사진게시글로 표현하기로 약속했었음!!
			  - 결국 동일한 테이블에 대한 CRUD 임!!
			  - 보통은 Controller, Service, Dao, mapper 를 도메인 (테이블) 기준으로 쪼개서 작업한다.
			  - 우리는 사진게시글에 대한 요청을 처리해주는 Controller 만 별도로 쪼개서 작업해볼것!!
			    (com.kh.myweb.board.controller.ThumbnailController 로 생성)
		-->
		<div class="menu"><a href="/myweb/thumbnail/list">PHOTO</a></div>

	</div>

</body>
</html>









