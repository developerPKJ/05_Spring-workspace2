package com.kh.myweb.common.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.kh.myweb.member.model.vo.Member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/*
 * * Interceptor (인터셉터)
 * 
 * - 요청이 DispatcherServlet 을 거쳐 Controller 클래스로 도달하기 전에
 *   요청을 "가로채서" 선처리할 구문을 작성하거나 
 *   반대로 요청 처리가 완료된 후 응답 정보가 DispatcherServlet 으로 리턴되기 전에
 *   응답을 "가로채서" 후처리할 구문을 작성할 수 있는 클래스
 *   
 * 즉,
 * View --> DispatcherServlet --> Interceptor --> Controller 
 * 							  <--				  <--
 * 
 * - 주로 로그인 권한 체크 시 많이 쓰임 (선처리)
 * - 주로 로깅을 남길 때 많이 쓰임 (후처리)
 * > 공통적으로 선처리 또는 후처리 해야 하는 공통코드들 (구문들) 을 작성할 때 많이 쓰임!!
 * 
 * * Interceptor 의 흐름 간섭 시점
 * 1. 요청 전 : Controller 로 요청이 도달하기 "전" 선처리할 내용을 정의
 * 			  즉, Controller 메소드가 실행되기 전에 실행할 구문을 정의
 * 			  (preHandle)
 * 			  예) 로그인 유무 판단, 로그인한 회원들 끼리의 권한 체크 등
 * 2. 요청 후 : Controller 에서 요청 처리 후 응답페이지로 도달하기 "전" 에 후처리할 내용을 정의
 * 			  즉, Controller 메소드가 실행된 직후에 실행할 구문을 정의
 * 			  (postHandle)
 * 			  예) 요청이 잘 처리 되었는지 기록 (로깅) 등
 * 
 * * Interceptor 구현 방법
 * 
 * - 스프링에서 제공하는 HandlerInterceptor 라는 "인터페이스" 를 상속받아서 구현
 * - 단, 모든 메소드가 default 메소드임!! (내가 필요한 것들만 오버라이딩 해도 됨)
 * - Interceptor 를 구현 후에는 요청이 인터셉터를 거쳐갈 수 있도록 설정해줘야함!!
 */

@Component // 이 클래스를 빈으로 등록하겠다.
public class LoginInterceptor implements HandlerInterceptor {

	// 1. preHandle 메소드 (선처리용)
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// * 매개변수 
		// - request : 사용자의 요청, parameter 영역에 요청 시 전달값들이 담겨있음
		// - response : 요청에 대한 응답, 응답 시 필요한 메소드들이 들어있음
		// - handler : 이 요청은 누가 처리하기로 했는가? 에 대한 정보가 담겨있음
		
		// System.out.println("preHandle 메소드가 잘 작동하나?");
		// System.out.println(handler);
		
		// > 로그인 여부 확인 로직 구현
		//   참고로 로그인한 사용자의 정보는 session 객체에 담겨있음!!
		
		// 1. 우선 session 객체 부터 얻어내기 (request 객체로부터)
		HttpSession session = request.getSession();
		
		// 2. session 으로 부터 로그인한 회원의 정보를 꺼내오기
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		// 3. 로그인 여부 판별
		if(loginUser != null) {
			// > 로그인이 된 상황
			
			return true; // 로그인된 사용자만 접속 가능하도록 true
			
		} else {
			// > 로그인이 안된 상황
			
			// 이왕이면 1회성 알림 문구를 담아서 메인페이지로 url 재요청까지 하고싶음!!
			// (안그러면 흰 화면이 나오기 때문)
			
			session.setAttribute("alertMsg", "로그인 후 이용 가능한 서비스입니다.");
			
			response.sendRedirect("/myweb");
			// > 오버라이딩된 메소드라 문자열 타입으로 리턴이 불가!!
			//   그래서 response 객체에서 제공하는 sendRedirect 메소드를 통해서 url 재요청을 진행
			
			return false; // 로그인하지 않은 사용자는 요청 접속 차단 false
		}
		
		// * 리턴값 (boolean)
		// - true : 요청에 최종적으로 도달하겠다. (Controller 로 넘어가겠다)
		// - false : 요청에 도달하지 않고 여기서 끊어버리겠다. (Controller 로 넘어가지않겠다)
		
		// return true;
	}

	// 2. postHandle 메소드 (후처리용)
	/*
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {

		// * 매개변수
		// - request : 사용자의 요청
		// - response : 요청에 대한 응답
		// - handler : 이 요청은 누가 처리하기로 했는가? 에 대한 정보
		// - modelAndView : model (응답데이터) + view (응답페이지) 정보
		
		System.out.println("postHandle 이 잘 작동 되나?");
		System.out.println(handler);
		System.out.println(modelAndView);
		
		// * 리턴값 (void) - 없음
		// > 이미 Controller 에서 요청 처리가 다 끝나고 돌아오는 시점 (길) 이기 때문에
		//   이미 처리된 내용을 되돌릴 수 없으므로 void
	}
	*/
	
}





