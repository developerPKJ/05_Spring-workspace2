<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- taglib 지시어는 매 페이지마다 일일이 필요하다면 다 적어줘야함 --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	.list-area {
		width : 90% !important;
		/* 우선순위에 의해 스타일이 적용되지 않음!! */
		
		text-align : center;
		margin : auto;
	}
	
	.list-area>tbody>tr:hover {
		cursor : pointer;
	}
</style>
</head>
<body>

	<jsp:include page="../common/menubar.jsp" />
	
	<div class="outer">
	
		<br>
		<h2 align="center">공지사항</h2>
		<br>

		<br>

		<!-- 관리자만 보이는 글작성 버튼으로 배치 -->
		<c:if test="${ (not empty sessionScope.loginUser) and (sessionScope.loginUser.userId eq 'admin') }">
			<div align="right" style="width : 950px;">
				<a href="/myweb/notice/enrollForm" class="btn btn-secondary btn-sm">
					글작성
				</a>
	
				<br><br>
			</div>
		</c:if>

		<table class="list-area table table-hover">
			<thead>
				<tr>
					<th>글번호</th>
					<th>글제목</th>
					<th>작성자</th>
					<th>조회수</th>
					<th>작성일</th>
				</tr>
			</thead>
			<tbody>
			
				<c:choose>
				
					<c:when test="${ empty requestScope.list }">
						<!-- case1. 조회된 공지사항이 없다면 -->
						<tr>
							<th colspan="5">
								조회된 공지사항이 없습니다.
							</th>
						</tr>
					</c:when>
					<c:otherwise>
						
						<!-- case2. 조회된 공지사항들이 있다면 -->
						<c:forEach var="n" items="${ requestScope.list }">
							
							<tr>
								<td>${ n.noticeNo }</td>
								<td>${ n.noticeTitle }</td>
								<td>${ n.noticeWriter }</td>
								<td>${ n.count }</td>
								<td>${ n.createDate }</td>
							</tr>
							
						</c:forEach>
						
					</c:otherwise>
					
				</c:choose>
				
			</tbody>
		</table>

		<br><br>

	</div>

	<br><br>
	
	<script>
		$(function() {
			
			// 게시글 1개를 나타내는 tr 요소에 클릭이벤트 부여
			// > 해당 게시글 번호에 해당하는 게시글 상세보기 요청
			$(".list-area>tbody>tr").click(function() {
				
				// console.log("클릭됨!!");
				// console.log($(this).children().eq(0).text());
				
				// 방금 클릭당한 글번호를 뽑아서 변수에 담기
				let nno = $(this).children().eq(0).text();
				// > 글번호가 Primary Key 제약조건이기 때문에 전달값으로 넘길 것!!
				
				// 쿼리스트링 방식 적용
				// location.href = "/myweb/notice/detail?nno=" + nno;
				// > href 로 요청을 보내면 GET 방식이므로
				//   내가 필요하다면 요청 시 전달값을 쿼리스트링으로 직접 명시해서 보낸다.
				
				// Path Variable 방식 적용
				location.href ="/myweb/notice/detail/" + nno;
				// > 요청 url 주소 사이에 글번호를 마치 url 주소의 일부인것 마냥 같이 보낸다.
				
			});
			
		});
	</script>

</body>
</html>





