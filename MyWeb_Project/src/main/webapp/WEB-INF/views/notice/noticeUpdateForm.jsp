<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	#update-form table {
		margin : auto;
		width : 90%;
	}

	#update-form input, #update-form textarea {
		width : 100%;
		padding : 5px;
		margin : 5px;
	}
	#update-form textarea {
		resize : none;
		height : 300px;
	}
</style>
</head>
<body>

	<jsp:include page="../common/menubar.jsp" />

	<div class="outer">
		
		<br>
		<h2 align="center">공지사항 수정</h2>
		<br>
		
		<!-- 
			* 공지사항 수정 기능 구현
			- 수정할 제목과 내용을 입력하고 수정하기 버튼을 클릭하면
			  
			  http://localhost:8006/myweb/notice/update 로 POST 방식으로 요청
		-->
		<form id="update-form" action="/myweb/notice/update" method="post">

			<!--
				* 공지사항 수정 시 제목과 내용을 입력받아서 수정해야함!!
				- 눈에 보이지는 않지만 해당 수정할 공지사항의 글번호도 같이 넘겨줘야함!!
				  (input type="hidden")
			-->
			
			<input type="hidden" name="noticeNo" value="${ requestScope.n.noticeNo }">
			
			<table class="table">
				<tr>
					<th>제목</th>
					<td>
						<input type="text" name="noticeTitle"
							   value="${ requestScope.n.noticeTitle }" required>
					</td>
				</tr>
				<tr>
					<th>내용</th>
					<td>
						<textarea name="noticeContent" 
								  required>${ requestScope.n.noticeContent }</textarea>
					</td>
				</tr>
			</table>

			<br><br>

			<div align="center">
				<button type="submit" class="btn btn-primary btn-sm">수정하기</button>
				<button type="button" class="btn btn-secondary btn-sm"
						onclick="history.back();">뒤로가기</button>
			</div>

		</form>
		
		<br><br>
		
	</div>
	
	<br><br>

</body>
</html>






