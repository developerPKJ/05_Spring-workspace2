<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	#update-form>table {
		margin : auto;
		width : 90%;
	}

	#update-form input, #update-form textarea, #update-form select {
		padding : 5px;
		margin : 5px;
	}
	#update-form input, #update-form textarea {
		width : 100%;
	}
	#update-form textarea {
		height : 300px;
		resize : none;
	}
</style>
</head>
<body>

	<jsp:include page="../common/menubar.jsp" />

	<div class="outer">
		
		<br>
		<h2 align="center">게시글 수정</h2>
		<br>
		
		<form id="update-form" action="" method="">

			<!--
				- 게시글 수정 시 입력받아야 하는 것들
				  : 카테고리 번호, 제목, 내용
				- 눈에 보이지는 않지만 글번호도 같이 넘겨야함
				  (input type="hidden")
				- 또한, 첨부파일도 변경할 수 있어야 한다!!
			-->
			
			<table class="table">
				<tr>
					<th>카테고리</th>
					<td>
						<select name="category">
							<c:forEach var="c" items="${ requestScope.list }">
								<option value="${ c.categoryNo }">${ c.categoryName }</option>
							</c:forEach>
						</select>
						
						<script>
							$(function() {
								
								// [option, option, option, ...]
								$("#update-form option").each(function() {
									
									if($(this).text() == "${ requestScope.b.category }") {
										
										$(this).prop("selected", true);
									}
								});
							});
						</script>
					</td>
				</tr>
				<tr>
					<th>제목</th>
					<td>
						<input type="text" name="boardTitle" value="${ requestScope.b.boardTitle }" required>
					</td>
				</tr>
				<tr>
					<th>내용</th>
					<td>
						<textarea name="boardContent" required>${ requestScope.b.boardContent }</textarea>
					</td>
				</tr>
				<tr>
					<th>첨부파일</th>
					<td>
						<!-- 선택적으로 기존의 파일이 이미 존재할 경우 -->
						<c:if test="${ not empty requestScope.at }">
							
							<a download="${ requestScope.at.originName }"
							   href="/myweb/${ requestScope.at.filePath }${ requestScope.at.changeName }">
								${ requestScope.at.originName }
							</a>
							
						</c:if>
						
						<input type="file" name="reUpfile">
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









