<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	/*
	.outer {
		width : 1000px;
		border : 1px dotted lightgray;
		margin : auto;
		margin-top : 50px;
	}
	*/

	#enroll-form table {
		margin : auto;
	}

	#enroll-form input {
		padding : 5px;
		margin : 5px;
		width : 300px;
	}

</style>
</head>
<body>

	<!-- 모든 페이지마다 상단에는 menubar.jsp 가 include 되야함!! -->
	
	<jsp:include page="../common/menubar.jsp" />
	<!-- ../ : 현재 이 폴더로부터 한겹 빠져나가겠다. -->
	
	<!-- 여기서부터 실제 회원가입 페이지 화면 짜기 -->
	<div class="outer">
	
		<br>
		<h2 align="center">회원가입</h2>
		<br>

		<!-- 
			* 회원가입 기능 구현
			- 아이디 ~ 주소까지 입력 후 회원가입 버튼 클릭 시
			
			http://localhost:8006/myweb/member/insert 로 요청 (POST 방식으로)
			
			- 단, 회원가입 버튼 클릭 시 곧바로 요청이 들어가면 안됨!!
			  validate 함수를 거쳐가야함!! (사용자가 입력한 값들이 유효한지 정규표현식 등으로 검사)
		-->
		<form id="enroll-form" action="/myweb/member/insert" method="post">

			<!--
				* 회원가입 시 입력받아야 하는 것들
				- 아이디, 비번, 이름, 전화번호, 이메일, 주소
				- 아이디, 비번, 이름은 "필수입력사항"
			-->

			<table>
				<tr>
					<th>* 아이디</th>
					<td>
						<input type="text" name="userId" maxlength="12" required>
					</td>
					<td>
						<button type="button" onclick="idCheck();"
								class="btn btn-secondary btn-sm">중복확인</button>
						<!-- 중복확인은 나중에 AJAX 배우고 나서 구현!! -->
					</td>
				</tr>
				<tr>
					<th>* 비밀번호</th>
					<td>
						<input type="password" name="userPwd" maxlength="15" required>
					</td>
					<td></td>
				</tr>
				<tr>
					<th>* 비밀번호 확인</th>
					<td>
						<input type="password" maxlength="15" required>
						<!-- 어차피 비밀번호값이랑 일치하면 한번만 서버로 넘기면 되므로 name 속성은 생략 -->
					</td>
					<td></td>
				</tr>
				<tr>
					<th>* 이름</th>
					<td>
						<input type="text" name="userName" maxlength="6" required>
					</td>
					<td></td>
				</tr>
				<tr>
					<th>&nbsp;&nbsp;&nbsp;전화번호</th>
					<td>
						<input type="text" name="phone" placeholder="- 포함해서 입력">
					</td>
					<td></td>
				</tr>
				<tr>
					<th>&nbsp;&nbsp;&nbsp;이메일</th>
					<td>
						<input type="email" name="email">
					</td>
					<td></td>
				</tr>
				<tr>
					<th>&nbsp;&nbsp;&nbsp;주소</th>
					<td>
						<input type="text" name="address">
					</td>
					<td></td>
				</tr>
			</table>

			<br><br>

			<div align="center">
				<button type="submit" class="btn btn-primary btn-sm" disabled>회원가입</button>
				<!-- 아이디 중복확인이 정확히 들어가기 전에는 회원가입버튼을 클릭하지 못하게 막음 -->
				<button type="reset" class="btn btn-secondary btn-sm">초기화</button>
			</div>

		</form>

		<br><br>

	</div>

	<br><br>
	
	<script>
		function idCheck() {
			
			// 아이디를 입력받는 input 요소 객체를 변수에 담기
			let $userId = $("#enroll-form input[name=userId]");
			// > $("input[name=userId]"); 이렇게 선택하면 상단에 include 된 menubar.jsp 의
			//   로그인 폼 내의 아이디 입력창 또한 같이 선택되게 됨!!
			// > 내가 원하는 요소만 정확하게 하나만 고르고 싶다면
			//   확실하게 어디에 속해있는 요소인지 잘 구분해서 선택해야 한다!!
			
			// 아이디 중복 체크 요청 시
			// http://localhost:8006/myweb/member/idCheck?checkId=xxx
			// 로 비동기식 요청
			
			$.ajax({
				url : "/myweb/member/idCheck",
				type : "get",
				data : { checkId : $userId.val() },
				success : function(result) {
					
					// console.log(result);
					
					if(result == "NNNNN") {
						// > 사용 불가한 아이디일 경우
						
						alert("이미 사용중이거나 탈퇴한 회원의 아이디입니다.");
						
						// 아이디 재입력 유도
						$userId.focus();
						
					} else {
						// > 사용 가능한 아이디일 경우
						
						if(confirm("사용 가능한 아이디입니다. 사용하시겠습니까?")){
							// > 사용하겠다고 의사를 밝힌 경우 (확인 버튼 클릭 시)
							
							// 아이디값을 확정 (다시는 수정 못하게)
							$userId.prop("readonly", true);
							
							// 회원가입 버튼 활성화
							$("#enroll-form button[type=submit]").removeAttr("disabled");
							
						} else {
							// > 사용하지 않겠다고 의사를 밝힌 경우 (취소 버튼 클릭 시)
							
							// 아이디 재입력 유도
							$userId.focus();
						}
					}
				},
				error : function() {
					
					console.log("아이디 중복체크용 ajax 통신 실패!");
				}
			});
			
		}
	</script>

</body>
</html>




