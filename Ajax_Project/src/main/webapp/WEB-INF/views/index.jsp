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
		- Asynchronous Javascript And XML 의 약자로, 서버로부터 요청 후 응답 "데이터" 만 가져와
		  전체 페이지를 새로 고치지 않고 일부만 로드할 수 있도록 해주는 기법 <br>
		- 기존에 우리가 a 태그 또는 location.href 또는 form 태그를 통해 요청했던 방식은 "동기식" 요청이였음!! <br>
		- 동기식 요청은 "응답페이지" 가 돌아와야 그 다음 화면을 볼 수 있음!!
		  응답페이지가 조금 늦게 돌아온다면 다 돌아올 때 까지 사용자는 흰 화면만 볼 수 있음!!
		  (그래서 응답페이지가 보여지는 과정에서 브라우저 화면이 깜빡거림) <br>
		- 웹 기능에 따라 화면이 깜빡거리지 않고 응답을 받아야하는 기능을 구현하려면 "비동기식" 요청을 보내야 하고,
		  그러기 위해서는 AJAX 라는 기술이 필요하다.
		
		<br><br>
		
		* 동기식 / 비동기식 <br>
		1. 동기식 : 요청 처리 후 그에 해당하는 응답페이지가 돌아와야만 그 다음 작업이 가능해짐 <br>
				  만약, 서버에서 호출된 결과까지의 시간이 지연된다면 (즉, 응답페이지가 돌아오는데 시간이 걸린다면)
				  사용자는 계속 흰 화면만 보면서 무작정 계속 기다려야함!! <br>
				  응답페이지가 통으로 돌아오기 때문에 전체 페이지가 리로드됨 (새로고침, 페이지 전환 시 깜빡거린다) <br>
				  예) 거의 대부분의 모든 기능 구현 시 기본적으로 쓰임
		<br>
		
		2. 비동기식 : 요청을 보내더라도 현재 사용자가 보는 페이지를 그대로 유지하면서 요청이 들어감 <br>
				    사용자 입장에서는 응답이 돌아오기 전까지 현재 페이지를 유지하면서 중간 중간 마다 추가적인 다른 요청 또한
				    보낼 수 있게 됨!! <br>
				    (요청을 보내놓고 그에 해당하는 응답이 돌아올 때 까지 다른 작업을 할 수 있게 된다.) <br>
				    또한, 응답 데이터만 돌아오기 때문에 페이지가 깜빡거리지 않으면서도 돌려받은 응답데이만 화면 상에
				    살짝 반영도 가능해진다!! <br>
				    예) 회원가입 시 아이디 중복체크 기능, 
				       댓글 목록 조회 기능, 댓글 작성 기능 => 수업시간에 직접 만들어 볼 것
				       <br>
				       실시간 검색어 랭크 기능, 검색어 자동완성 기능, 메인페이지 TOP-N 분석 기능, 좋아요 기능 등
				       
		<br><br>
		
		* 비동기식 요청의 단점 <br>
		- 현재 페이지에 지속적으로 리소스가 쌓임 => 페이지가 현저히 느려질 수 있음 <br>
		- 코드 상의 복잡도가 기하급수적으로 증가 => 에러 발생 시 디버깅이 어려움 <br>
		- 요청 후 돌아온 응답데이터를 가지고 현재 페이지에서 새로운 요소를 만들어서 뿌려줘야 함
		  => DOM 요소를 새로이 만들어서 찍어내는 구문을 정확히 익혀둬야 함!!
		  
		<br><br>
		
		* AJAX 구현 방식 <br>
		1. 순수 JavaScript 언어를 이용한 방식 : 복잡하고 어려움 <br>
		2. jQuery 라이브러리를 이용한 방식 : 간결하고 사용하기 쉬움 - 수업시간에 배워 볼 내용 <br>
		3. axios 라이브러리를 이용한 방식 : jQuery 방식과 문법이 거의 비슷 - react 수업때 사용해볼 예정
	</p>
	
	<pre>
		* jQuery 라이브러리를 이용해서 AJAX 요청 보내기
		
		[ 표현법 ]
		
		$.ajax({
			속성명 : 속성값,
			속성명 : 속성값,
			...
		});
		
		- $.ajax 라는 jQuery 메소드를 호출하고 이 때, 인자로 {} (자바스크립트 객체) 를 넘겨준다!! 
		- 인자 객체 내부에 속성명 : 속성값들을 이용해서 요청과 응답에 대한 정보를 기술한다.
		
		* 주요 속성 (반드시 암기할 것!!)
		- url : 요청할 url 주소 (필수로 작성해야 하는 속성임)
			    즉, 어디로 요청을 보낼꺼냐 적는 부분 (form 태그로 따지면 action 속성의 역할)
		- type 또는 method : 요청 전송 방식 (get / post)
							생략 가능, 생략 시 기본값은 get 방식임!! (form 태그로 따지면 method 속성의 역할)
		- data : 요청 시 전달할 값들을 기술하는 부분
				 키 + 밸류 세트로 "객체" 형식으로 나열한다.
				 요청 시 전달값이 딱히 없다면 생략 가능한 속성임!! (기존 form 태그 내의 입력양식 관련 요소의 역할)
		- success : ajax 요청 성공 시 실행할 코드를 정의하는 메소드 속성
		- error : ajax 요청 실패 시 실행할 코드를 정의하는 메소드 속성
		- complete : ajax 요청을 성공하든 실패하든 간에 무조건 실행할 코드를 정의하는 메소드 속성
		
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
		* 명심할 사항 : jQuery 라이브러리 연동은 필수!! 
					 jQuery slim 버전에는 $.ajax() 메소드가 정의되어있지 않음 (slim 버전 사용 불가)
	-->
	
	<h3>1. 버튼 클릭 시 get 방식으로 서버에 데이터 전송 및 응답</h3>
	
	<!-- 기존의 동기식 요청 방식 -->
	<!--  
	<form action="/ajax/test1" method="get">
	
		입력 : <input type="text" name="test1">
		<button type="submit">전송</button>
		
	</form>
	-->
	
	<!-- ajax 를 이용한 비동기식 요청 방식 -->
	입력 : <input type="text" id="input1">
	<button type="button" id="btn1">전송</button>
	
	<!-- 
		- 기존 동기식 요청 방식과의 차이점
		1. form 태그로 감싸지 않음
		2. 전송버튼이 일반버튼임
		3. 입력 텍스트상자에 name 으로 키값을 부여하지 않음
	-->
	
	<br><br>
	
	응답 : <label id="output1">현재 응답 없음</label>
	
	<script>
		$(function() {
			
			// btn1 이 클릭 되었을 때 ajax 요청을 보내볼 것
			$("#btn1").click(function() {
				
				// 굳이 form 태그를 안쓰더라도 동기식 요청을 보낼 수 있긴 함!!
				// location.href = "/ajax/test1?test1=" + $("#input1").val();

				// 비동기식 요청
				// > 페이지가 전환되지 않음!! 현재 페이지가 그대로 보임!!
				//   (즉, 브라우저의 url 주소창의 주소가 변하지 않음)
				
				$.ajax({
					url : "/ajax/jqAjax1",
					type : "get",
					data : { input : $("#input1").val() },
					success : function(result) {
						
						console.log("ajax 통신 성공!!");
						
						// console.log("응답데이터 : " + result);
						// > Ajax 형식으로 요청을 보냈다면 
						//   그 응답데이터는 success function 의 매개변수로 받아낼 수 있다!!
						
						$("#output1").text(result);
						
					},
					error : function() {
						
						console.log("ajax 통신 실패!!");
					},
					complete : function() {
						
						console.log("ajax 통신 성공이든 실패든 간에 무조건 실행!!");
					}
				});
				// > ajax 또한 마찬가지로 요청을 받아줄 Controller 가 없다면 404 에러 발생!!
				
			});
			
		});
	</script>
	
	<br><br>
	
	<hr>
	
	<h3>2. 버튼 클릭 시 post 방식으로 서버에 데이터 전송 및 응답</h3>

	이름 : <input type="text" id="input2_1"> <br>
	나이 : <input type="number" id="input2_2"> <br>
	<button type="button" onclick="test2();">전송</button>

	<br><br>
	
	응답 : <label id="output2">현재 응답 없음</label>

	<script>
		function test2() {
			
			// 우선 사용자가 입력한 값들을 변수에 먼저 담기
			let name = $("#input2_1").val();
			let age = $("#input2_2").val();
			
			// 이 값들을 넘기면서 ajax 요청
			$.ajax({
				url : "/ajax/jqAjax2",
				type : "post",
				data : { name : name, age : age }, 
				success : function(result) {
					
					console.log("ajax 통신 성공!!");
					
					// console.log(result);
					
					$("#output2").text(result);
				},
				error : function() {
					
					console.log("ajax 통신 실패!!");
				},
				complete : function() {
					
					console.log("ajax 통신 성공이든 실패든 간에 무조건 실행!!");
				}
			});
			
		}
	</script>
	
	<br><br>
	
	<hr>
	
	<h3>3. 응답데이터가 여러개 넘어올 경우</h3>
	
	수학 : <input type="number" id="input3_1"> <br>
	영어 : <input type="number" id="input3_2"> <br>
	
	<button type="button" onclick="test3();">전송</button>
	
	<br><br>
	
	결과 : 총합은 <label id="output3_1">0</label>, 
		  평균은 <label id="output3_2">0</label> 점 입니다.

	<script>
		function test3() {
			
			// 사용자가 입력한 값들을 먼저 변수에 담기
			let math = $("#input3_1").val();
			let eng = $("#input3_2").val();
			
			// ajax 로 요청 보내기
			$.ajax({
				url : "/ajax/jqAjax3",
				type : "get",
				data : { 
					math : math, 
					eng : eng 
				},
				success : function(result) {
					
					console.log(result);
					// > 18592.0 (여러개의 응답데이터가 한줄로 연이어져서 도착)
					// > 185, 92.0 (육안상으로는 구분 되지만, 사실 하나의 문자열로 넘어옴)
					//   split 메소드 같은 걸로 데이터를 분리해서 쓸 수는 있지만 귀찮고 복잡해짐!!
					
					// > [185, 92.0] : JSONArray 형식으로 제대로 넘겨받았을 경우
					// $("#output3_1").text(result[0]);
					// $("#output3_2").text(result[1]);
					
					// > {total : 185, avg : 92.0} : JSONObject 형식으로 제대로 넘겨받았을 경우
					$("#output3_1").text(result.total); // 객체명.속성명
					$("#output3_2").text(result["avg"]); // 객체명["속성명"]
					
				}, 
				error : function() {
					
					console.log("ajax 통신 실패!!");
				}
			});
		}	  
	</script>	
	
	<br><br>
	
	<hr>
	
	<!-- 여기서부터가 진짜!! -->
	<h3>4. 서버로 데이터 전송 후, 조회된 "객체" 를 응답데이터로 받기</h3>
	
	<!-- 
		즉, DB 로 부터 "단일행 조회" 한 결과를 응답데이터로 받아보기
		
		예) Member VO 클래스를 하나 만들고, 
		   회원번호를 입력받아서 Controller 로 전달 후 DB 로 부터 조회해왔다 라는 가정 하에
		   Member VO 객체 하나를 응답데이터로 넘겨볼 것!!
	-->
	
	회원번호 : <input type="number" id="input4"> 
	
	<button onclick="test4();">조회</button>
	<!-- submit 버튼은 form 태그 밖에 있는 순간 그 역할을 다 못함!! (앞으로 생략해볼 것) -->
	
	<br><br>
	
	<div id="output4"></div>
	
	<script>
		function test4() {
			
			// 입력받은 회원번호 먼저 변수에 담기
			let userNo = $("#input4").val();
			
			$.ajax({
				url : "/ajax/jqAjax4",
				type : "get",
				data : {
					userNo : userNo
				},
				success : function(result) {
					
					console.log(result);
					
					/*
					console.log(result.userNo);
					console.log(result.userName);
					console.log(result["age"]);
					console.log(result["gender"]);
					*/
					
					let resultStr = "회원번호 : " + result.userNo
								  + "<br>"
								  + "이름 : " + result.userName
								  + "<br>"
								  + "나이 : " + result.age
								  + "<br>"
								  + "성별 : " + result.gender;
					
					$("#output4").html(resultStr);
					
				}, 
				error : function() {
					
					console.log("ajax 통신 실패!");
				}
			});
		}
	</script>
	
	<br><br>
	
	<hr>
	
	<h3>5. 응답데이터로 여러개의 객체들이 담겨있는 ArrayList 받기</h3>
	
	<!-- 
		즉, DB 로 부터 "여러행 조회" 된 결과를 응답데이터로 받아보기
		
		예) 관리자페이지에서 회원 전체 목록 조회 기능을 만든다고 가정하고
		    회원들의 데이터가 모두 담겨있는 ArrayList<Member> 를 응답데이터로 받아보자
	-->
	
	<button onclick="test5();">회원 전체 조회</button>
	
	<br><br>
	
	<table id="output5" border="1">
		<thead>
			<tr>
				<th>회원번호</th>
				<th>회원명</th>
				<th>나이</th>
				<th>성별</th>
			</tr>
		</thead>
		<tbody></tbody>
	</table>
	
	<script>
		function test5() {
			
			$.ajax({
				url : "/ajax/jqAjax5",
				type : "post",
				success : function(result) {
					
					console.log(result); 
					// > [{}, {}, {}, ..]
					
					let resultStr = "";
					
					for(let i = 0; i < result.length; i++) {
						
						// console.log(result[i].userName);
						
						resultStr += "<tr>"
								   + 		"<td>" + result[i].userNo + "</td>"
								   + 		"<td>" + result[i].userName + "</td>"
								   + 		"<td>" + result[i].age + "</td>"
								   + 		"<td>" + result[i].gender + "</td>"
								   + "</tr>";
						
					}
					
					$("#output5 tbody").html(resultStr);
					
				},
				error : function() {
					
					console.log("ajax 통신 실패!");
				}
			});
			
		}
	</script>
	
</body>
</html>







