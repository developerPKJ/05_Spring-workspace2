<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	#enroll-form table {
		margin : auto;
		width : 90%;
	}

	#enroll-form input, #enroll-form textarea {
		width : 100%;
		padding : 5px;
		margin : 5px;
	}
	#enroll-form textarea {
		resize : none;
		height : 300px;
	}
</style>
</head>
<body>

	<jsp:include page="../common/menubar.jsp" />

	<div class="outer">
		
		<br>
		<h2 align="center">공지사항 작성</h2>
		<br>
		
		<!-- 
			* 공지사항 작성 기능 구현
			- 관리자가 제목, 내용을 입력 후 등록하기 버튼을 클릭하는 순간
			  
			  http://localhost:8006/myweb/notice/insert 로 POST 방식으로 요청
		-->
		
		<form id="enroll-form" action="/myweb/notice/insert" method="post">

			<!--
				* 공지사항 게시글 작성 시 입력해야할 데이터들
				- 공지사항 제목, 공지사항 내용
				- 직접 입력받지는 않지만 (눈에 노출되지도 않지만) 
				  작성자 (현재 로그인한 사용자) 의 회원번호도 같이 넘겨줘야함!!
				
				현재 로그인한 회원의 정보를 알아내고자 한다면
				1. Controller 에서 session 에 담겨있는 회원정보 뽑기
				2. form 태그 내에서 input type="hidden" 으로 같이 넘기기
			-->
			<input type="hidden" name="noticeWriter" 
				   value="${ sessionScope.loginUser.userNo }"> 
			
 			<table class="table">
				<tr>
					<th>제목</th>
					<td>
						<input type="text" name="noticeTitle" required>
					</td>
				</tr>
				<tr>
					<th>내용</th>
					<td>
						<textarea name="noticeContent" required></textarea>
					</td>
				</tr>
			</table>

			<br><br>

			<div align="center">
				<button type="submit" class="btn btn-primary btn-sm">등록하기</button>
				<button type="reset" class="btn btn-secondary btn-sm">초기화</button>
				<button type="button" class="btn btn-secondary btn-sm"
						onclick="history.back();">뒤로가기</button>
				<!-- history.back() : 이전 페이지로 돌아가게 해주는 메소드 속성 -->
			</div>
			
		</form>
		
		<br><br>
		
	</div>
	
	<br><br>

</body>
</html>



