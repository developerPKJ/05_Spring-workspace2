package com.kh.opendata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OpenDataProjectApplicationTests {
	public static final String SERVICE_KEY = "4fdb0aecbc5127c1a125a2fd96a216fc0527c3d8e96e98fa33751992c2b05058";

	@Test
	void contextLoads() throws IOException {
		// 1. 주소 설정
		// http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty
		// 위 주소로 요청보내기
		String url = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty";
		
		// 딱봐도 get 방식
		url += "?serviceKey=" + SERVICE_KEY;
		url += "&returnType=json";
		url += "&numOfRows=5";
		url += "&pageNo=1";
		url += "&sidoName=서울";
		url += "&ver=1.0";
		
		// System.out.println(url);
		
		// 2. java 구문 이용해서 OpenAPI 요청 보내기
		URL requestUrl = new URL(url);
		
		HttpURLConnection urlConnection = (HttpURLConnection)requestUrl.openConnection();
		
		urlConnection.setRequestMethod("GET");
		
		// 3. 응답데이터를 받아올 입력스트림 객체 얻기
		// 응답데이터에 한글이 포함되어 있으므로 문자스트림 사용
		// 속도 향상 위해 보조스트림도 사용
		BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		
		// 4. br.readLine()로 while문 이용해 읽기
		String responseData = "";
		String line = "";
		while((line = br.readLine()) != null) {
			responseData += line;
		}
	}

}
