<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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

	#enroll-form input, #enroll-form textarea, #enroll-form select {
		padding : 5px;
		margin : 5px;
	}
	#enroll-form input, #enroll-form textarea {
		width : 100%;
	}
	#enroll-form textarea {
		height : 300px;
		resize : none;
	}
</style>
</head>
<body>

	<jsp:include page="../common/menubar.jsp" />

	<div class="outer">
	
		<br>
		<h2 align="center">일반게시글 작성</h2>
		<br>
		
		<!-- 
			* 일반게시글 작성 기능 구현
			- 로그인된 상태에서 카테고리번호, 제목, 내용, 첨부파일을 입력하고 등록하기 버튼을 클릭하면
			  
			  http://localhost:8006/myweb/board/insert 로 POST 방식으로 요청
			  
			- enctype 속성은 form 태그 내부에서 요청 시 전달값 중에 첨부파일도 같이 넘기겠다 라는 뜻임!!
			
			[ 표현법 ]
			enctype="multipart/form-data" 
		-->
		<form id="enroll-form" action="/myweb/board/insert" method="post"
							   enctype="multipart/form-data">

			<!--
				- 일반게시글 작성 시
				  카테고리번호, 제목, 내용, 첨부파일 까지 일단 입력받아야함!!
				- 또한 입력받지 않고, 눈에 노출되지도 않으면서 작성자 (현재 로그인한 회원)의
				  회원번호도 같이 넘겨야함 (input type="hidden")
			-->
			<input type="hidden" name="boardWriter" value="${ sessionScope.loginUser.userNo }">
			
			<table class="table">
				<tr>
					<th>카테고리</th>
					<td>
						<select name="category">
							<c:forEach var="c" items="${ requestScope.list }">
								<option value="${ c.categoryNo }">${ c.categoryName }</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<th>제목</th>
					<td>
						<input type="text" name="boardTitle" required>
					</td>
				</tr>
				<tr>
					<th>내용</th>
					<td>
						<textarea name="boardContent" required></textarea>
					</td>
				</tr>
				<tr>
					<th>첨부파일</th>
					<td>
						<input type="file" name="upfile">
					</td>
				</tr>
			</table>

			<br><br>

			<div align="center">
				<button type="submit" class="btn btn-primary btn-sm">등록하기</button>
				<button type="reset" class="btn btn-secondary btn-sm">초기화</button>
			</div>

		</form>
		
		<br><br>

	</div>
	
	<br><br>

</body>
</html>






