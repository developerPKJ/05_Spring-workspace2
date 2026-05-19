package com.kh.myweb.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kh.myweb.member.model.service.MemberService;
import com.kh.myweb.member.model.vo.Member;

import jakarta.servlet.http.HttpSession;

/*
 * * 어떤 요청이 들어올 때 마다 Handler Mapping 에 대응되는 메소드가 호출됨!!
 * 
 * 내부적으로 스프링의 핵심 부품인 DispatcherServlet 에서 
 * 
 * @Autowired
 * MemberController memberController;
 * 
 * memberController.loginMember(m);
 * 
 * 이 구문이 실행됨!!
 * 
 * * 사실 Controller 클래스 상단에 @Controller 어노테이션을 붙였던 것 또한
 *   스프링이 필요할 때 알아서 객체로 생성해서 쓰라고 Bean 으로 등록해줬던것!!
 *   (@Component 어노테이션으로 써도 되지만, 더 명시적이기 때문에 @Controller 를 더 많이 씀)
 */

/*
 * * MemberController 클래스는 애초에 "회원 관련 요청" 처리용도로 만든 클래스임!!
 *   (보통 이런식으로 회원 관련, 공지사항 관련, 게시판 관련 등 도메인 단위로 코드를 쪼개서 짠다)
 * 
 * - 우리는 일부러 그동안 회원 관련 요청일 경우 url 주소를
 *   member/xxx 이런식으로 앞에 "member/" 를 붙여서 통일시켜줬었음!!
 * - 그래서 이 MemberController 의 모든 메소드들은 HandlerMapping 이
 *   "member/xxx" 로 잡혀있음!!
 */

// @Component
@Controller
@RequestMapping("member")
// > @RequestMapping 어노테이션은 메소드 상단 뿐만 아니라 Controller 클래스 상단에도 기술 가능!!
public class MemberController {
	
	// BCryptPasswordEncoder 객체를 전역변수로 둘 것 (의존성 주입도 받을 것)
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	// MemberService 객체를 전역변수로 둘 것
	// private MemberService memberService = new MemberService();
	// > 물론 이렇게 직접 객체를 생성해서 써도 되지만,
	//   Spring 에서는 주도권이 모두 스프링 프레임워크에게 있음!!
	//   (하다못해 객체를 생성하는것 까지도 스프링이 알아서 해주겠다라는 뜻)
	
	// IoC : Inversion of Control, 제어의 역전
	// > 프로그래밍 상의 모든 주도권은 스프링 프레임워크가 쥐겠다.
	//   객체 생성, 객체 소멸, 트랜잭션 처리 등등 스프링이 알아서 해주겠다.
	@Autowired
	private MemberService memberService /* = new MemberService() */;
	// > @Autowired 어노테이션에 의해 자동으로 MemberService 객체가 생성되서 담김
	//   단, 해당 클래스 선언부 상단에 @Component 어노테이션을 반드시 붙여줘야 성립됨!!
	
	// DI : Dependency Injection, 의존성 주입
	// > 자바 객체를 스프링이 생성해서 나에게 쓰라고 "주입" 해주겠다.
	//   단, 내가 얻어내고 싶은 클래스에 대해 필요할 때마다 객체를 생성해달라고 설정 (Bean 등록)
	//   스프링이 객체를 DI 해줄 때, 알아서 교통정리까지 다 해줌!!
	// > 우리는 위에서 Bean 등록 시 
	//   MemberService 클래스 상단에 @Component 라는 어노테이션을 쓴 것!!
	
