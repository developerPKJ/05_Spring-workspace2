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
	<h1>AJAX 개요</h1>

    <p>
        - Asynchronous JavaScript and XML의 약자로, 웹 페이지에서 서버와 비동기적으로 통신하여
          페이지를 새로 고침하지 않고도 데이터를 주고받을 수 있게 해주는 기술<br>
        - 기존의 a 태그 또는 location.href 또는 form 태그를 통해 요청했던 방식은 동기식 요청<br>
        - 동기식 요청은 응답페이지가 돌아와야 다음 화면 볼 수 있음<br>
        - AJAX는 자바스크립트를 이용하여 서버에 요청을 보내고, 서버로부터 응답데이터만 받아와
          화면에 필요한 부분만 업데이트하는 방식<br>
    </p>

    <br><br>

    동기식/비동기식 <br>
    1. 동기식 : 요청을 보내면 응답이 돌아올 때까지 기다리는 방식<br>
               서버에서 호출된 결과가 지연되면 사용자 입장에서는 답답한 경험이 될 수 있음<br>
               응답페이지가 통으로 돌아오므로 전체 페이지가 리로드됨(새로고침, 깜빡임) <br>
               - 거의 대부분 모든 곳에서 사용

    <br>

    2. 비동기식 : 요청을 보내고 응답이 돌아올 때까지 기다리지 않고 다음 작업을 계속하는 방식<br>
                 사용자 입장에서는 응답 돌아오기 전까지 현재 페이지를 유지하면서 다른 요청 가능<br>
                 응답데이터만 돌아오기 때문에 전체 페이지가 리로드되지 않고 필요한 부분만 업데이트됨<br>
                 - 사용자 아이디 중복체크, 댓글 작성, 검색어 자동완성 등에서 활용<br>

    <br><br>

    비동기식 요청의 단점<br>
    - 현재 페이지에 지속적으로 resource가 쌓임 -> 페이지가 무거워짐
    - 코드상 복잡도 기하급수적으로 증가 -> 유지보수 어려움
    - 응답데이터를 바탕으로 현재 페이지에 새로운 요소를 만들어서 추가해야함 -> DOM 조작 필요

    <br><br>

    AJAX 구현 방식<br>
    1. 순수 자바스크립트 언어를 이용한 방식 : 복잡함 <br>
    2. jQuery 라이브러리를 이용한 방식 : 비교적 간단함 <br>
    3. axios 라이브러리를 이용한 방식 : jQuery와 문법이 유사함 <br>

    <pre>
        jQuery 라이브러리를 이용해 AJAX 요청 보내기

        $.ajax({
            url : '요청보낼 주소',
            method/type : 'GET/POST',                 // 생략 시 기본값은 GET
            data : {key1: value1, key2: value2, ...}, // 서버로 보낼 데이터, 생략 가능
            success : function(responseData) {
                // 요청이 성공했을 때 실행할 코드
                // responseData는 서버로부터 돌아온 응답데이터
            },
            error : function() {
                // 요청이 실패했을 때 실행할 코드
            }
            complete : function() {
                // 요청이 성공하든 실패하든 무조건 실행할 코드
            }
        });

        +)
        * 부수적인 속성 (참고만 할 것)
		- async : 서버와의 비동기 처리 방식 설정 여부 (기본값 true)
		- contentType : request 의 데이터 인코딩 방식 정의 (보내는 측의 데이터 인코딩)
		- dataType : 서버에서 response 로 오는 데이터의 데이터 형 설정, 값이 없다면 스마트하게 판단함
						xml : 트리 형태의 구조
						json : 맵 형태의 데이터 구조 (일반적인 데이터 구조)
						script : javascript 및 일반 String 형태의 데이터
						html : html 태그 자체를 return 하는 방식
						text : String 데이터
		- accept : 파라미터의 타입을 설정 (사용자 특화 된 파라미터 타입 설정 가능)
		- beforeSend : ajax 요청을 하기 전 실행되는 이벤트 callback 함수 (데이터 가공 및 header 관련 설정)
		- cache : 요청 및 결과값을 scope 에서 갖고 있지 않도록 하는 것 (기본값 true)
		- contents : jQuery 에서 response 의 데이터를 파싱하는 방식 정의
		- context : ajax 메소드 내 모든 영역에서 파싱 방식 정의
		- crossDomain : 타 도메인 호출 가능 여부 설정 (기본값 false)
		- dataFilter : response 를 받았을 때 정상적인 값을 return 할 수 있도록 데이터와 데이터 타입 설정
		- global : 기본 이벤트 사용 여부 (ajaxStart, ajaxStop) (버퍼링 같이 시작과 끝을 나타낼 때, 선처리 작업)
		- password : 서버에 접속 권한 (비밀번호) 가 필요한 경우
		- processData : 서버로 보내는 값에 대한 형태 설정 여부 (기본 데이터를 원하는 경우 false 설정)
		- timeout : 서버 요청 시 응답 대기 시간 (milisecond)
    </pre>

    <br><br>

    <hr>

    <h1>jQuery 라이브러리를 이용한 AJAX 테스트</h1>
    <!--
        주의 사항 : jQuery 라이브러리를 사용하기 위해서는 먼저 jQuery를 로드(연동)해야 합니다.
                   jQuery slim 버전은 AJAX 기능이 포함되어 있지 않으므로 주의
    -->

    <h3>1. 버튼 클릭 시 get 방식으로 서버에 데이터 전송 및 응답</h3>
    <!-- 기존 동기식 요청 방식 -->
     <!--
    <form action="/ajax/test1" method="get">
        <input type="text" name="test1" placeholder="데이터 입력">
        <button type="submit">전송</button>
    </form>
    -->

    <!-- AJAX 요청 방식 -->
    <input type="text" id="input1" placeholder="데이터 입력">
    <button type="button" id="btn1">AJAX 전송</button>

    <!-- 
        차이점
        1. form 태그로 감싸지 않음
        2. 전송 버튼이 일반 버튼임
        3. 입력 텍스트상자에 name 속성 대신 id 속성 부여 -> 자바스크립트에서 요소 선택하기 위해
    -->

    <br><br>

    응답 : <label id="output1">현재 응답 없음</label>

    <script>
        $(function() {
            // btn1이 클릭 되었을 때 실행할 코드 작성(ajax 요청)
            $('#btn1').click(function() {
                // loaction.href : '/ajax/test1?test1=' + $("#input1").val()
                // 이렇게 동기식도 가능

                // 비동기식 요청 보내기
                // 페이지가 전환되지 않음 -> 현재 페이지 유지
                $.ajax({
                    url : '/ajax/jqAjax1', // 요청 보낼 주소
                    method : 'GET', // HTTP 메소드
                    data : {input: $("#input1").val()}, // 서버로 보낼 데이터 (key-value 형태)
                    success : function(responseData) {
                        // 요청이 성공했을 때 실행할 코드
                        // responseData는 서버로부터 돌아온 응답데이터
                        $('#output1').text(result);
                    },
                    error : function() {
                        // 요청이 실패했을 때 실행할 코드
                        $('#output1').text('AJAX 요청 실패!');
                    },
                    complete : function() {
                        console.log('AJAX 요청 완료!');
                    }
                });
            });
        });
    </script>

    <br><br>

    <hr>

    <h3>2. 버튼 클릭 시 post 방식으로 서버에 데이터 전송 및 응답</h3>
    이름 : <input type="text" id="input2_1" placeholder="이름 입력">
    나이 : <input type="text" id="input2_2" placeholder="나이 입력">
    <button type="button" id="btn2" onclick="test2();">AJAX 전송</button>

    <br><br>

    응답 : <label id="output2">현재 응답 없음</label>

    <script>
        function test2() {
            let name = $('#input2_1').val();
            let age = $('#input2_2').val();

            $.ajax({
                url : '/ajax/jqAjax2',
                method : 'POST',
                data : {name: name, age: age},
                success : function(result) {
                    $('#output2').text(result);
                },
                error : function() {
                    $('#output2').text('AJAX 요청 실패!');
                },
                complete : function() {
                    console.log('AJAX 요청 완료!');
                }
            });
        }
    </script>

    <br><br>

    <hr>

    <h3>3. </h3>
</body>
</html>