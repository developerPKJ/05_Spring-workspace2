<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	#detail-area, #reply-area>table {
		margin : auto;
		width : 90%;
	}

	#reply-area textarea {
		width : 570px;
		height : 90px;
		resize : none;
	}
</style>
</head>
<body>

	<jsp:include page="../common/menubar.jsp" />

	<div class="outer">
	
		<br>
		<h2 align="center">게시글 상세조회</h2>
		<br>
		
		<table id="detail-area" class="table">
			<tr>
				<th>카테고리</th>
				<td>${ requestScope.b.category }</td>
				<th>제목</th>
				<td>${ requestScope.b.boardTitle }</td>
			</tr>
			<tr>
				<th>작성자</th>
				<td>${ requestScope.b.boardWriter }</td>
				<th>작성일</th>
				<td>${ requestScope.b.createDate }</td>
			</tr>
			<tr>
				<th>내용</th>
				<td colspan="3">
					<p style="height : 300px;">
						${ requestScope.b.boardContent }
					</p>
				</td>
			</tr>
			<tr>
				<th>첨부파일</th>
				<td colspan="3">
					<c:choose>
						<c:when test="${ empty requestScope.at }">
							<!-- case1. 첨부파일이 없을 경우 -->
							첨부파일이 없습니다.
						</c:when>
						<c:otherwise>
							<!-- case2. 첨부파일이 있을 경우 -->
							<a download="${ requestScope.at.originName }"
							   href="/myweb/${ requestScope.at.filePath }${ requestScope.at.changeName }">
								${ requestScope.at.originName }
							</a>
							<!-- 
								- a 태그의 href 속성에 이 서버가 갖고있는 파일의 경로를 정확히 적어주면
								  클릭 시 그 파일이 브라우저로 열려진다!!
								- 다운로드를 하고싶다면 a 태그에 download 속성을 걸어주면 된다!!
							-->
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</table>
		
		<br><br>

		<div align="center">
			<a href="/myweb/board/list" class="btn btn-secondary btn-sm">목록가기</a>
			
			<!-- 수정하기, 삭제하기 버튼은 작성자만 보여져야함!! -->
			<c:if test="${ (not empty sessionScope.loginUser) and (sessionScope.loginUser.userId eq requestScope.b.boardWriter) }">
				<a class="btn btn-warning btn-sm" onclick="postFormSubmit(1);">수정하기</a>
				<a class="btn btn-danger btn-sm" onclick="postFormSubmit(2);">삭제하기</a>
				
				<form id="postForm" action="" method="post">
					<input type="hidden" name="bno" value="${ requestScope.b.boardNo }">
				</form>
				
				<script>
					function postFormSubmit(num) {
						
						// num 값에 따른 알맞은 action 속성을 부여한 뒤 submit 
						if(num == 1) {
							
							$("#postForm").prop("action", "/myweb/board/updateForm").submit();
							
						} else {
							
							$("#postForm").prop("action", "/myweb/board/delete").submit();	
						}
					}
				</script>
			</c:if>
		</div>

		<br><br>

		<!-- 댓글 관련 화면 구현 - AJAX 배우고 나서 기능 구현 -->
		<div id="reply-area">

			<table class="table">
				<thead>
					<tr>
						<th>댓글작성</th>
						<td>
							<textarea></textarea>
						</td>
						<td>
							<button class="btn btn-secondary btn-sm">
								댓글등록
							</button>
						</td>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>admin</td>
						<td>댓글내용이 들어갈 자리~~</td>
						<td>2026-05-14</td>
					</tr>
					<tr>
						<td>admin</td>
						<td>댓글내용이 들어갈 자리~~</td>
						<td>2026-05-14</td>
					</tr>
					<tr>
						<td>admin</td>
						<td>댓글내용이 들어갈 자리~~</td>
						<td>2026-05-14</td>
					</tr>
				</tbody>
			</table>

		</div>

		<br><br>

	</div>
	
	<br><br>

</body>
</html>