	// @PostMapping("member/login")
	@PostMapping("login")
	public String loginMember(Member m, Model model, HttpSession session) {
		
		// System.out.println("잘 호출되나?");
		
		// 1. 로그인할 회원의 아이디와 비번을 받기 (요청 시 전달값 받기)
		// System.out.println(m);
		// > 커맨드 객체 방식으로 잘 받아왔고, userId, userPwd 필드에 잘 담겨옴!!
		// > VO 객체로 가공까지 끝!!
		
		// 암호화 작업 후
		// 로그인 기능 코드 또한 암호화 작업을 감안해서 수정해줘야함!!
		// System.out.println(m);
		// > Member m 의 userId : 사용자가 입력했던 아이디
		//				userPwd : 사용자가 입력했던 비번 "평문"
		
		// 우선 DB 에 가서 select 를 해올 예정!!
		// > 사용자로 입력받은 비번 평문을 일단 암호화 후 DB 까지 가서 일치하는지를 검사한다면?
		// > 우리는 쿼리문으로 비밀번호 일치 여부를 검사할 수 없다.
		//   (매번 랜덤한 salt 값이 붙은 다음에 암호화가 되기 때문,
		//    매번 평문이 아무리 같더라도 암호화된 결과는 다르게 나올 것이기 때문!!)
		// > 비밀번호를 제외하고, 아이디가 입력값과 일치하는지, 유효한 회원인지 (탈퇴되지 않은 회원인지)
		//   이 두개만 쿼리문으로 검사하고 돌아올 예정!!
		
		// 2. Service 요청 후 결과 받기
		Member loginUser = memberService.loginMember(m);
		// System.out.println(loginUser);
		// > 만약 사용자가 제대로된 아이디를 입력했다면 (유효한 회원의 경우)
		//   SELECT * 에 의해 해당 회원의 정보가 통으로 넘어옴!!
		// > 만약 아이디가 없는 아이디거나 유효하지 않은 회원의 경우에는
		//   null 이 넘어오는 것을 볼 수 있음!!
		
		// 즉, loginUser 가 null 이 아니라면 비번만 일치한다면 로그인 성공 처리를 해주면 된다.
		
		// > 자바 코드로 이 자리에서 비번 대조까지 해보자!!
		
		// m 의 userPwd 필드 (평문) 와 loginUser 의 userPwd 필드 (암호문) 값을 대조
		// > BCryptPasswordEncoder 객체에서는
		//   평문과 암호문을 둘 다 제시했을 때 내부적으로 대조 작업을 해주는 메소드를 이미 갖고있음!!
		
		// [ 표현법 ]
		// bCryptPasswordEncoder.matches("평문", "암호문") : boolean
		// > 일치하면 true, 일치하지 않으면 false
		
		// 3. 최종 로그인 검사 결과에 따른 응답화면 지정
		if((loginUser != null) && 
		   (bCryptPasswordEncoder.matches(m.getUserPwd(), loginUser.getUserPwd()))) {
			// > 로그인 성공일 경우
			
			// 로그인한 회원의 정보를 마찬가지로 session 에 담아야함!! (loginUser 키값으로)
			session.setAttribute("loginUser", loginUser);
			
			// 세션에 1회성 알림 문구를 담아 메인페이지로 url 재요청
			session.setAttribute("alertMsg", "성공적으로 로그인이 되었습니다.");
			
			return "redirect:/";
			
		} else {
			// > 로그인 실패일 경우
			
			// 에러 문구를 담아서 에러페이지로 포워딩
			model.addAttribute("errorMsg", "로그인에 실패했습니다.");
			
			return "common/errorPage";
		}
		
		// 암호화 작업 전
		/*
		// 2. Service 요청 후 결과 받기
		Member loginUser = memberService.loginMember(m);
		
		// System.out.println(loginUser);
		// > 아이디, 비번이 모두 일치하고 탈퇴되지 않은 회원이라면 null 이 아님!! (로그인 성공)
		
		// 3. 결과에 따른 응답화면 지정
		if(loginUser == null) {
			// > 로그인 실패

			// 에러문구를 담아서 에러페이지가 보여지게끔 해주기!!
			
			// 1) 에러페이지를 나타내는 jsp 파일 먼저 생성
			// > views 폴더의 common 폴더 내에 errorPage.jsp 파일로
			
			// 2) 에러페이지에서 필요로 하는 데이터를 담기 (Model 객체에)
			model.addAttribute("errorMsg", "로그인에 실패했습니다.");
			
			// 3) 응답페이지가 보여지게끔 문자열 return
			return "common/errorPage";
			// > /WEB-INF/views/common/errorPage.jsp
			
		} else {
			// > 로그인 성공
			
			// 로그인한 회원의 정보를 응답데이터로 session 객체에 담아두기!!
			session.setAttribute("loginUser", loginUser);
			// > 로그인한 회원의 정보는 로그아웃 전까지 아무데서나 다 꺼내 쓸 수 있어야 함!!
			//   주로 게시글 작성, 댓글 작성 등 (로그인한 회원의 정보가 추후 작성자의 정보가 될 것)
			
			// session 에 loginUser 뿐만 아니라 1회성 알람 문구도 담을 수 있음!!
			// > 이 1회성 알람 문구를 띄워주는 기능은 이미 menubar.jsp 에서 공통코드로 완성시킴
			session.setAttribute("alertMsg", "성공적으로 로그인이 되었습니다.");
			
			// * 응답페이지를 보여주는 방법에는 두가지 방법이 있음!!
			// 1. 포워딩 방식
			// > 응답페이지용 jsp 파일을 새로이 하나 만들어서
			//   현재 브라우저에 보이는 url 주소는 그대로, 화면만 위임하는 방식
			//   (그동안 알게모르게 써왔던 방식이 바로 포워딩 방식이였음)
			
			// 2. url 재요청 방식 (sendRedirect 방식)
			// > url 주소를 제시하여 이미 존재하는 컨트롤러에 요청을 보내 화면을 띄워주는 방식
			//   즉, 자바스크립트의 location.href = "~~~"; 와 같은 원리
			//   (서버로 다시 한번 url 요청을 보내서 그에 해당하는 응답페이지를 받는 방식)
			// > 주로 응답데이터를 request 에 담을 때에는 쓰지 않음!!
			//   (request 객체에 응답데이터를 담아둘 경우 url 재요청 방식을 쓸 경우 다 날라감)
			
			// 메인페이지로 응답화면이 보여지게끔
			
			// 1. 포워딩 방식으로 응답화면을 띄워줄 경우
			// return "index";
			// > /WEB-INF/views/index.jsp
			// > 분명히 눈에는 메인페이지로 보이나,
			//   url 주소창에는 http://localhost:8006/myweb/member/login 으로 적혀있음
			
			// 2. url 재요청 방식으로 응답화면을 띄워줄 경우
			// > http://localhost:8006/myweb/ 으로 요청을 보내고 싶음!!
			//   그러면 알아서 내 눈 앞에 메인페이지가 보여질꺼니깐
			// [ 표현법 ]
			// return "redirect:요청할url경로";
			
			return "redirect:/"; // / 는 /myweb 뒤의 / 를 나타냄
			
			// > 각 서비스마다 사용되는 방식이 다름!! 
			//   로그인 성공 시에는 url 재요청 방식이 쓰인다!!
		}
		*/
		
	}
	
