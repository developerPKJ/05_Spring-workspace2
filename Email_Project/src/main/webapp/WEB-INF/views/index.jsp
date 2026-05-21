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

    <!-- 회원가입 페이지라고 가정 -->

    <br><br>

    이메일 : <input type="email" id="email">
    <button onclick="sendMail();" id="sendMail">인증메일 보내기</button>

    <br><br>

    인증번호 : <input type="text" id="checkNo" disabled>
    <button onclick="validateCheckNo();" id="validate" disabled>인증하기</button>

    <!-- setInterval을 이용해 인증번호 입력란 활성화 시간 제한 -->

    <script>
        function sendMail() {
            $.ajax({
                url: "/email/send",
                method: "POST",
                data: {
                    email: $("#email").val()
                },
                success: function(result) {
                    alert(result);

                    $("#email").prop("readonly", true);
                    $("#sendMail").prop("disabled", true);

                    $("#checkNo").prop("disabled", false);
                    $("#validate").prop("disabled", false);
                },
                error: function() {
                    alert("인증메일 발송 과정에서 알 수 없는 오류가 발생했습니다.");
                }
            });
        }

        function validateCheckNo() {
            $.ajax({
                url: "/email/validate",
                method: "POST",
                data: {
                    email: $("#email").val(),
                    checkNo: $("#checkNo").val()
                },
                success: function(result) {
                    if (result == "인증 성공") {
                        alert(result);

                        $("#checkNo").prop("readonly", true);
                        $("#validate").prop("disabled", true);
                    }
                    else {
                        alert("인증 실패");
                    }
                },
                error: function() {
                    alert("인증번호 검증 과정에서 알 수 없는 오류가 발생했습니다.");
                }
            });
        }
    </script>

</body>
</html>