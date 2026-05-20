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
					<c:choose>
						<c:when test="${ not empty sessionScope.loginUser }">
							<!-- case1. 로그인이 되어있을 경우 - 댓글 작성이 가능해짐 -->
							<tr>
								<th>댓글작성</th>
								<td>
									<textarea id="replyContent"></textarea>
								</td>
								<td>
									<button class="btn btn-secondary btn-sm" 
											onclick="insertReply();">
										댓글등록
									</button>
								</td>
							</tr>
						</c:when>
						<c:otherwise>
							<!-- case2. 로그인이 되어있지 않은 경우 - 댓글 작성이 불가 -->
							<tr>
								<th>댓글작성</th>
								<td>
									<textarea readonly>로그인 후 이용 가능한 서비스입니다.</textarea>
								</td>
								<td>
									<button class="btn btn-secondary btn-sm" disabled>
										댓글등록
									</button>
								</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</thead>
				<tbody>
				</tbody>
			</table>

		</div>

		<br><br>

	</div>
	
	<br><br>
	
	<script>
		$(function() {
			
			// 브라우저 상에 이 페이지가 다 로딩된 후에 단 한번 전체 댓글 목록을 조회해서 갖고올 것!!
			selectReplyList();
			
			setInterval(selectReplyList, 1000);
			// > 댓글을 비동기식으로 조회해주는 selectReplyList 함수를
			//   일정 시간마다 주기적으로 실행시켜주기!! (실시간 효과)
			
		});
	
		// 댓글 목록 조회용 함수
		function selectReplyList() {
			
			$.ajax({
				url : "/myweb/board/rlist",
				type : "get",
				data : {
					boardNo : ${ requestScope.b.boardNo }
				}, 
				success : function(result) {
					
					// console.log(result);
					// [{}, {}, {}, ...]
					
					let resultStr = "";
					
					// for(let i = 0; i < result.length; i++) {
					for(let i in result) {	
						
						// console.log(result[i]);
						
						resultStr += "<tr>"
								   +		"<td>" + result[i].replyWriter + "</td>"
								   +		"<td>" + result[i].replyContent + "</td>"
								   +		"<td>" + result[i].createDate + "</td>"
								   + "</tr>";
					}
					
					$("#reply-area tbody").html(resultStr);
					
				},
				error : function() {
					
					console.log("댓글 목록 조회용 ajax 통신 실패!");
				}
			});
		}
	
		// 댓글 등록용 함수
		function insertReply() {
			
			// 사용자가 입력한 댓글 내용을 변수에 담기
			let replyContent = $("#replyContent").val();
			
			// 댓글 등록 요청 시
			// http://localhost:8006/myweb/board/rinsert 로 POST 방식으로 요청
			// 이 때, 참조게시글번호, 댓글내용만 넘길 것임!!
			$.ajax({
				url : "/myweb/board/rinsert",
				type : "post",
				data : {
					refBoardNo : ${ requestScope.b.boardNo },
					replyContent : replyContent
				},
				success : function(result) {
					
					if(result == "success") {
						// > 성공
						
						// 갱신된 댓글 목록을 재조회
						selectReplyList();
						
						// 댓글 작성용 textarea 초기화
						$("#replyContent").val("");
						
					} else {
						// > 실패
						
						alert("댓글 작성에 실패했습니다.");
						
						$("#replyContent").val("");
					}
				},
				error : function() {
					
					console.log("댓글 작성용 ajax 통신 실패!");
				}
			});
			
		}
	</script>

</body>
</html>