	// @GetMapping("member/logout")
	@GetMapping("logout")
	public String logoutMember(HttpSession session) {
		
		// 로그인 요청 처리
		// > 유효한 회원 정보를 session 에 담는 것
		
		// 로그아웃 요청 처리
		// > session 에 담긴 로그인한 회원의 정보를 날려버리는 것
		
		// 로그아웃 구현 방법 2가지
		// 1. session 을 만료시키는 방법 (session 을 무효화 시키기)
		// session.invalidate();
		
		// 2. session 에 담겨있는 loginUser 에 대한 키 + 밸류 세트만 제거하는 방법
		//    (removeAttribute 메소드를 이용)
		session.removeAttribute("loginUser");
		
		// > 단, session 에 loginUser 이외의 다른 응답데이터도 담을 수 있다!!
		//   session 에 loginUser 이외의 다른 응답데이터도 담겨있다면
		//   그 경우에는 1번 방법으로 로그아웃을 구현할 수 없다!!
		//   (1번 방법은 응답데이터들을 담고있는 session 이라는 그릇 자체를 무효화 시키는 방법이라서
		//    loginUser 이외의 데이터들까지 싹 다 날라가게됨!!)
		
		// * session 에 반드시 로그인한 회원의 정보만 담으라는 법음 없다!!
		// > 1회성 alert 문구를 session 에 담아보자!!
		session.setAttribute("alertMsg", "성공적으로 로그아웃이 되었습니다.");
		
		// url 재요청 방식으로 메인페이지가 보여지게끔
		return "redirect:/";
		
	}
	
