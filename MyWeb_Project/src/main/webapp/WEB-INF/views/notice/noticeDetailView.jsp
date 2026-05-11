<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	#detail-area {
		margin : auto;
		width : 90%;
	}
</style>
</head>
<body>

	<jsp:include page="../common/menubar.jsp" />

	<div class="outer">
	
		<br>
		<h2 align="center">공지사항 상세조회</h2>
		<br>

		<table id="detail-area" class="table">
			<tr>
				<th>제목</th>
				<td colspan="3">
					${ requestScope.n.noticeTitle }
				</td>
			</tr>
			<tr>
				<th>작성자</th>
				<td>
					${ requestScope.n.noticeWriter }
				</td>
				<th>작성일</th>
				<td>
					${ requestScope.n.createDate }
				</td>
			</tr>
			<tr>
				<th>내용</th>
				<td colspan="3">
					<p style="height : 300px;">
						${ requestScope.n.noticeContent }
					</p>
				</td>
			</tr>
		</table>

		<br><br>

		<div align="center">
			<a href="/myweb/notice/list" 
			   class="btn btn-secondary btn-sm">목록으로</a>
			   
			<!-- 
				- 수정하기, 삭제하기 버튼은
				  현재 이 페이지를 보는 사용자가 (즉 로그인한 사용자가)
				  해당 글을 작성한 작성자일 경우에만 보여져야 하는 버튼임!!
			-->	
			<c:if test="${ (not empty sessionScope.loginUser) and (sessionScope.loginUser.userId eq requestScope.n.noticeWriter) }">
				<a class="btn btn-warning btn-sm" onclick="postFormSubmit(1);">수정하기</a>
				<a class="btn btn-danger btn-sm" onclick="postFormSubmit(2);">삭제하기</a>
				<form id="postForm" action="" method="post">
					<input type="hidden" name="nno" 
										 value="${ requestScope.n.noticeNo }">
				</form>
				<script>
					function postFormSubmit(num) {
						
						if(num == 1) {
							// > 수정하기 버튼을 클릭했음
							
							$("#postForm").prop("action", "/myweb/notice/updateForm").submit();
							
						} else {
							// > 삭제하기 버튼을 클릭했음
							
							$("#postForm").prop("action", "/myweb/notice/delete").submit();
							
						}
					}
				</script>
				<!-- 
					- 수정하기와 삭제하기는 클릭 시 글번호가 노출되는 GET 방식 보다는
					  POST 방식이 조금 더 보안 상 안전하다!!
					- url 주소를 조작하여 여기 저기 게시글을 건들고 다닐 수 있기 때문
					
					1. 눈에 안보이게끔 nno 키값으로 해당 글번호를 넘길 수 있게
					   form 태그와 input type="hidden" 을 배치함 (action 은 비워둠)
					2. 수정하기와 삭제하기 버튼 클릭 시 실행시킬 함수를 정의
					3. action 속성만 어떤 버튼이 클릭되었을 때 어디로 보낼건지 잘 설정 후
					   submit 처리 진행
				-->
			</c:if>
		</div>

		<br><br>

	</div>
	
	<br><br>

</body>
</html>







