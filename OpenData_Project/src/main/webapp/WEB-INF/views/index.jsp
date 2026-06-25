<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<!-- 온라인 방식 -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<body>
	
	<h1>실시간 대기오염 정보</h1>

	<br><br>
	
	지역 : 
	<select id="location">
		<option>전국</option>
		<option>서울</option>
		<option>부산</option>
		<option>대구</option>
		<option>인천</option>
		<option>광주</option>
		<option>대전</option>
		<option>울산</option>
		<option>경기</option>
		<option>강원</option>
		<option>충북</option>
		<!-- 개발문서에서 제시하는 sidoName 에 해당되는 값들은 필요하다면 다 제시하긴 해야함 -->
	</select>
	
	<button id="btn">대기오염 정보 확인</button>
	
	<br><br>
	
	<table id="result" border="1" align="center">
		<thead>
			<tr>
				<th>측정소명</th>
				<th>측정일시</th>
				<th>통합대기환경수치</th>
				<th>미세먼지농도</th>
				<th>일산화탄소농도</th>
				<th>이산화질소농도</th>
				<th>아황산가스농도</th>
				<th>오존농도</th>
				<!-- 개발문서에서 제시하는 응답데이터들 중에서 필요한 것들만 추려서 쓴다. -->
			</tr>
		</thead>
		<tbody></tbody>
	</table>
	
	<br><br>
	
	<script>
		$(function() {
			
			$("#btn").click(function() {
				
				// 사용자가 선택한 지역을 뽑기
				let location = $("#location").val();
				// > select 도 val() 메소드로 선택된 option 값을 얻어낼 수 있음
				
				// console.log(location);
				
				// Controller 로 요청 보내기 (Ajax)
				$.ajax({
					url : "/opendata/air",
					type : "get",
					data : {
						location : location
					},
					success : function(result) {
						
						// XML 형식으로 응답데이터를 받았을 경우
						// console.log(result);
						// > #document 가 최상위 객체로 잡혀 있고,
						//   html 의 DOM 요소들 처럼 태그 안에 태그들이 부모 - 자식 관계를 이루고 있다.
						
						// console.log(result.find("item"));
						// > jQuery 의 find() 메소드 : 선택된 요소의 모든 자손, 후손들 중에서
						//							 내가 원하는 요소들만 선택할 수 있음!!
						//							 (후손 탐색 메소드)
						// > xml 요소들도 html 과 동일하게 다 태그 형식이기 때문에
						//   jQuery 의 탐색 메소드를 적용할 수 있다!! (Markup Language 이기 때문)
						
						// > result.find is not a function
						//   result (#document) 는 순수 자바스크립트 형식의 객체고,
						//   find 는 jQuery 형식의 메소드임!! (호환이 안맞음)
						// > $(result) 로 jQuery 화 시켜주면 호환이 맞게 된다!!
						
						// console.log($(result).find("item"));
						
						let itemArr = $(result).find("item");
						// [item, item, item, ...]
						
						let resultStr = "";
						
						itemArr.each(function(index, item) {
							
							// console.log(item);
							// console.log($(item).find("stationName").text());
							
							resultStr += "<tr>"
									   +		"<td>" + $(item).find("stationName").text() + "</td>"
									   +		"<td>" + $(item).find("dataTime").text() + "</td>"
									   +		"<td>" + $(item).find("khaiValue").text() + "</td>"
									   +		"<td>" + $(item).find("pm10Value").text() + "</td>"
									   +		"<td>" + $(item).find("coValue").text() + "</td>"
									   +		"<td>" + $(item).find("no2Value").text() + "</td>"
									   +		"<td>" + $(item).find("so2Value").text() + "</td>"
									   +		"<td>" + $(item).find("o3Value").text() + "</td>"
									   + "</tr>";
							
						});
						
						$("#result>tbody").html(resultStr);
						
						// JSON 형식으로 응답데이터를 받았을 경우
						/*
						// console.log(result);
						// console.log(result.response);
						// console.log(result.response.body);
						// console.log(result.response.body.items);
						// > [{}, {}, {}, ...]
						
						// 배열을 따로 변수에 담기
						let itemArr = result.response.body.items;
						
						// 반복적으로 요소 만들어내기 (문자열로)
						let resultStr = "";
						
						for(let i = 0; i < itemArr.length; i++) {
							
							// console.log(itemArr[i]);
							// console.log(itemArr[i].stationName, itemArr[i].dataTime);
							
							let item = itemArr[i];
							
							resultStr += "<tr>"
									   +		"<td>" + item.stationName + "</td>"
									   +		"<td>" + item.dataTime + "</td>"
									   +		"<td>" + item.khaiValue + "</td>"
									   +		"<td>" + item.pm10Value + "</td>"
									   +		"<td>" + item.coValue + "</td>"
									   +		"<td>" + item.no2Value + "</td>"
									   +		"<td>" + item.so2Value + "</td>"
									   +		"<td>" + item.o3Value + "</td>"
									   + "</tr>";
						}
						
						// 최종적으로 화면에 출력 (tbody)
						$("#result>tbody").html(resultStr);
						*/
						
					},
					error : function() {
						
						console.log("대기오염 정보 조회용 ajax 통신 실패!");
					}
				});
				
			});
			
		});
	</script>

</body>
</html>