	// @GetMapping("member/enrollForm")
	@GetMapping("enrollForm")
	public String enrollForm() {
		
		// 회원가입 페이지만 띄워주고 끝
		return "member/memberEnrollForm";
		// > /WEB-INF/views/member/memberEnrollForm.jsp
	}
	
	// @PostMapping("member/insert")
	@PostMapping("insert")
	public String insertMember(Member m, Model model, HttpSession session) {
		
		// 1. 요청 시 전달값 얻어내기
		// > 커맨드 객체 방식으로 얻어내고 VO 로 가공까지 완료함!!
		
		/*
		 * 문제점)
		 * - 비밀번호가 사용자가 입력했던 텍스트 그대로 저장됨!! (절대로 이런 일이 있어서는 안됨)
		 * 
		 * 원인 : 없음 (요청 시 전달값이 제대로 넘어오는 것이 당연하기 때문)
		 * 조치 : 비밀번호를 암호문으로 바꿔서 저장할 것 (salt 값을 쳐서 단방향 방식으로)
		 *		 스프링에서 제공하는 BCrypt 라는 방식의 암호화 모듈을 이용해볼것!!
		 *		 
		 * > BCrypt 방식 : 평문을 그냥 암호문으로 바꿔주는게 아니라,
		 * 				  평문에 매번 다른 랜덤값 (salt) 를 붙여서 단방향 방식으로 암호화를 해줌
		 * 
		 * > 평문 : 사용자가 입력했던 텍스트 원본 그대로
		 * > 암호문 : 평문을 한번에 보기 난해하게 바꿔둔 문자열 텍스트
		 * > 이 때, 평문 --> 암호문 으로 바꾸는 과정을 "암호화" 라고 한다.
		 * > 반대로 암호문 --> 평문 으로 원상복귀 해주는 과정을 "복호화" 라고 한다.
		 * > 암호화 과정에서 그냥 평문을 암호문으로 바꿔도 되지만,
		 *   이왕이면 평문에 랜덤값을 붙여서 암호문으로 바꿔줘도 된다!!
		 *   (알고리즘 구조를 파악하지 못하게끔 교란 장치를 걸어주는 꼴)
		 *   이 때, 이 랜덤값을 "salt" 라고 부른다.
		 * 
		 * > 암호화를 하는 방법은 여러 가지가 있다. (암호화 알고리즘)
		 *   크게 2가지 종류가 있는데
		 *   1) 양방향 암호화 알고리즘 : 암호화 + 복호화 과정 둘 다 제공
		 *   2) 단방향 암호화 알고리즘 : 암호화 과정만 제공 (복호화 X)
		 *
		 * - 우리는 그동안 평문 비밀번호를 그대로 테이블에 INSERT 해서 저장했었음!!
		 * - 보안 상의 취약점을 줄이기 위해 비밀번호를 암호문으로 변경한 후 INSERT 할 것!!
		 * - 우리는 단방향 암호화 알고리즘을 이용해서 비밀번호를 암호문으로 바꿀 것!!
		 *   (즉, 저장된 비밀번호를 평문으로 되돌릴 수 없다!!)
		 * - 이왕이면 salt 값까지 임의로 붙여서 암호화 해줄 것!!
		 */
		
		// 암호화 작업 후
		// System.out.println("평문 : " + m.getUserPwd());
		
		// 평문 비밀번호를 암호화 작업 해보기
		// > Spring Security 모듈 에서 제공하는 BCryptPasswordEncoder 객체를 이용해야함!!
		//   (이 Spring Security 모듈을 우선 jar 형태로 연동 필수!! - pom.xml)
		
		// [ 표현법 ]
		// bCryptPasswordEncoder.encode("평문") : String (암호문)
		
		String encPwd = bCryptPasswordEncoder.encode(m.getUserPwd());
		// System.out.println("암호문 : " + encPwd);
		// > 아무리 평문이 같더라도 매번 salt 를 붙여서 암호화를 하기 때문에
		//   매번 다른 암호문이 나온다!!
		// > 이미 저장된 더미데이터의 비번도 다 암호문 형태로 바꿔둠!!
		
		// 2. 서비스단으로 전달값을 넘기면서 호출 후 결과 받기
		// > 그 전에 이 암호화된 비번을 Member m 의 userPwd 필드에 담기
		// System.out.println("before : " + m);
		
		// 비번 필드값 변경
		m.setUserPwd(encPwd);
		// System.out.println("after : " + m);
		
		// > 최종적으로 셋팅된 Member 객체를 Service 로 넘기면서 요청 후 결과 받기
		int result = memberService.insertMember(m);
		
		// 3. 결과에 따른 응답페이지 지정
		if(result > 0) {
			// > 회원가입 성공
			
			// 1회성 알림문구를 담아서 메인페이지로 url 재요청
			session.setAttribute("alertMsg", "성공적으로 회원가입이 되었습니다.");
			
			return "redirect:/";
			
		} else { 
			// > 회원가입 실패
			
			// 에러 문구를 담아서 에러페이지로 포워딩
			model.addAttribute("errorMsg", "회원가입에 실패했습니다.");
			
			return "common/errorPage";
		}
		
		// 암호화 작업 전
		/*
		// 2. 서비스단으로 전달값을 넘기면서 호출 후 결과 받기
		int result = memberService.insertMember(m);
		
		// 3. 결과에 따른 응답페이지 지정
		if(result > 0) {
			// > 회원가입 성공
			
			// 1회성 알림 문구를 담아서 메인페이지로 url 재요청
			session.setAttribute("alertMsg", "성공적으로 회원가입이 되었습니다.");
			
			return "redirect:/";
			
		} else {
			// > 회원가입 실패
			
			// 에러 문구를 담아서 에러페이지로 포워딩
			model.addAttribute("errorMsg", "회원가입에 실패했습니다.");
			
			return "common/errorPage";
			// > /WEB-INF/views/common/errorPage.jsp
		}
		*/
		
	}
	
