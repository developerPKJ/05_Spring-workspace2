package com.kh.ajax.controller;

import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kh.ajax.model.vo.Member;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AjaxController {

	// Ajax 요청이 들어오면 기존 동기식 방식 때와 동일하게
	// Controller 클래스와 메소드를 작성해준다!!
	
	// Ajax 요청 시 전달값 뽑기 1. HttpServletRequest 객체 이용
	/*
	@GetMapping("jqAjax1")
	public void ajaxMethod1(HttpServletRequest request) {
		
		// System.out.println("잘 호출되나?");
		
		String input = request.getParameter("input");
		System.out.println("요청 시 전달값 : " + input);	
	}
	*/
	
	// Ajax 요청 시 전달값 뽑기 2. @RequestParam 어노테이션 이용
	/*
	@GetMapping("jqAjax1")
	public void ajaxMethod1(@RequestParam("input") String param1) {
		
		System.out.println("요청 시 전달값 : " + param1);
	}
	*/
	
	// Ajax 요청 시 전달값 뽑기 3. 키값과 동일한 이름의 매개변수 정의
	// (@RequestParam 어노테이션 생략하는 방법)
	
	// Ajax 요청 시 전달값 뽑기 4. 커맨드 객체 방식
	// > 마찬가지로 요청 시 전달값들도 기존의 동기식 방식이랑 동일하게 뽑아낸다.
	
	// -----------------------------
	
	// Ajax 요청 시 응답 데이터 전달하기 1. HttpServletResponse 객체 이용
	/*
	@GetMapping("jqAjax1")
	public void ajaxMethod1(String input, HttpServletResponse response) throws IOException {
		
		// System.out.println(input);
		// > 이 전달값을 가지고 DB 를 거쳐 왔다 라는 가정 하에 응답할 데이터 셋팅
		
		String responseData = "문자열의 길이는 " + input.length() + "입니다.";
		
		// 1. 응답데이터의 형식 및 인코딩 설정
		response.setContentType("text/html; charset=UTF-8");
		
		// 2. 요청했었던 jsp 와의 "출력스트림" 객체 생성해두기
		PrintWriter out = response.getWriter();
		
		// 3. 만들어진 출력 통로를 통해 응답데이터를 넘겨주기
		out.print(responseData);
	}
	*/
	
	// Ajax 요청 시 응답 데이터 전달하기 2. 응답할 데이터를 문자열로 반환해주기
	// > 단, 문자열을 그냥 리턴하면 DispatcherServlet 이 리턴된 문자열을 
	//   응답데이터가 아닌 "응답페이지" 정보로 인식하게 된다!!
	// > 그래서 지금부터 내가 리턴하는 문자열이 "응답페이지" 정보가 아니라 "응답데이터" 임을 알려주는 
	//   어노테이션을 반드시 붙여줘야 한다!!
	@ResponseBody // 지금부터 내가 리턴하는 문자열은 응답데이터임을 알려주는 어노테이션
	@GetMapping("jqAjax1")
	public String ajaxMethod1(String input) {
		
		// > 마찬가지로 DB 를 거쳐 왔다라는 가정 하에 응답데이터를 셋팅하면
		String responseData = "문자열의 길이는 " + input.length() + "입니다.";
		
		return responseData;
	}
	
	@ResponseBody
	@PostMapping("jqAjax2")
	public String ajaxMethod2(String name, int age) {
		
		// System.out.println(name);
		// System.out.println(age);
		
		return name + "님은 " + age + "살 입니다.";
	}
	
	// --------------------------
	
	// 다수의 응답 데이터가 있을 경우
	/*
	@GetMapping("jqAjax3")
	public void ajaxMethod3(int math, int eng, HttpServletResponse response) throws IOException {
		
		// System.out.println(math);
		// System.out.println(eng);
		
		// 응답데이터로 두 점수의 총합과 평균을 보내볼 것!!
		int total = math + eng;
		double avg = (math + eng) / 2;
		
		response.setContentType("text/html, charset=UTF-8");
		
		PrintWriter out = response.getWriter();
		
		out.print(total);
		out.print(avg);
		// > response 객체에서 제공하는 출력스트림을 통해 여러개의 응답 데이터를 내보낼 경우
		//   그 응답데이터를 받아주는 success function 에서 여러개의 응답 데이터가
		//   하나의 문자열로 연이어져서 도착하게 된다!! (스트림은 선입선출이라서)
		
	}
	*/
	
	/*
	@ResponseBody
	@GetMapping("jqAjax3")
	public String ajaxMethod3(int math, int eng) {
		
		int total = math + eng;
		double avg = (math + eng) / 2;
		
		return total + ", " + avg;
	}
	*/
	
	@ResponseBody
	@GetMapping(value="jqAjax3", produces="application/json; charset=UTF-8")
	public String ajaxMethod3(int math, int eng, HttpServletResponse response) throws IOException {
		
		int total = math + eng;
		double avg = total / 2;
		
		/*
		 * * JSON (JavaScript Object Notation)
		 * - 자바스크립트 객체 표기법
		 * - Ajax 응답 시 데이터 전송에 사용되는 포맷 형식 중 하나
		 * - 여러개의 데이터를 하나로 뭉칠 수 있음 (JS 의 객체 형식으로 가공할 수 있음)
		 * - 기본적으로 자바에서는 제공해주지 않기 때문에 JSON 을 이용하려면 라이브러리 (.jar) 를 연동해야함!!
		 *   (json-simple.jar 추가)
		 * 
		 * * JSON 처리 시 사용되는 클래스 종류 (응답 데이터가 여러개일 경우 사용)
		 * 1. JSONArray : [value, value, ...]
		 * - 즉, 자바스크립트의 배열 형식으로 묶어서 한번에 응답데이터들을 보내겠다.
		 * 
		 * 2. JSONObject : {key : value, key : value, ...}
		 * - 즉, 자바스크립트의 객체 형식으로 묶어서 한번에 응답데이터들을 보내겠다.
		 */
		
		// - JSONArray 이용
		/*
		JSONArray jArr = new JSONArray(); // []
		jArr.add(total); // [total]
		jArr.add(avg); // [total, avg]
		// > JSONArray 는 자바의 ArrayList 와 같은 구조
		//				 자바스크립트의 배열과 같은 구조
		
		// response.setContentType("text/html; charset=UTF-8");
		// > "[185, 92.0]" 라는 문자열로 응답데이터가 넘어감!! (배열인척 하는 문자열)
		// > JSON 형식을 응답데이터로 넘기고자 할 경우에는 content type 을 아래와 같이 지정해야 함!!
		// response.setContentType("application/json; charset=UTF-8");
		// response.getWriter().print(jArr);
		
		return jArr.toJSONString(); // "[185, 92.0]"
		*/
		
		// - JSONObject 이용
		JSONObject jObj = new JSONObject(); // {}
		jObj.put("total", total); // {total : 185}
		jObj.put("avg", avg); // {total : 185, avg : 92.0}
		// > JSONObject 는 자바의 HashMap 과 같은 구조
		//  			  자바스크립트의 객체와 같은 구조
		
		// response.setContentType("application/json; charset=UTF-8");
		// response.getWriter().print(jObj);
	
		return jObj.toJSONString(); // "{total : 185, avg : 92.0}"
		// > 굳이 복잡하게 HttpServletResponse 객체를 쓰는 대신에
		//   JSON 의 문자열 형식을 리턴해서 응답데이터를 넘길 수 있다!! (produces 설정 필수)
	}
	
	@ResponseBody
	@GetMapping(value="jqAjax4", produces="application/json; charset=UTF-8")
	public Member ajaxMethod4(int userNo, HttpServletResponse response) throws IOException {
		
		// System.out.println(userNo);
		
		// > DB 로 부터 해당 회원 한명의 정보를 조회해왔다 라는 가정 하에 응답할 데이터를 셋팅
		Member m = new Member(userNo, "고길동", 50, "남");
		
		/*
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().print(m);
		*/
		// > 그냥 출력 스트림으로 Member 객체인 m 을 응답하게 되면
		//   내부적으로 toString() 메소드가 호출되면서 각 필드값이 하나의 문자열로 연이어져서 넘어가게 된다.
		
		// > 그래서 JSON 을 활용해볼 것!!
		//   JSONObject 를 활용해서 응답해볼 것
		//   {속성명 : 속성값, 속성명 : 속성값, ...}
		// > 속성명 자리에는 m 의 필드명을, 속성값 자리에는 해당 필드의 필드값을 담아볼 것
		
		/*
		JSONObject jObj = new JSONObject(); // {}
		jObj.put("userNo", m.getUserNo());
		jObj.put("userName", m.getUserName());
		jObj.put("age", m.getAge());
		jObj.put("gender", m.getGender()); // {userNo : 1, userName : "고길동", age : 50, gender : "남"}
		
		return jObj.toJSONString(); // '{userNo : 1, userName : "고길동", age : 50, gender : "남"}'
		*/
		
		// > 자바 VO 객체 단위를 응답데이터를 넘겨야 할 경우에는 JSONObject 타입으로 옮겨 담은 후 넘겨준다!!
		//   이 때, 속성명을 통상적으로 필드명과 동일하게 잡고 넘겨준다.
		// > 단, VO 필드 갯수만큼 일일이 다 JSON 으로 옮겨담는게 정석이나,
		//   코드가 불필요하게 길어지고, 실수할 가능성도 높아진다!!
		// > 그래서 Spring Boot 에서는 기본적으로 jackson 이라는 라이브러리를 제공해준다.
		
		return m;
		// > 해당 메소드의 리턴 타입을 내가 응답데이터로 넘기고자 하는 VO 타입으로 지정한 후,
		//   해당 VO 객체를 그냥 직접 return 해주면 된다!!
		// > 내부적으로 해당 VO 객체가 jackson 라이브러리에 의해 VO -> JSONObject 형식으로 알아서 가공 된 후
		//   응답데이터로 넘어가는 원리
		//   (이 때, 심지어 JSONObject 의 속성명은 자동으로 해당 VO 객체의 필드명으로 잡힌다)
	
	}
	
	@ResponseBody
	@PostMapping("jqAjax5")
	public ArrayList<Member> ajaxMethod5() {
		
		// 회원 전체조회 요청을 처리했다 라는 가정 하에 응답할 데이터를 셋팅
		// > 전체 회원들의 정보가 담겨있는 ArrayList
		
		ArrayList<Member> list = new ArrayList<>();
		
		list.add(new Member(1, "고길동", 50, "남"));
		list.add(new Member(2, "김갑생", 23, "여"));
		list.add(new Member(3, "박말순", 39, "남"));
		list.add(new Member(4, "강개똥", 44, "여"));
		list.add(new Member(5, "박말똥", 37, "남"));
		list.add(new Member(6, "김말똥", 26, "여"));
		// > MyBatis 를 통해 받아올 내용임!! (나중에 직접 코드로는 작성하지 않음)
		
		// list 를 응답데이터로 넘기기 위해서는 JSON 형식으로 변환을 해줘야함!!
		// > VO 객체 한개 --> JSONObject 
		//   ArrayList 한개 --> JSONArray
		
		/*
		JSONArray jArr = new JSONArray(); // []
		
		for(Member m : list) {
			
			JSONObject jObj = new JSONObject(); // {}
			jObj.put("userNo", m.getUserNo());
			// ..
			
			jArr.add(m);
		}
		*/
		// > 원래대로라면 이렇게 일일이 옮겨줘야 한다!!
		// > 마찬가지로 jackson 라이브러리를 이용하면 자동으로 가공해서 넘겨준다!!
		
		return list;
	}
	
	
}











