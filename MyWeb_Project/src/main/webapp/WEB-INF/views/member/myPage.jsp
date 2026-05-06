<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	.outer {
		width : 1000px;
		border : 1px dotted lightgray;
		margin : auto;
		margin-top : 50px;
	}

	#mypage-form table {
		margin : auto;
	}

	#mypage-form input {
		padding : 5px;
		width : 300px;
		margin : 5px;
	}
</style>
</head>
<body>

	<!-- 상단에 menubar 부터 넣기 -->
	<jsp:include page="../common/menubar.jsp" />
	
	<div class="outer">
	
		<br>
		<h2 align="center">마이페이지</h2>
		<br>

		<!-- 
			* 내 정보 수정 기능 구현
			
			- 변경할 이름, 전화번호, 이메일, 주소를 입력받은 후 정보변경 버튼을 클릭하는 순간
			
			http://localhost:8006/myweb/member/update 로 POST 방식으로 요청
		-->
		<form id="mypage-form" action="/myweb/member/update" method="post">

			<!--
				* 마이페이지에서 보여질, 수정될 내용들

				- 아이디, 이름, 전화번호, 이메일, 주소
				- 단, 아이디는 수정 안되게끔 막기!!
				  (아이디는 회원 식별 용도의 데이터임 - 불변성)
				- 또한 비밀번호는 조회 X, 여기에서 수정 X
				  (비밀번호 변경 기능은 별도로 뺄 예정)
			-->

			<table>
				<tr>
					<th>* 아이디</th>
					<td>
						<input type="text" name="userId" maxlength="12" readonly
							  			   value="${ sessionScope.loginUser.userId }" required>
					</td>
					<td>
					</td>
				</tr>
				<tr>
					<th>* 이름</th>
					<td>
						<input type="text" name="userName" maxlength="6" 
										   value="${ sessionScope.loginUser.userName }" required>
					</td>
					<td></td>
				</tr>
				<tr>
					<th>&nbsp;&nbsp;&nbsp;전화번호</th>
					<td>
						<input type="text" name="phone" 
										   value="${ sessionScope.loginUser.phone }" placeholder="- 포함해서 입력">
					</td>
					<td></td>
				</tr>
				<tr>
					<th>&nbsp;&nbsp;&nbsp;이메일</th>
					<td>
						<input type="email" name="email"
											value="${ sessionScope.loginUser.email }">
					</td>
					<td></td>
				</tr>
				<tr>
					<th>&nbsp;&nbsp;&nbsp;주소</th>
					<td>
						<input type="text" name="address"
										   value="${ sessionScope.loginUser.address }">
					</td>
					<td></td>
				</tr>
			</table>

			<br><br>

			<div align="center">
				<button type="submit"
						class="btn btn-primary btn-sm">정보변경</button>
				<button type="button"
						class="btn btn-warning btn-sm">비밀번호변경</button>
				<button type="button"
						class="btn btn-danger btn-sm">회원탈퇴</button>
			</div>

		</form>

		<br><br>

	</div>

	<br><br>

</body>
</html>