	// @GetMapping("member/myPage")
	@GetMapping("myPage")
	public ModelAndView myPage(ModelAndView mv) {
		
		// myPage.jsp 에서 각 input 요소의 value 속성값으로
		// 현재 로그인한 사용자의 각각의 알맞은 정보들이 출력되야함!!
		// > 응답페이지에서 필요로 하는 데이터 == 현재 로그인한 회원의 데이터
		
		// > 현재 로그인한 사용자의 정보를 DB 로 부터 조회해서 갖고 온 다음에
		//   Model 에 응답데이터로 담아도 되지만, 굳이 DB 까지 다녀올 필요는 없음!!
		// > 왜? 마이페이지는 로그인한 사용자만 접속 가능하다!!
		//   로그인한 상태라면 기본적으로 이미 session 에 해당 회원의 정보가 통으로 담겨있음!!
		//   세션 객체는 전역변수의 특징을 갖기 때문에 굳이 응답데이터로 한번 더 보내줄 필요가 없이
		//   그냥 바로 jsp 페이지에서 session 에 접근해서 값을 뽑아오면됨!!
		
		// 우선 마이페이지 화면을 만들어서 띄워보자!!
		mv.setViewName("member/myPage");
		// > /WEB-INF/views/member/myPage.jsp
		
		return mv;
	}
	
