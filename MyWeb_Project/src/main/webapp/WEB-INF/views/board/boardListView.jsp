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
		<div id="search-area" align="center">
			<form action="/myweb/board/search" method="get">
				<!-- 검색 조건과 검색어를 입력받고 검색버튼 클릭 -->
				<select name="condition">
					<option value="title">제목</option>
					<option value="writer">작성자</option>
					<option value="content">내용</option>
				</select>

				<input type="search" name="keyword" value="${ requestScope.keyword }">
				<!-- 
					EL 구문은 꺼내올 응답 데이터가 없다면 오류도 안내고 출력도 안하고 넘어감
					> boardListView.jsp 에서 일반게시글 목록 조회와 일반게시글 검색 조회 둘다 재활용가능
				-->
				<button type="submit" class="btn btn-secondary btn-sm">검색</button>
			</form>

			<!--
				검색 창 입력 값 유지는 위에서 작성한 EL 구문으로 해결했지만
				condition 검색 조건이 유지되지 않는 문제는 위의 EL 구문으로 해결할 수 없음
				> 검색 조건에 대한 select, option 유지 구문 작성 필요
				- 검색 결과에 대한 응답화면일 경우에만 실행되야하는 코드임
				(응답데이터로 condition이나 keyword가 존재할 때만 실행되야하는 코드)
			-->
			<c:if test="${ not empty requestScope.condition}">
				<script>
					$(function() {
						// 검색 조건이 유지되도록 하는 구문
						// - 검색창의 select 태그 자식들 중
						// - value 속성값이 응답데이터로 넘어온 condition과 일치하는 요소를 선택해서
						// - 해당 요소의 selected 속성값을 true로 변경
						$('#search-area option[value="${ requestScope.condition }"]').prop('selected', true);
					});
				</script>
			</c:if>
		</div>

		<br><br>

		<!-- 로그인한 회원만 보여지는 글작성 버튼 -->
		<c:if test="${ not empty sessionScope.loginUser }">
			<div align="right" style="width: 90%; margin: auto;">
				<a href="/myweb/board/enrollForm" class="btn btn-secondary btn-sm">
					글작성
				</a>

				<br><br>
			</div>
		</c:if>

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
						<c:choose>
							<c:when test="${ not empty requestScope.condition }">
								<li class="page-item">
									<a class="page-link" 
									   href="/myweb/board/search?cpage=${ requestScope.pi.currentPage - 10 }&condition=${ requestScope.condition }&keyword=${ requestScope.keyword }">
										&lt;&lt;
									</a>
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
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${ requestScope.pi.currentPage eq 1 }">
						<li class="page-item disabled">
							<a class="page-link" href="#">Previous</a>
						</li>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${ not empty requestScope.condition }">
								<li class="page-item">
									<a class="page-link" 
									   href="/myweb/board/search?cpage=${ requestScope.pi.currentPage - 1 }&condition=${ requestScope.condition }&keyword=${ requestScope.keyword }">
										Previous
									</a>
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
					</c:otherwise>
				</c:choose>
				<c:forEach var="p" begin="${ requestScope.pi.startPage }" 
								   end="${ requestScope.pi.endPage }">
					<c:choose>
						<c:when test="${ p == requestScope.pi.currentPage }">
							<c:choose>
								<c:when test="${ not empty requestScope.condition }">
									<li class="page-item active">
										<a class="page-link" href="/myweb/board/search?cpage=${p}&condition=${ requestScope.condition }&keyword=${ requestScope.keyword }">
											${p}
										</a>
									</li>
								</c:when>
								<c:otherwise>
									<li class="page-item active">
										<a class="page-link" href="/myweb/board/list?cpage=${p}">
											${p}
										</a>
									</li>
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${ not empty requestScope.condition }">
									<li class="page-item">
										<a class="page-link" href="/myweb/board/search?cpage=${p}&condition=${ requestScope.condition }&keyword=${ requestScope.keyword }">
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
						<c:choose>
							<c:when test="${ not empty requestScope.condition }">
								<li class="page-item">
									<a class="page-link" 
									   href="/myweb/board/search?cpage=${ requestScope.pi.currentPage + 1 }&condition=${ requestScope.condition }&keyword=${ requestScope.keyword }">
										Next
									</a>
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
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${ requestScope.pi.startPage + 10 > requestScope.pi.maxPage }">
						<li class="page-item disabled">
							<a class="page-link" href="#">&gt;&gt;</a>
						</li>
					</c:when>
					<c:otherwise>
						<c:choose>
							<c:when test="${ not empty requestScope.condition }">
								<li class="page-item">
									<a class="page-link" 
									   href="/myweb/board/search?cpage=${ requestScope.pi.startPage + 10 }&condition=${ requestScope.condition }&keyword=${ requestScope.keyword }">
										&gt;&gt;
									</a>
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
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
	</div>
</body>
</html>