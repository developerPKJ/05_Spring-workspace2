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
		text-align : center;
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
		<h2 align="center">일반게시판</h2>
		<br>

		<br><br>

		<!-- 검색창을 만들 부분 -->
		<div id="search-area" align="center">
		
			<!-- 
				* 게시글 검색 기능 구현
				- 검색 조건과 검색어를 입력 후 검색 버튼을 클릭하는 순간
				  http://localhost:8006/myweb/board/search 로 GET 방식으로 요청
			-->
			<form action="/myweb/board/search" method="get">
				
				<!-- 검색 시 검색 조건과 검색어를 입력받고 검색버튼을 클릭함!! -->
				<select name="condition">
					<option value="writer">작성자</option>
					<option value="title">제목</option>
					<option value="content">내용</option>
				</select>
				
				<input type="search" name="keyword" value="${ requestScope.keyword }">
				<!-- 
					- EL 구문은 꺼내올 응답데이터가 없다면 오류도 안내고 출력도 안하고 만다!!
					- 그래서 boardListView.jsp 에서 일반게시글 목록 조회 와 일반게시글 검색 화면
					  둘 다 재활용이 가능해진다!!
				-->
				
				<button type="submit" class="btn btn-secondary btn-sm">검색</button>
				
			</form>
			
			<!-- 
				- 검색조건 (condition) 에 대한 select, option 유지 구문 작성 - js 
				- 단, 검색 결과에대한 응답화면일 경우에만 실행되야하는 코드임!!
				  (즉, 응답데이터로 condition 또는 keyword 가 넘어온 경우에만 코드 실행)	
			-->
			<c:if test="${ not empty requestScope.condition }">
				<!-- 응답데이터로 condition 이 넘어온 경우 -->
				<script>
					$(function() {
						
						// 검색창의 select 태그의 자식들 중 option 태그들 중
						// value 속성값이 응답데이터의 condition 과 일치하는 놈을 찾아
						// selected 속성을 부여하겠다. (true)
						$("#search-area option[value=${ requestScope.condition }]").prop("selected", true);
					});
				</script>
			</c:if>
		
		</div>
		
		<br><br>
		
		<!-- 로그인한 회원만 보여지는 글작성 버튼 -->
		<c:if test="${ not empty sessionScope.loginUser }">
			<div align="right" style="width : 950px;">
				<a href="/myweb/board/enrollForm" class="btn btn-secondary btn-sm">
					글작성
				</a>
				
				<br><br>
			</div>
		</c:if>

		<!-- 게시글의 목록을 보여주는 부분 -->
		<table class="list-area table table-hover">
			<thead>
				<tr>
					<th>글번호</th>
					<th>카테고리</th>
					<th>글제목</th>
					<th>작성자</th>
					<th>조회수</th>
					<th>작성일</th>
				</tr>
			</thead>
			<tbody>
				<c:choose>
					<c:when test="${ empty requestScope.list }">
						
						<tr>
							<th colspan="6">
								조회된 게시글이 없습니다.
							</th>
						</tr>
					
					</c:when>
					<c:otherwise>
						
						<c:forEach var="b" items="${ requestScope.list }">
							
							<tr>
								<td>${ b.boardNo }</td>
								<td>${ b.category }</td>
								<td>${ b.boardTitle }</td>
								<td>${ b.boardWriter }</td>
								<td>${ b.count }</td>
								<td>${ b.createDate }</td>
							</tr>
							
						</c:forEach>
						
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
		
		<script>
			$(function() {
				
				// 게시글을 나타내는 tr 요소에 클릭이벤트 부여 (클릭 시 해당 게시글 상세조회 요청)
				$(".list-area>tbody>tr").click(function() {
					
					let bno = $(this).children().eq(0).text();
					
					// console.log(bno);
					
					location.href = "/myweb/board/detail/" + bno;
					
				});
			});
		</script>

		<br><br>

		<!-- 페이징바 영역 -->
		<div class="paging-area">
		
			<ul class="pagination justify-content-center">
			
			  <c:choose>
			  	<c:when test="${ requestScope.pi.currentPage eq 1 }">
			  		<!-- 1 번 페이지일 경우 -->
			  		<li class="page-item disabled">
					  <a class="page-link">Previous</a>
					</li>
			  	</c:when>
			  	<c:otherwise>
			  	
			  		<c:choose>
			  			<c:when test="${ empty requestScope.condition }">
			  				<!-- 일반 목록 조회일 경우 -->
			  				<li class="page-item">
							  <a class="page-link" href="/myweb/board/list?cpage=${ requestScope.pi.currentPage - 1 }">Previous</a>
							</li>
			  			</c:when>
			  			<c:otherwise>
			  				<!-- 검색 목록 조회일 경우 -->
			  				<li class="page-item">
							  <a class="page-link" href="/myweb/board/search?condition=${ requestScope.condition }&keyword=${ requestScope.keyword }&cpage=${ requestScope.pi.currentPage - 1 }">Previous</a>
							</li>
			  			</c:otherwise>
			  			
			  		</c:choose>
			  	</c:otherwise>	
			  </c:choose>	
			  		
			  <c:forEach var="p" begin="${ requestScope.pi.startPage }"
			  					 end="${ requestScope.pi.endPage }" step="1">
			  	
			  	<c:choose>
			  		<c:when test="${ requestScope.pi.currentPage eq p }">
			  			<!-- 
			  				현재 보고 있는 페이지라면 파란색으로 보이게끔 
			  				이 페이지로는 다시 이동 못하게끔 href 속성을 지워줌!!
			  			-->
			  			<li class="page-item active"><a class="page-link">${ p }</a></li>
			  		</c:when>
			  		<c:otherwise>
			  		
			  			<c:choose>
			  				<c:when test="${ empty requestScope.condition }">
			  					<!-- 응답데이터로 condition 이 안넘어왔다면 (그냥 목록 조회일 경우) -->
					  			<li class="page-item">
					  				<a class="page-link" href="/myweb/board/list?cpage=${ p }">${ p }</a>
					  			</li>
			  				</c:when>
			  				<c:otherwise>
			  					<!-- 검색 결과를 보여줘야 할 경우 -->
			  					<li class="page-item">
					  				<a class="page-link" href="/myweb/board/search?condition=${ requestScope.condition }&keyword=${ requestScope.keyword }&cpage=${ p }">${ p }</a>
					  			</li>
			  				</c:otherwise>
			  			</c:choose>
			  		
			  			
			  		</c:otherwise>
			  	</c:choose>				 
			  						 
			  </c:forEach>
			  
			  <c:choose>
			  	<c:when test="${ requestScope.pi.currentPage eq requestScope.pi.maxPage }">
				  <li class="page-item disabled">
				  	<a class="page-link">Next</a>
				  </li>
			  	</c:when>
			  	<c:otherwise>
			  	
			  		<c:choose>
			  			<c:when test="${ empty requestScope.condition }">
			  				<!-- 일반 목록 조회일 경우 -->
			  				<li class="page-item">
								<a class="page-link" href="/myweb/board/list?cpage=${ requestScope.pi.currentPage + 1 }">Next</a>
							</li>
			  			</c:when>
			  			<c:otherwise>
			  				<!-- 검색 결과 조회일 경우 -->
			  				<li class="page-item">
								<a class="page-link" href="/myweb/board/search?condition=${ requestScope.condition }&keyword=${ requestScope.keyword }&cpage=${ requestScope.pi.currentPage + 1 }">Next</a>
							</li>
			  			</c:otherwise>
			  		</c:choose>
			  	</c:otherwise>
			  </c:choose>
			  
			</ul>
			
		</div>

		<br><br>

	</div>
	
	<br><br>

</body>
</html>