	@PostMapping("update")
	// > 위에서 클래스 상단에 @RequestMapping 으로 "member" 라는 공용주소를 설정했기 때문에
	//   메소드 단위에서는 이제 말단 주소만 잘 셋팅해주면 된다!!
	public ModelAndView updateMember(Member m, ModelAndView mv, HttpSession session) {
		
		// 1. 요청 시 전달값 얻어내기
		// System.out.println(m);
		// > 커맨드 객체 방식으로 잘 넘어옴!! (아이디, 이름, 전화번호, 이메일, 주소)
		//   아이디만 고정이고, 이름, 전화번호, 이메일, 주소는 변경할 수 있는 값임!!
		
		// 2. 그대로 Service 로 넘기면서 메소드 호출 후 결과 반환
		int result = memberService.updateMember(m);
		
		// 3. 결과에 따른 응답페이지 처리
		if(result > 0) {
			// > 회원 정보 변경 성공
			
			// 갱신된 회원의 정보를 다시 조회해와서 세션에 덮어씌운 후
			// > 기존의 로그인용 서비스를 재활용 해서 단순히 호출해서 쓸 것!!
			//   (아까 로그인용 쿼리문에서 아이디가 일치하고 STATUS = 'Y' 일 경우만 조회되도록 수정했음)
			Member updateMem = memberService.loginMember(m);
			
			session.setAttribute("loginUser", updateMem);
			// > session 에 이미 loginUser 라는 키 + 밸류로 갱신 전 회원의 정보가 담겨있는 상황
			//   loginUser 키값으로 갱신된 회원의 정보를 다시 setAttribute 하면
			//   동일한 키값으로 데이터가 덮어씌워진다!! (HashMap 과 동일)
			
			// 일회성 문구를 담아서 마이페이지로 url 재요청 
			// > 우리가 마이페이지에서 내 정보 조회 기능을 별도로 따로 조회해서 출력하는게 아니라
			//   이미 세션에 담겨있던 해당 회원의 정보를 그냥 출력해줬기 때문에
			//   회원 정보 변경이 일어난 후 갱신된 정보로 다시 세션에 덮어씌워주는것 까지 해줘야함!!
			session.setAttribute("alertMsg", "성공적으로 회원 정보가 변경되었습니다.");
			
			// ModelAndView 방식으로도 url 재요청이 가능!!
			mv.setViewName("redirect:/member/myPage");
			// > 똑같이 "redirect:url주소" 를 적는다!!
			
		} else {
			// > 회원 정보 변경 실패
			
			// 에러 문구를 담아서 에러페이지로 포워딩
			mv.addObject("errorMsg", "회원정보 변경에 실패했습니다.");
			
			mv.setViewName("common/errorPage");
			// > /WEB-INF/views/common/errorPage.jsp
		}
		
		return mv;
		
	}
	
