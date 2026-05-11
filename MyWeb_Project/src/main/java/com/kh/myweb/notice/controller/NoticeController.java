package com.kh.myweb.notice.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.kh.myweb.common.template.XssDefencePolicy;
import com.kh.myweb.notice.model.service.NoticeService;
import com.kh.myweb.notice.model.vo.Notice;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("notice")
public class NoticeController {
	
	@Autowired
	private NoticeService noticeService;
	
	@GetMapping("list")
	public String selectNoticeList(Model model) {
		
		// 공지사항 리스트 조회 페이지에서 필요로 하는 응답데이터를 구해와야함!!
		// > NOTICE 테이블로부터 SELECT 된 결과물들
		ArrayList<Notice> list = noticeService.selectNoticeList();
		// > 여러행 조회이므로 ArrayList<Notice> 로 받기!!
		
		/*
		for(Notice n : list) {
			
			System.out.println(n);
		}
		*/
		
		model.addAttribute("list", list);
		
		// 응답페이지를 먼저 만들어서 포워딩
		return "notice/noticeListView";
		// > /WEB-INF/views/notice/noticeListView.jsp
	}
	
	@GetMapping("enrollForm")
	public String enrollForm() {
		
		// 공지사항 작성 페이지만 보여주고 끝
		return "notice/noticeEnrollForm";
		// > /WEB-INF/views/notice/noticeEnrollForm.jsp
	}
	
	@PostMapping("insert")
	public String insertNotice(Notice n, Model model, HttpSession session) {
		
		System.out.println(n);
		// > 글 제목이나 내용 등에 html 태그 형식이 들어가면
		//   상세조회 시 진짜 태그로써 해석되서 화면에 출력됨!! 
		//   (또한 js 구문, css 까지 싹 다 반영됨)
		
		/*
		 * * XSS (Cross-Site-Scripting) 공격
		 * - 웹 사이트 페이지 상에 악성 스크립트를 삽입하는 공격
		 * - 글 제목이나 내용 등에 html 태그, js 코드, css 스타일 등을 삽입
		 *   (그 중에서도 js 구문 삽입이 제일 심각)
		 *   
		 * * 조치방법
		 * - 요청 시 전달값에 < 를 &lt; 으로, > 를 &gt; 로 변경해서 저장
		 * 
		 * * 시큐어 코딩
		 * - 개발 과정에서 보안 취약점을 미리 제거해
		 *   최대한 안전한 소프트웨어를 개발자 선에서 만드는 기법
		 * 예) PreparedStatement 쓰기 - SQL Injection 공격 방지
		 *     html 예약어 변경 - XSS 공격 방지
		 * 	   비밀번호 암호화
		 * 	   등등..
		 */
		
		// XSS 공격 방지용 공통코드 작업 진행!!
		// > XssDefencePolicy 클래스의 defence 메소드 (static)
		String replaceTitle 
			= XssDefencePolicy.defence(n.getNoticeTitle());
		
		String replaceContent 
			= XssDefencePolicy.defence(n.getNoticeContent());
		
		// > 각각 치환된 결과를 각 필드로 셋팅!!
		n.setNoticeTitle(replaceTitle);
		n.setNoticeContent(replaceContent);
		
		System.out.println(n);
		
		// Service 로 전달값을 넘기면서 요청 후 결과 받기
		int result = noticeService.insertNotice(n);
		
		// 처리된 결과에 따라 사용자가 보게 될 응답페이지를 지정
		if(result > 0) { 
			// > 공지사항 등록 성공
			
			// 1회성 알림 문구를 담아서 공지사항 목록 페이지로 url 재요청
			session.setAttribute("alertMsg", "성공적으로 공지사항이 등록되었습니다.");
			
			return "redirect:/notice/list";
			
		} else { 
			// > 공지사항 등록 실패
		
			// 에러문구를 담아서 에러페이지로 포워딩
			model.addAttribute("errorMsg", "공지사항 등록에 실패했습니다.");
			
			return "common/errorPage";
		}
		
	}
	
	// 쿼리스트링 방식 적용
	// @GetMapping("detail")
	// public String selectNotice(@RequestParam("nno") int noticeNo, Model model) {
		
