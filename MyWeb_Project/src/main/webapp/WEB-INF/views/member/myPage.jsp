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
						class="btn btn-warning btn-sm"
						data-toggle="modal" data-target="#updatePwdForm">비밀번호변경</button>
				<button type="button"
						class="btn btn-danger btn-sm"
						data-toggle="modal" data-target="#deleteForm">회원탈퇴</button>
			</div>

		</form>

		<br><br>

	</div>

	<br><br>
	
	<!-- 비밀번호변경과 회원탈퇴는 모달창을 이용해서 구현해볼 예정!! -->
	
	<!-- 회원탈퇴용 모달창 -->
	<!-- The Modal -->
	<div class="modal" id="deleteForm">
	  <div class="modal-dialog">
	    <div class="modal-content">
	
	      <!-- Modal Header -->
	      <div class="modal-header">
	        <h4 class="modal-title">회원 탈퇴</h4>
	        <button type="button" class="close" data-dismiss="modal">&times;</button>
	      </div>
	
	      <!-- Modal body -->
	      <div class="modal-body" align="center">
	      
	      	<b>
	      		탈퇴 후 복구가 불가능합니다. <br>
	      		정말로 탈퇴하시겠습니까? <br><br>
	      	</b>
	      
	      	<!-- 
	      		* 회원 탈퇴 기능 구현
	      		- 현재 이 회원의 비밀번호를 입력 후 탈퇴하기 버튼 클릭 시
	      		  http://localhost:8006/myweb/member/delete 로 POST 방식으로 요청
	      	-->
	      	<form action="/myweb/member/delete" method="post">
	      		
				<!-- 
					* 회원 탈퇴 요청 시 필요한 데이터
					- 현재 이 회원의 비밀번호
				-->	      		
				
				<table>
					<tr>
						<th>비밀번호</th>
						<td>
							<input type="password" name="userPwd" required>
						</td>
					</tr>
				</table>
				
				<br><br>
				
				<div align="center">
					<button type="submit" class="btn btn-danger btn-sm">
						탈퇴하기
					</button>
				</div>
	      		
	      	</form>
	      	
	      </div>
	
	    </div>
	  </div>
	</div>
	
	<!-- 비번변경용 모달창 -->
	<!-- The Modal -->
	<div class="modal" id="updatePwdForm">
	  <div class="modal-dialog">
	    <div class="modal-content">
	
	      <!-- Modal Header -->
	      <div class="modal-header">
	        <h4 class="modal-title">비밀번호 변경</h4>
	        <button type="button" class="close" data-dismiss="modal">&times;</button>
	      </div>
	
	      <!-- Modal body -->
	      <div class="modal-body" align="center">
	        
	        <!-- 
	        	* 비밀번호 변경 기능 구현
	        	
	        	- 사용자가 현재 비밀번호, 변경할 비밀번호, 변경할 비밀번호 재입력을 입력하고 나서
	        	  비밀번호 변경 버튼을 클릭 시
	        	  
	        	  http://localhost:8006/myweb/member/updatePwd 로 POST 방식으로 요청
	        -->
	        <form action="/myweb/member/updatePwd" method="post">
	        	
	        	<!-- 
	        		* 비밀번호 변경 시 입력받아야 되는 것들
	        		- 현재 비밀번호, 변경할 비밀번호, 변경할 비밀번호 재입력
	        		- 누구의 비번을 변경할건지 해당 회원의 아이디도 필요하긴 함!!
	        		  (아이디를 input type="hidden" 을 통해 눈에 보이지 않게 같이 몰래 넘기는 것)
	        	-->
	        	
	        	<input type="hidden" name="userId" value="${ sessionScope.loginUser.userId }">
	        	
	        	<table>
	        		<tr>
	        			<th>현재 비밀번호</th>
	        			<td>
	        				<input type="password" name="userPwd" required>
	        			</td>
	        		</tr>
	        		<tr>
	        			<th>변경할 비밀번호</th>
	        			<td>
	        				<input type="password" name="updatePwd" required>
	        			</td>
	        		</tr>
	        		<tr>
	        			<th>변경할 비밀번호 재입력</th>
	        			<td>
	        				<input type="password" name="checkPwd" required>
	        			</td>
	        		</tr>
	        	</table>
	        	
	        	<br><br>
	        	
	        	<div align="center">
	        		<button type="submit" class="btn btn-secondary btn-sm"
	        				onclick="return validatePwd();">
	        			비밀번호 변경
	        		</button>
	        	</div>
	        	
	        </form>
	        
	        <script>
	        	// 변경할 비밀번호 유효성 검사용 함수
	        	// > 변경할 비밀번호와 변경할 비밀번호 재입력이 일치할 경우 true, 아닐 경우 false 반환
	        	function validatePwd() {
	        		
	        		// 우선 변경할 비밀번호와 변경할 비밀번호 재입력값을 변수에 담아오기
	        		let updatePwd = $("input[name=updatePwd]").val();
	        		let checkPwd = $("input[name=checkPwd]").val();
	        		
	        		// console.log(updatePwd, checkPwd);
	        		
	        		// 두 값이 일치하면 기본이벤트를 살리고, 두 값이 일치하지 않으면 기본이벤트를 제거할 것
	        		if(updatePwd != checkPwd) {
	        			
	        			alertify.alert("비밀번호가 일치하지 않습니다.");
	        			
	        			return false;
	        		}
	        		
	        		return true;
	        	}
	        </script>
	        
	      </div>
	
	    </div>
	  </div>
	</div>
	

</body>
</html>