	@PostMapping("updatePwd")
	public String updatePwd(String userId, String userPwd, String updatePwd, HttpSession session) {
		
		// System.out.println(userPwd);
		// System.out.println(updatePwd);
		
		// > 쿼리문을 미리 짜봤더니 
		//   해당 회원(== 비번을 바꾸고자 하는 회원 == 현재 로그인한 회원) 의 아이디도 필요함!!
		
		// * 현재 로그인한 회원의 정보를 알아내는 방법
		// 1. HttpSession 객체로부터 꺼내오는 방법
		// String userId = ((Member)(session.getAttribute("loginUser"))).getUserId();
		// System.out.println(userId);
		
		// 2. form 태그 내부에서 <input type="hidden"> 을 통해 로그인한 회원의 정보를 넘기는 방법
		// System.out.println(userId);
		
		// > 평문 아이디, 평문 현재의 비밀번호, 평문 바꿀 비밀번호
		
		// 우선 사용자가 입력한 평문 현재의 비밀번호와 
		// 세션에 담겨있는 현재 로그인한 사용자의 암호화된 비밀번호가 맞아 떨어지는지 대조
		Member loginUser = (Member)(session.getAttribute("loginUser"));
		
		if(bCryptPasswordEncoder.matches(userPwd, loginUser.getUserPwd())) {
			// > 평문과 암호문 비밀번호가 맞아 떨어질 경우 
			
			// 비밀번호 변경 요청 서비스 호출 후 결과 받기
			// > 변경할 비밀번호 또한 암호문 형태로 변경해야한다!!
			String updateEncPwd = bCryptPasswordEncoder.encode(updatePwd);
			
			// 아이디와 변경할 비밀번호의 암호문을 넘기면서 서비스 호출 및 결과 받기
			// > 두 개 이상의 값을 한번에 넘길 경우에는 무조건 VO 등으로 가공해서 한번에 넘긴다!!
			Member m = new Member();
			m.setUserId(userId);
			m.setUserPwd(updateEncPwd);
			
			int result = memberService.updatePwd(m);
			
			// 처리된 결과에 따라 사용자가 보게 될 응답페이지를 지정
			if(result > 0) { 
				// > 비밀번호 변경 성공
				
				// 현재 로그인한 회원의 정보가 조금이라도 변경되었다면 
				// 무조건 그 갱신된 정보를 다시 불러와서 세션에 덮어씌워야함!!
				// > 기존의 로그인용 서비스 재활용하기
				Member updateMem = memberService.loginMember(m);
				
				session.setAttribute("loginUser", updateMem);
				// > 동일한 키값으로 한번 더 추가를 하면 밸류가 덮어씌워짐!!
				
				// 비밀번호가 잘 변경되었음을 1회성 알림 문구로 담아줄 것
				session.setAttribute("alertMsg", "성공적으로 비밀번호가 변경되었습니다.");
				
			} else {
				// > 비밀번호 변경 실패
				
				// 1회성 알림문구를 담기
				session.setAttribute("alertMsg", "비밀번호 변경에 실패했습니다.");
			}
			
		} else {
			// > 평문과 암호문 비밀번호가 맞아 떨어지지 않을 경우
			//   (사용자가 현재 비밀번호를 잘못 입력한 경우)
			
			// 1회성 알림 문구로 잘못입력했다고 알려주기
			session.setAttribute("alertMsg", "잘못된 비밀번호입니다. 다시 입력해주세요.");
		}
		
		// 뭐가 되었든 간에 마이페이지로 url 재요청
		return "redirect:/member/myPage";
	}
	
	@PostMapping("delete")
	public String deleteMember(String userPwd, HttpSession session, Model model) {
		
		// 우선 사용자가 입력한 평문 비밀번호와 세션에 담겨있는 암호화된 기존의 비밀번호를 대조하기
		Member loginUser = (Member)(session.getAttribute("loginUser"));
		
		if(bCryptPasswordEncoder.matches(userPwd, loginUser.getUserPwd())) {
			// > 평문과 암호문 비밀번호가 맞아 떨어질 경우
			
			// 회원 탈퇴 서비스 요청 후 결과 받기
			int result = memberService.deleteMember(loginUser.getUserId());
			
			// 탈퇴 처리 결과에 따른 응답 페이지 지정
			if(result > 0) { 
				// > 탈퇴 성공
				
				// 로그아웃 처리 후 일회성 알림 문구를 담고 메인페이지로 url 재요청
				session.removeAttribute("loginUser");
				
				session.setAttribute("alertMsg", "성공적으로 회원 탈퇴 처리 되었습니다. 그동안 이용해 주셔서 감사합니다.");
				
				return "redirect:/";
				
			} else {
				// > 탈퇴 실패
				
				// 에러문구를 담아서 에러페이지로 포워딩
				model.addAttribute("errorMsg", "회원 탈퇴에 실패했습니다.");
				
				return "common/errorPage";
			}
			
		} else {
			// > 평문과 암호문 비밀번호가 다를 경우
			//   (현재 비밀번호를 잘못 입력한 경우)
			
			// 1회성 알림 문구로 잘못 입력했음을 알려주고, 마이페이지로 url 재요청
			session.setAttribute("alertMsg", "잘못된 비밀번호입니다. 다시 입력해주세요.");
			
			return "redirect:/member/myPage";
		}
		
	}
	
	@ResponseBody
	@GetMapping("idCheck")
	public String ajaxIdCheck(String checkId) {
		// System.out.println(checkId);

		int count = memberService.idCheck(checkId);

		// System.out.println(count);

		return (count > 0) ? "NNNNN" : "NNNNY";
	}
	
}