	// Path Variable 방식 적용
	@GetMapping("detail/{nno}")
	public String selectNotice(@PathVariable(value="nno") int noticeNo, Model model) {
	
		// System.out.println("글번호 : " + noticeNo);
		
		// 게시판 형식에서 상세보기 요청 시
		// 일단 해당 게시글의 조회수를 1 증가시키고 나서
		// 그다음에 해당 게시글을 단일행 조회 해오는 식으로 구현!!
		
		// 먼저 조회수를 증가시키는 Service 로 요청
		int result = noticeService.increaseCount(noticeNo);
		
		// 공지사항 조회수 증가에 성공했다면
		// 해당 게시글을 한건만 정확하게 불러와서 (조회해와서) 응답데이터로 넘기기
		if(result > 0) {
			// > 조회수 증가에 성공했다면
			
			// 해당 게시글 정보를 담아서 상세보기 페이지로 포워딩
			Notice n = noticeService.selectNotice(noticeNo);
			// > 단일행 조회이므로 Notice 로 받아야함!!
			
			model.addAttribute("n", n);
			
			// 먼저 상세보기 페이지 띄워보기
			return "notice/noticeDetailView";
			// > /WEB-INF/views/notice/noticeDetailView.jsp
			
		} else {
			// > 조회수 증가에 실패했다면
			
			// 에러문구를 담아서 에러페이지로 포워딩
			model.addAttribute("errorMsg", "공지사항 상세조회에 실패했습니다.");
			
			return "common/errorPage";
		}
		
	}
	
	@PostMapping("updateForm")
	public ModelAndView updateForm(@RequestParam("nno") int noticeNo, ModelAndView mv) {
		
		// System.out.println("글번호 : " + noticeNo);
		
		// 요청 시 전달값인 글번호를 통해 기존의 해당 공지사항 내용들을 조회해오기
		// > 기존 공지사항 상세보기 서비스를 재활용
		Notice n = noticeService.selectNotice(noticeNo);
		// > noticeNo, noticeTitle, noticeContent
		
		mv.addObject("n", n);
		
		// 먼저 공지사항 수정 페이지 띄워보기
		mv.setViewName("notice/noticeUpdateForm");
		// > /WEB-INF/views/notice/noticeUpdateForm.jsp
		
		return mv;
	}
	
	@PostMapping("update")
	public ModelAndView updateNotice(Notice n, ModelAndView mv, HttpSession session) {
		
		// System.out.println(n);
		
		// > 마찬가지로 공지사항 게시글 수정 시에도 XSS 공격 방지를 미리 해줘야 한다.
		//   (공통코드 작업)
		String replaceTitle 
			= XssDefencePolicy.defence(n.getNoticeTitle());
		
		String replaceContent
			= XssDefencePolicy.defence(n.getNoticeContent());
		
		n.setNoticeTitle(replaceTitle);
		n.setNoticeContent(replaceContent);
		
		int result = noticeService.updateNotice(n);
		
		// 결과에 따른 응답페이지 처리
		if(result > 0) {
			// > 공지사항 수정 성공
			
			// 1회성 알림 문구를 담아서 해당 게시글의 상세보기 페이지로 url 재요청
			session.setAttribute("alertMsg", "성공적으로 공지사항이 수정되었습니다.");
			
			// 쿼리스트링 방식 적용
			// mv.setViewName("redirect:/notice/detail?nno=" + n.getNoticeNo());
			// > url 재요청 방식일 경우에도 필요하다면 직접 쿼리스트링을 명시한다!!
			
			// Path Variable 방식 적용
			mv.setViewName("redirect:/notice/detail/" + n.getNoticeNo());
			
		} else {
			// > 공지사항 수정 삭제
			
			// 에러문구를 담아서 에러페이지로 포워딩
			mv.addObject("errorMsg", "공지사항 수정에 실패했습니다.");
			
			mv.setViewName("common/errorPage");
			
		}
		
		return mv;
	}
	
	@PostMapping("delete")
	public String deleteNotice(@RequestParam("nno") int noticeNo, Model model, HttpSession session) {
		
		// System.out.println(noticeNo);
		
		int result = noticeService.deleteNotice(noticeNo);
		
		if(result > 0) {
			// > 공지사항 삭제 성공
			
			// 1회성 알림 문구를 담아 공지사항 목록 페이지로 url 재요청
			session.setAttribute("alertMsg", "성공적으로 공지사항이 삭제되었습니다.");
			
			return "redirect:/notice/list";
			
		} else {
			// > 공지사항 삭제 실패
			
			// 에러문구를 담아서 에러페이지로 포워딩
			model.addAttribute("errorMsg", "공지사항 삭제에 실패했습니다.");
			
			return "common/errorPage";
		}
		
	}

}






