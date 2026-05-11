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
		margin : auto;
		width : 90% !important;
		text-align: center;
	}

	.list-area>tbody>tr:hover {
		cursor: pointer;
	}
</style>
</head>
<body>
	<jsp:include page="../common/menubar.jsp" />
	
	<div class="outer">
		<br>
		<h2 align="center">일반게시판</h2>
		<br>

		<br><br>

		<!-- 검색창 -->
		<div id="search-area">

		</div>

		<br><br>

		<!-- 게시글 목록 -->
		<table class="list-area table table-hover">
			<thead>
				<tr>
					<th>글번호</th>
					<th>카테고리</th>
					<th>제목</th>
					<th>작성자</th>
					<th>조회수</th>
					<th>작성일</th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${empty requestScope.list}">
						<tr>
							<td colspan="6">게시글이 없습니다.</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach var="b" items="${requestScope.list}">
							<tr>
								<td>${b.boardNo}</td>
								<td>${b.category}</td>
								<td>${b.boardTitle}</td>
								<td>${b.boardWriter}</td>
								<td>${b.count}</td>
								<td>${b.createDate}</td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>

		<br><br>

		<!-- 페이징바 -->
		<div class="paging-area">
			<ul class="pagination justify-content-center">
				<c:choose>
					<c:when test="${ requestScope.pi.currentPage <= 10 }">
						<li class="page-item disabled">
							<a class="page-link" href="#">&lt;&lt;</a>
						</li>
					</c:when>
					<c:otherwise>
						<li class="page-item">
							<a class="page-link" 
							   href="/myweb/board/list?cpage=${ requestScope.pi.currentPage - 10 }">
								&lt;&lt;
							</a>
						</li>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${ requestScope.pi.currentPage eq 1 }">
						<li class="page-item disabled">
							<a class="page-link" href="#">Previous</a>
						</li>
					</c:when>
					<c:otherwise>
						<li class="page-item">
							<a class="page-link" 
							   href="/myweb/board/list?cpage=${ requestScope.pi.currentPage - 1 }">
								Previous
							</a>
						</li>
					</c:otherwise>
				</c:choose>
				<c:forEach var="p" begin="${ requestScope.pi.startPage }" 
								   end="${ requestScope.pi.endPage }">
					<c:choose>
						<c:when test="${ p == requestScope.pi.currentPage }">
							<li class="page-item active">
								<a class="page-link" href="/myweb/board/list?cpage=${p}">
									${p}
								</a>
							</li>
						</c:when>
						<c:otherwise>
							<li class="page-item">
								<a class="page-link" href="/myweb/board/list?cpage=${p}">
									${p}
								</a>
							</li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				<c:choose>
					<c:when test="${ requestScope.pi.currentPage eq requestScope.pi.maxPage }">
						<li class="page-item disabled">
							<a class="page-link" href="#">Next</a>
						</li>
					</c:when>
					<c:otherwise>
						<li class="page-item">
							<a class="page-link" 
							   href="/myweb/board/list?cpage=${ requestScope.pi.currentPage + 1 }">
								Next
							</a>
						</li>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${ requestScope.pi.startPage + 10 > requestScope.pi.maxPage }">
						<li class="page-item disabled">
							<a class="page-link" href="#">&gt;&gt;</a>
						</li>
					</c:when>
					<c:otherwise>
						<li class="page-item">
							<a class="page-link" 
							   href="/myweb/board/list?cpage=${ requestScope.pi.startPage + 10 }">
								&gt;&gt;
							</a>
						</li>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
	</div>
</body>
</html>