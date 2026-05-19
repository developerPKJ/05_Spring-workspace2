<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	.detail-area {
		margin : auto;
		width : 90% !important;
	}
</style>
</head>
<body>

	<jsp:include page="../common/menubar.jsp" />

	<div class="outer">
		
		<br>
		<h2 align="center">사진게시글 상세조회</h2>
		<br>
		
		<table class="detail-area table">
			<tr>
				<th>제목</th>
				<td colspan="3">
					${ requestScope.b.boardTitle }
				</td>
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
				<th>대표이미지</th>
				<td colspan="3" align="center">
					<!-- 
						우리는 아까 첨부파일 조회 시 ORDER BY 절을 추가해서
						썸네일 이미지가 0 번 인덱스에 담겨서 조회될 수 있도록 이미 유도했음!!
					-->
					<img src="/myweb/${ requestScope.list[0].filePath }${ requestScope.list[0].changeName }" 
						 width="500" height="300">
				</td>
			</tr>
			<tr>
				<th>상세이미지</th>
				<td colspan="3" align="center">
					<c:choose>
						<c:when test="${ requestScope.list.size() eq 1 }">
							<!-- 상세이미지가 없을 경우 -->
							상세이미지가 존재하지 않습니다.
						</c:when>
						<c:otherwise>
							<!-- 상세이미지가 있을 경우 - 1번 인덱스부터 반복 -->
							<c:forEach var="i" begin="1" end="${ requestScope.list.size() - 1 }" step="1">
								
								<img src="/myweb/${ requestScope.list[i].filePath }${ requestScope.list[i].changeName }" 
									 width="200" height="160">
								
							</c:forEach>							
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</table>
		
		<br><br>
		
	</div>
	
	<br><br>

</body>
</html>