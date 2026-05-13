package com.kh.myweb.board.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.kh.myweb.board.model.service.BoardService;
import com.kh.myweb.board.model.vo.Attachment;
import com.kh.myweb.board.model.vo.Board;
import com.kh.myweb.common.model.vo.PageInfo;
import com.kh.myweb.common.template.Pagination;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("board")
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	@GetMapping("list")
	public ModelAndView selectBoardList(@RequestParam(value="cpage", defaultValue = "1") int currentPage, ModelAndView mv) {
//		System.out.println("currentPage : " + currentPage);
		
//		일반 게시판 목록 조회 기능 구현(+페이징 처리)
		
//		페이징처리
//		: 리스트 조회 시 한 페이지 당 조회돌 게시글 수 지정
		
//		> 변수 7개 필요(4+3)
		int listCount; 		// 총 게시글 수
//		int currentPage;	// 현재 페이지 번호
		int pageLimit; 		// 한 페이지 당 보여질 최대 페이지 버튼 수
		int boardLimit; 	// 한 페이지 당 보여질 최대 게시글 수
		
//		int maxPage; 		// 가장 마지막 페이지 번호(총 페이지 수)
//		int startPage; 		// 하단 페이지 버튼의 시작 번호
//		int endPage; 		// 하단 페이지 버튼의 끝 번호
		
		listCount = boardService.selectListCount();
//		System.out.println("listCount : " + listCount);
//		currentPage = 매개변수로 이미 받음
		pageLimit = 10;
		boardLimit = 10;
		
//		-- Pagination class로 이동 --
		
//		매번 listCount, currentPage, pageLimit, boardLimit, maxPage, startPage, endPage 7개 변수를 계산 및 코드 작성 귀찮음
//		> 공통 코드 작업으로 Pagination 클래스 생성하고 그 안에 getPageInfo() 메소드 만들어 리턴하는 방식 이용
		
		
//		위의 7개 변수를 하나의 객체(VO)로 관리하기 위해 PageInfo 클래스 생성
//		한번 만들어두면 유사한 목록 조회 기능 구현 시 재사용 가능
		/*
		PageInfo pi = new PageInfo(listCount, currentPage, pageLimit, boardLimit, 
								   maxPage, startPage, endPage);
		*/
		PageInfo pi = Pagination.getPageInfo(listCount, currentPage, pageLimit, boardLimit);
//		pi를 전달하면서 Service를 요청 후 리턴
		ArrayList<Board> list = boardService.selectBoardList(pi);
		
		/*
		for (Board b : list) {
			System.out.println(b);
		}
		*/
		
		mv.addObject("list", list);
		mv.addObject("pi", pi);
		
		mv.setViewName("board/boardListView");
		
		return mv;
	}
	
	@GetMapping("search")
	public ModelAndView searchBoardList(String condition, String keyword, ModelAndView mv,
										@RequestParam(value="cpage", defaultValue = "1") int currentPage) {
//		System.out.println("condition : " + condition);
//		System.out.println("keyword : " + keyword);
		
//		+) paging 기능도 필요함
		HashMap<String, String> map = new HashMap<>();
		map.put("condition", condition);
		map.put("keyword", keyword);
		
		int searchCount = boardService.selectSearchCount(map);
//		System.out.println("searchCount : " + searchCount);
//		System.out.println("currentPage : " + currentPage);
		
		int pageLimit = 10;
		int boardLimit = 10;
		
		PageInfo pi = Pagination.getPageInfo(searchCount, currentPage, pageLimit, boardLimit);
		
//		HashMap과 PageInfo 둘다 넘겨서 검색 조건과 페이징 처리 모두 적용된 게시글 목록 조회
		ArrayList<Board> list = boardService.searchBoardList(map, pi);
		
		/*
		for (Board b : list) {
			System.out.println(b);
		}
		*/
		
		/*
		mv.addObject("list", list);
		mv.addObject("pi", pi);
//		검색 결과도 일반 게시글 목록과 같은 화면으로 보여줄 예정이므로, 뷰 이름은 동일하게 boardListView로 지정
//		문제는 검색 결과 창에 검색 조건, 검색어가 보여지지 않음
//		추가로 paging 바 클릭하면, 검색 조건, 검색어가 사라지면서 일반 게시글 목록으로 이동하는 문제 발생
		mv.addObject("condition", condition);
		mv.addObject("keyword", keyword);
		
		mv.setViewName("board/boardListView");
		*/
		
//		ModelAndView의 addObject() 메소드는 return 타입이 ModelAndView
//		> 메소드 체이닝 방식으로 addObject() 메소드를 여러 번 호출해서 객체 담을 수 있음
		mv.addObject("list", list)
		  .addObject("pi", pi)
		  .addObject("condition", condition)
		  .addObject("keyword", keyword)
		  .setViewName("board/boardListView");
//		.setViewName()은 return 타입이 void이므로, 가장 마지막에만 작성 필요
		
		return mv;
	}
	
	@GetMapping("enrollForm")
	public String enrollForm() {
		return "board/boardEnrollForm";
	}
	
	@PostMapping("insert")
	public void insertBoard(Board b, MultipartFile upfile, HttpSession session) {
//		System.out.println(b);
//		카테고리번호, 제목, 내용은 Board 객체에 담겨서 전달됨
//		사용자 id는 session에서 꺼낼 수도 있지만 hidden으로 넘기는게 편함
		
//		System.out.println(upfile);
//		파일 업로드는 MultipartFile 객체로 전달됨 > jsp form 태그에서도 enctype="multipart/form-data" 작성 필요
//		추가적으로 파일 업로드와 관련된 Spring 설정을 추가해야함(application.properties)
		
//		첨부파일의 파일명 리턴 / 첨부파일이 없으면 빈 문자열 리턴
//		System.out.println(upfile.getOriginalFilename());
//		첨부파일의 크기 리턴
//		System.out.println(upfile.getSize());
		
//		upfile로 받은 첨부파일의 정보를 Attachment VO 객체로 가공
		Attachment at = null;
		
		if(!upfile.getOriginalFilename().equals("")) {
//			System.out.println("첨부파일 있음");
//			> 파일명 수정 후 서버로 업로드
//			- 동일한 이름의 파일이 업로드 될 경우 기존 파일이 덮어씌워지는 문제 방지 위해서
//			> 년월시분초+랜덤5자리+확장자명
			
//			1. 원본 파일명
			String originName = upfile.getOriginalFilename();
			
//			2. 현재 시간 문자열로
			String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			
//			3. 랜덤 숫자 5자리
			int ranNum = (int)(Math.random() * 90000 + 10000);
			
//			4. 확장자명
			String ext = originName.substring(originName.lastIndexOf("."));
			
//			5. 변경된 파일명
			String changeName = currentTime + ranNum + ext;
			
//			6. 서버 업로드 경로(서버 폴더의 물리적인 경로)
//			> 이 프로젝트 폴더 내부에 저장할 것
//			( 이 프로젝트 1개 = 웹사이트 1개 = applicationScope )
//			> applicationScope 내강객체(ServletContext 타입)로부터 저장할 경로 알아내기
//			> session에서 얻어낼 수 있음
//			String savePath = session.getServletContext().getRealPath("/resources/uploadFiles/");
			String savePath = session.getServletContext().getRealPath("resources/board_upfiles/");
//			이렇게 안하면 사용자 로컴 컴퓨터 해당 경로에 저장됨
//			주소 뒤에 / 가 붙으면 해당 폴더 안에 저장을 의미
			
//			7. 업로드
			try {
				upfile.transferTo(new File(savePath + changeName));
			} catch (IOException e) {
				e.printStackTrace();
			}

//			8. Attachment 객체로 가공
			at = new Attachment();
			at.setOriginName(originName);
			at.setChangeName(changeName);
			at.setFilePath("resources/board_upfiles/");
		} else {
			System.out.println("첨부파일 없음");
		}
		
//		System.out.println(b);
//		System.out.println(at);
		int result = boardService.insertBoard(b, at);
	}
	
}
