<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	.list-area {
		width : 770px;
		margin : auto;
	}

	.thumbnail {
		width : 220px;
		display : inline-block;
		margin : 15px;
		/* border : 1px solid lightgray; */
		padding-top : 10px;
	}
	.thumbnail:hover {
		cursor : pointer;
	}
	.thumbnail>img:hover {
		opacity : 0.75;
	}
</style>
</head>
<body>

	<jsp:include page="../common/menubar.jsp" />

	<div class="outer">

		<br>
		<h2 align="center">사진게시판</h2>
		<br>

		<br><br>

		<!-- 로그인한 회원만 보여지는 글작성 버튼 -->
		<c:if test="${ not empty sessionScope.loginUser }">
			
			<div align="right" style="width : 850px;">
				<a href="/myweb/thumbnail/enrollForm" class="btn btn-secondary btn-sm">
					글작성
				</a>
				<br><br>
			</div>
			
		</c:if>

		<div class="list-area">

			<c:choose>
				<c:when test="${ not empty requestScope.list }">
					<!-- 조회된 게시글 목록이 있다면 - 반복적으로 출력 -->
					
					<c:forEach var="b" items="${ requestScope.list }">
						<div class="thumbnail" align="center">
							<input type="hidden" value="${ b.boardNo }">
							
							<img src="/myweb/${ b.titleImg }" width="200px" height="150px">
							
							<c:choose>
								<c:when test="${ b.boardTitle.length() le 9 }">
									<!-- 제목의 글자수가 9글자 이하인 경우 - 제목 그대로 출력 -->
									<p>
										No.${ b.boardNo } ${ b.boardTitle } <br>
										조회수 : ${ b.count }
									</p>
								</c:when>
								<c:otherwise>
									<!-- 제목의 글자수가 9글자 초과인 경우 - 9글자까지 끊고 "..." 덧붙여서 출력 -->
									<p>
										No.${ b.boardNo } ${ b.boardTitle.substring(0, 9).concat("...") } <br>
										조회수 : ${ b.count }
									</p>
								</c:otherwise>
							</c:choose>
								
						</div>
					</c:forEach>
					
				</c:when>
				<c:otherwise>
					<!-- 조회된 게시글 목록이 없다면 -->
					
					<br><br>
					<div align="center">
						등록된 게시글이 없습니다.
					</div>
					<br><br>
					
				</c:otherwise>
			</c:choose>
			
		</div>
		
		<script>
			$(function() {
				
				$(".thumbnail").click(function() {
					
					// 글번호를 뽑아서 상세조회 요청을 넘기기!!
					let bno = $(this).children().eq(0).val();
					
					// console.log(bno);
					
					location.href = "/myweb/thumbnail/detail/" + bno;
				});
				
			});
		</script>

		<br><br>

	</div>
	
	<br><br>

</body>
</html>