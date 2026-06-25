package com.kh.opendata.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class APIController {
	
	// 인증키 정보 (상수 필드)
	public static final String SERVICEKEY = "Yr16XkfGx9Vy2lEjt4bQy%2BSYthnLjMGP%2BMz3T%2FfsiD%2FTpHcQewhjnP97drW0gBlWKg%2FXGl6AX%2BXRPVz7UKx8ew%3D%3D";
	
	@ResponseBody
	// @GetMapping(value="air", produces="application/json; charset=UTF-8") // JSON 형식
	@GetMapping(value="air", produces="text/xml; charset=UTF-8") // XML 형식
	public String airPollution(String location) throws IOException {
		
		// System.out.println(location);
		
		// 1. 요청할 url 주소 만들기 (String)
		String url = "https://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty";
		
		// 쿼리스트링으로 이어붙이기
		url += "?serviceKey=" + SERVICEKEY;
		url += "&returnType=xml";
		url += "&numOfRows=20";
		url += "&pageNo=1";
		url += "&sidoName=" + URLEncoder.encode(location, "UTF-8");

		// 2. 자바 구문을 통해 OpenAPI 요청 후 응답받기
		URL requestUrl = new URL(url);
		
		HttpURLConnection urlConnection = (HttpURLConnection)requestUrl.openConnection();
		
		urlConnection.setRequestMethod("GET");
		
		// 요청한 서버와의 입력스트림 객체 얻어내기
		BufferedReader br = new BufferedReader( 
							new InputStreamReader( urlConnection.getInputStream()));  
		
		// 3. 통로를 통해 응답데이터를 한줄씩 읽어들이기
		String line = null;
		
		// 응답데이터를 최종적으로 담을 변수
		String responseText = "";
		
		while((line = br.readLine()) != null) {
			
			responseText += line;
		}
		
		// 4. 통로를 닫고 연결도 끊기
		br.close();
		urlConnection.disconnect();
		
		// System.out.println(responseText);
		// > responseText 에는 JSON 형식의 문자열이 담겨있음!!
		
		return responseText;
	}

}







