package com.kh.myweb.board.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.kh.myweb.board.model.service.BoardService;
import com.kh.myweb.board.model.vo.Attachment;
import com.kh.myweb.board.model.vo.Board;
import com.kh.myweb.board.model.vo.Category;
import com.kh.myweb.common.model.vo.PageInfo;
import com.kh.myweb.common.template.FileRenamePolicy;
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
	public String enrollForm(Model model) {
		
		// 일반게시글 등록 화면을 띄울 때
		// 사용자가 추가하고자 하는 카테고리명들이 jsp 상에 하드코딩 되어있음!!
		// > 만약, 카테고리 정보가 빈번하게 DB 상에 추가, 수정, 삭제될 일이 있다면?
		//   그 때 마다 select - option 태그를 찾아다니면서 일일이 코드를 수정해야함!!
		// > Category 테이블의 내용을 전체 조회 해서 응답데이터로 넘기기
		ArrayList<Category> list = boardService.selectCategoryList();
		
		/*
		for(Category c: list) {
			
			System.out.println(c);
		}
		*/
		
		model.addAttribute("list", list);
		
		return "board/boardEnrollForm";
		// > /WEB-INF/views/board/boardEnrollForm.jsp
	}
	
	@PostMapping("insert")
	public String insertBoard(Board b, MultipartFile upfile, HttpSession session, Model model) {
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
			// > 넘어온 첨부파일이 있을 경우
			
			String changeName = FileRenamePolicy.saveFile(upfile, session, "/resources/board_upfiles/");
			
			// 8. 첨부파일에 대한 정보를 Attachment at 객체에 담기
			at = new Attachment();
			at.setOriginName(upfile.getOriginalFilename()); // 원본명 (사용자가 넘긴 이름)
			at.setChangeName(changeName); // 수정명 (실제 서버에 업로드 되어있는 이름)
			at.setFilePath("resources/board_upfiles/");
		}
		
//		System.out.println(b);
//		System.out.println(at);
		int result = boardService.insertBoard(b, at);
		
		// 결과에 따른 응답페이지 처리
		if(result > 0) { 
			// > 최종 성공일 경우
			
			// 1회성 알림 문구를 담아 일반게시판 목록 1번페이지로 url 재요청 (이동)
			session.setAttribute("alertMsg", "성공적으로 게시글이 등록되었습니다.");
			
			return "redirect:/board/list";
			
		} else {
			// > 실패한 경우
			
			// 첨부파일이 있었을 경우 이미 업로드된 파일을 굳이 서버에 보관할 이유가 없음!!
			// > 서버 용량만 차지
			if(at != null) {
				// > 첨부파일이 있었을 경우
				
				// 삭제시키고자 하는 파일 객체 생성 후 delete 메소드 호출
				
				String savePath = session.getServletContext()
										 .getRealPath("/resources/board_upfiles/");
				
				new File(savePath + at.getChangeName()).delete();
			}
			
			// 에러문구를 담아서 에러페이지로 포워딩
			model.addAttribute("errorMsg", "게시글 작성에 실패했습니다.");
			
			return "common/errorPage";
		}
	}
	
	@GetMapping("detail/{boardNo}")
	public String selectBoard(@PathVariable("boardNo") int boardNo, Model model) {
//		System.out.println("boardNo : " + boardNo);
		
		// System.out.println(boardNo);
		
		// 우선 해당 게시글의 글번호를 넘기면서 조회수만 먼저 증가하고 돌아오기
		int result = boardService.increaseCount(boardNo);
		
		// 조회수 증가에 성공했다면 그제서야 상세조회로 들어가기!!
		if(result > 0) {
			// > 조회수 증가 성공
			
			// 조회수 증가에 성공했다면 해당 게시글을 조회해오기
			Board b = boardService.selectBoard(boardNo);
			
			// 또한 해당 게시글에 딸린 첨부파일 정보도 조회해오기
			Attachment at = boardService.selectAttachment(boardNo);
			// > 일반게시글은 게시글 하나당 첨부파일도 최대 1개이므로 단일행 조회임
			
			// 조회해온 위의 정보들을 응답데이터로 넘기기
			model.addAttribute("b", b);
			model.addAttribute("at", at);
			
			return "board/boardDetailView";
			// > /WEB-INF/views/board/boardDetailView.jsp
			
		} else {
			// > 조회수 증가 실패
			
			// 조회수 증가에 실패했다면 에러문구를 담아서 에러페이지로 포워딩
			model.addAttribute("errorMsg", "게시글 상세조회에 실패했습니다.");
			
			return "common/errorPage";
		}
	}
	
	@PostMapping("delete")
	public String deleteBoard(@RequestParam("bno") int boardNo, 
							  Model model, HttpSession session) {
		
		// System.out.println(boardNo);
		
		int result = boardService.deleteBoard(boardNo);
		
		if(result > 0) { 
			// > 게시글 삭제 성공
			
			// 도전문제 - 첨부파일이 있는 경우도 생각해보면 좋을 것 같음!! (옵션)
			// > 게시글 복구의 여지를 남겨두기 위해 첨부파일 관련된 것은 하나도 안건들였음!!
			
			// 일회성 알림 문구를 담아서 일반게시판 목록 1번 페이지로 url 재요청
			session.setAttribute("alertMsg", "성공적으로 게시글이 삭제되었습니다.");
			
			return "redirect:/board/list";
			
		} else {
			// > 게시글 삭제 실패
			
			// 에러 문구를 담아서 에러페이지로 포워딩
			model.addAttribute("errorMsg", "게시글 삭제에 실패했습니다.");
			
			return "common/errorPage";
		}
	}
	
	@PostMapping("updateForm")
	public ModelAndView updateForm(@RequestParam("bno") int boardNo, 
								   ModelAndView mv) {
		
		// 수정하기 페이지에서도 똑같이 카테고리 정보를 넘겨줄 것!!
		ArrayList<Category> list = boardService.selectCategoryList();
		
		// 수정하기 페이지에서는 기존의 제목, 내용 등이 미리 보여야함!!
		// > 해당 게시글을 조회해서 응답데이터로 보내줄 것!!
		
		// 기존 게시글 정보 먼저 조회해오기
		Board b = boardService.selectBoard(boardNo);
		// > boardNo, category, boardTitle, boardWriter, boardContent, createDate
		//   (기존 상세보기 서비스 재활용)
		
		// 기존 첨부파일 정보도 조회해오기
		Attachment at = boardService.selectAttachment(boardNo);
		// > fileNo, originName, changeName, filePath
		//   (기존 첨부파일 조회 서비스 재활용)
		
		mv.addObject("b", b)
		  .addObject("at", at)
		  .addObject("list", list)
		  .setViewName("board/boardUpdateForm");
		// > /WEB-INF/views/board/boardUpdateForm.jsp
		
		return mv;
	}
	
}
