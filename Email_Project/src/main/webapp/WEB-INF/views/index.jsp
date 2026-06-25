<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- 온라인 방식 -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
</head>
<body>

	<h1>이메일 인증 구현</h1>

	<!-- 회원가입 페이지라고 가정 하고 -->

	<br><br>
	
	이메일 : <input type="email" id="email">
	<button onclick="sendMail();" id="sendMail">인증메일 보내기</button>
	
	<br><br>
	
	인증번호 : <input type="text" id="checkNo" disabled>
	<button onclick="validate();" id="validate" disabled>인증하기</button>

	<!-- setInterval() 로 1초마다 카운트 다운 기능 -->

	<script>
		function validate() {
			
			// 이메일주소와 인증 번호를 서버로 다시 보내서 대조 작업
			$.ajax({
				url : "/email/validate",
				type : "post", 
				data : {
					email : $("#email").val(),
					checkNo : $("#checkNo").val()
				}, 
				success : function(result) {
					
					if(result == "success") {
						// > 대조 성공일 경우
						
						alert("본인 인증에 성공했습니다.");
						
						// 인증 관련 요소들도 다시 disabled (readonly) 상태로 되돌려놓기
						$("#checkNo").prop("readonly", true);
						$("#validate").prop("disabled", true);
						
					} else {
						// > 대조 실패일 경우
						
						alert("본인 인증에 실패했습니다. 다시 진행해 주세요.");
						
						// 인증 관련 요소들도 다시 disabled 상태로 되돌려놓기
						// > 특히, 이미 입력한 인증번호를 초기화까지 시켜줘야함
						$("#checkNo").prop("disabled", true).val("");
						$("#validate").prop("disabled", true);
						
						// 이메일 관련 요소들도 다시 활성화 상태로 되돌리기
						// > 마찬가지로 이미 입력했던 이메일 주소도 초기화 해줘야함
						$("#email").prop("disabled", false).val("");
						$("#sendMail").prop("disabled", false);
						
					}
					
				},
				error : function() {
					
					console.log("인증번호 대조용 ajax 통신 실패!");
				}
			});
		}
	
		function sendMail() {
			
			// 인증 번호를 이메일로 전송할 수 있도록 요청
			$.ajax({
				url : "/email/send",
				type : "post",
				data : {
					email : $("#email").val()
				},
				success : function(result) {
					
					alert(result);
					
					// 인증번호 발급 후 이메일 관련 요소들은 비활성화
					$("#email").prop("readonly", true);
					$("#sendMail").prop("disabled", true);
					
					// 인증 관련 요소들은 활성화
					$("#checkNo").prop("disabled", false);
					$("#validate").prop("disabled", false);
					
				},
				error : function() {
					
					console.log("인증메일 발송용 ajax 통신 실패!");
				}
			});
		}
	</script>
	

</body>
</html>



