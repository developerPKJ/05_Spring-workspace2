<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
	#enroll-form table {
		width : 90%;
		margin : auto;
	}

	#enroll-form input, #enroll-form textarea {
		width : 100%;
		padding : 5px;
		margin : 5px;
	}
	#enroll-form textarea {
		height : 300px;
		resize : none;
	}
</style>
</head>
<body>

	<jsp:include page="../common/menubar.jsp" />

	<div class="outer">
		
		<br>
		<h2 align="center">사진게시글 작성</h2>
		<br>
		
		<!-- 
			* 사진게시글 작성 기능 구현
			- 제목, 내용, 첨부파일 정보들을 입력하고 등록하기 버튼 클릭 시
			
			  http://localhost:8006/myweb/thumbnail/insert 로 POST 방식으로 요청
			
			  (또한, 요청 시 전달값들 중 첨부파일들이 있기 때문에
			   enctype="multipart/form-data" 까지 기술해야함!!)
		-->
		
		<form id="enroll-form" action="/myweb/thumbnail/insert" method="post"
							   enctype="multipart/form-data">

			<!--
				- 사진게시글 작성 시 입력받아야 할 것들
				  : 제목, 내용, 첨부파일 정보들 (다중파일업로드)
				- 눈에 보이지는 않지만 현재 로그인한 회원 (즉, 작성자) 의
				  회원 번호 또한 필요함!! (input type="hidden" 으로 넘기기)
			-->

			<input type="hidden" name="boardWriter" value="${ sessionScope.loginUser.userNo }">

			<table class="table">
				<tr>
					<th>제목</th>
					<td colspan="3">
						<input type="text" name="boardTitle" required>
					</td>
				</tr>
				<tr>
					<th>내용</th>
					<td colspan="3">
						<textarea name="boardContent" required></textarea>
					</td>
				</tr>
				<tr>
					<!--
						- 이미지 파일들을 입력받으려면 input type="file" 을 써야 하는데
						  입력 form 내부에 img 태그로 표현했음!!
						- 이미지 미리보기 기능을 구현해볼까 함!! (자바스크립트 이용)
						- 그래도 입력은 form 태그 내에서 input type="file" 로 받긴 해야함!!
					-->
					<th>대표이미지</th>
					<td colspan="3" align="center">
						<img id="titleImg" width="250" height="170">
					</td>
				</tr>
				<tr>
					<th>상세이미지</th>
					<td>
						<img id="contentImg1" width="150" height="110">
					</td>
					<td>
						<img id="contentImg2" width="150" height="110">
					</td>
					<td>
						<img id="contentImg3" width="150" height="110">
					</td>
				</tr>
			</table>

			<!-- 
				이 자리에 실질적으로 파일을 입력해줄 input type="file" 을 배치할 것 
				(우리는 게시글 하나 당 첨부파일을 최대 4개까지 입력받기로 함!!)

				- file1 ~ file4 까지 입력받은 이미지 파일을
				  위의 img 태그로 미리볼 수 있도록 연결해줄 예정!!
				- file1 ~ file4 까지 change 이벤트를 부여할 것!! (onchange 속성 이용)
				  : change 이벤트는 input 요소의 내용이 변경되었을 때 발생하는 이벤트
				    (change 이벤트가 발생할 때 마다 실행할 함수를 정의)
			-->
			<div id="file-area">
				<!-- input[type=file id=file$ name=file$]*4 + Enter -->
				<input type="file" id="file1" name="files" onchange="loadImg(this, 1);" required> <!-- #titleImg 와 연결 - 대표이미지(썸네일) 은 필수 -->
				<input type="file" id="file2" name="files" onchange="loadImg(this, 2);"> <!-- #contentImg1 과 연결 -->
				<input type="file" id="file3" name="files" onchange="loadImg(this, 3);"> <!-- #contentImg2 와 연결 -->
				<input type="file" id="file4" name="files" onchange="loadImg(this, 4);"> <!-- #contentImg3 과 연결 -->
				<!-- 매번 loadImg 를 호출할 때 마다 this(이 요소 자체) 와 자릿수를 나타내는 숫자갑을 같이 넘김!! -->
			</div>
			
			<script>
				$(function() {
					
					// 우선 #file-area 영역을 눈에 안보이게 숨김처리
					$("#file-area").hide(); // display : none; 효과를 줌
					
					// 그대신 각 img 마다 클릭 시 해당 자리에 맞는 input type="file" 을 띄워줄 것
					$("#titleImg").click(function() {
						
						$("#file1").click();
						// > jQuery 의 이벤트함수를 function 없이 호출하면
						//   역으로 이벤트를 일으키는 효과를 줌!! (getter)
						// > function 있이 호출하면 이벤트를 부여하는 효과를 줌!! (setter)
					});
					
					$("#contentImg1").click(function() {
						
						$("#file2").click();
					});
					
					$("#contentImg2").click(function() {
						
						$("#file3").click();
					});
					
					$("#contentImg3").click(function() {
						
						$("#file4").click();
					});
					
				});
			
				function loadImg(inputFile, num) {
					
					// console.log("change 이벤트 발생!!");
					
					// inputFile : 현재 change 이벤트가 발생된 input type="file" 요소 객체 (즉, 타겟)
					// num : 몇번째 input 요소인지 확인용 (자릿수 구분용) 숫자값
					
					// console.log(inputFile);
					// > 현재 내용물이 변경된 input type="file" 요소 자체
					
					// console.log(inputFile.files);
					// > input type="file" 요소의 files 속성은
					//   입력받은 파일의 정보들을 "배열" 형식으로 여러개 묶어서 반환해주는 속성
					// > 참고로 input type="file" 요소에 multiple 속성을 기술하면
					//   한번에 여러개의 파일을 입력받을 수 있다!!
					// > 파일이 여러개든 한개든 입력받든 간에 files 속성의 length 는 파일의 갯수를 나타냄
					
					// console.log(inputFile.files.length);
					// > input type="file" 로 파일 선택 시 1, 파일 선택 취소 시 0 이 나옴!!
					//   (length 는 배열의 크기)
					// > 즉, length 가 1 이냐 0 이냐에 따라 파일의 존재 유무를 판별 가능!!
					
					// console.log(num);
					
					if(inputFile.files.length == 1) {
						// > 선택된 파일이 있을 경우
						//   선택된 파일을 읽어들여서 그 영역에 맞는 곳 (img 요소) 에 이미지 미리보기
						
						// 1. 파일을 읽어들일 FileReader 자바스크립트 객체 생성 (생성자함수를 통해)
						let reader = new FileReader(); 
						
						// 2. FileReader 객체에서 제공하는 파일을 읽어들이는 메소드를 호출
						// > 입력된 파일들 중 어느 파일을 읽어들일건지 매개변수 인자로 제시해야함!!
						//   우리는 0 번 인덱스에 담긴 파일 정보를 제시한 것!!
						// > 호출 시 해당 파일을 읽어들여서 그 파일만의 고유한 URL 주소를 임의로 만들어서 부여해줌!!
						// > 해당 URL 주소를 알맞은 img 태그의 src 속성에 속성값으로 넣기!!
						reader.readAsDataURL(inputFile.files[0]);
						
						// 3. readAsDataURL 메소드에 의해 파일 읽기가 완료되었을 때 실행할 함수를 정의
						reader.onload = function(e) {
							// > 각 영역에 맞게 이미지 미리보기
							
							// 이벤트 핸들러 함수가 익명함수일 경우 매개변수로 e 를 제시하면
							// > 매번 해당 이벤트 관련된 정보를 담고 있는 이벤트 객체가 넘어온다!!
							// > e.target 으로 타겟도 얻어낼 수 있었음!!
							// > e.target.result 속성에 각 파일의 url 주소가 담긴다!!
							
							switch(num) {
							case 1 : 
								$("#titleImg").prop("src", e.target.result);
								break;
							case 2 :  
								$("#contentImg1").prop("src", e.target.result);
								break;
							case 3 :  
								$("#contentImg2").prop("src", e.target.result);
								break;
							case 4 :  
								$("#contentImg3").prop("src", e.target.result);
								break;
							}
							
						};
						
					} else {
						// > 파일 선택이 취소되었을 경우
						
						// 이미지 미리보기를 사라지게 하기
						
						switch(num) {
						case 1 : 
							$("#titleImg").prop("src", null);
							break;
						case 2 : 
							$("#contentImg1").prop("src", null);
							break;
						case 3 : 
							$("#contentImg2").prop("src", null);
							break;
						case 4 : 
							$("#contentImg3").prop("src", null);
							break;
						}
						
					}
					
				}
			</script>

			<br><br>

			<div align="center">
				<button type="submit" class="btn btn-primary btn-sm">등록하기</button>
				<button type="reset" class="btn btn-secondary btn-sm">초기화</button>
			</div>

			<br><br>

		</form>

	</div>
	
	<br><br>

</body>
</html>