package com.kh.myweb.board.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.kh.myweb.board.model.service.BoardService;
import com.kh.myweb.board.model.vo.Attachment;
import com.kh.myweb.board.model.vo.Board;
import com.kh.myweb.board.model.vo.Category;
import com.kh.myweb.board.model.vo.Reply;
import com.kh.myweb.common.model.vo.PageInfo;
import com.kh.myweb.common.template.FileRenamePolicy;
import com.kh.myweb.common.template.Pagination;
import com.kh.myweb.member.model.vo.Member;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("board")
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	@GetMapping("list")
	public ModelAndView selectBoardList(@RequestParam(value="cpage", defaultValue="1") int currentPage, ModelAndView mv) {
		
		// System.out.println("현재 요청한 페이지 : " + currentPage);
		
		// 일반게시판 목록 조회 기능 구현 (+ 페이징 처리)

		// * 페이징 처리 (Pagination)
		// > 리스트 조회 시 한 페이지 당 조회할 건수가 너무 많을 때
		//   한 페이지 당 n 개씩 끊어서 보여질 수 있도록 처리해주는 효과
		
		// --- 페이징 처리 ---
		// 필요한 변수는 총 7개!!
		// > 기본적으로 구할 수 있는 4개의 변수 + 그 4개의 변수를 통해 계산해서 도출해야 하는 3개의 변수
		
		// 기본적으로 구할 수 있는 4개의 변수
		int listCount; // 현재 총 게시글의 갯수 (단, 삭제되지 않은 일반게시글의 갯수)
		// int currentPage; // 현재 사용자가 보고자 하는 페이지 (즉, 사용자가 요청한 페이지)
		// > 이미 매개변수로 요청 시 전달값으로 받아내고 있음!!
		int pageLimit; // 페이지 하단에 보여질 페이징바의 페이지 최대 갯수
		int boardLimit; // 한 페이지에 보여질 게시글의 최대 갯수 (즉, 한 페이지당 몇개씩 볼거냐)
		
		// 위의 4개의 변수들로 계산해서 구해야 하는 3개의 변수
		int maxPage; // 가장 마지막 페이지가 몇 번 페이지인지 (즉, 총 페이지 수)
		int startPage; // 페이지 하단에 보여질 페이징바의 시작수
		int endPage; // 페이지 하단에 보여질 페이징바의 끝수
		
		// * listCount : 총 게시글의 갯수
		// > BOARD 테이블의 유효한 데이터의 갯수를 COUNT 함수로 세오기!!
		listCount = boardService.selectListCount();
		
		// * currentPage : 현재 사용자가 요청한 페이지
		// > 이미 위에서 매개변수로 요청 시 전달값으로 cpage 라는 키값으로 넘겨받았음!!
		
		// * pageLimit : 페이지 하단에 보여질 페이징바의 페이지 최대 갯수
		// > 한 페이지 당 페이지 목록들을 몇 개 단위씩 보여질건지 임의의 값으로 지정하기
		//   (알고리즘 수식 계산의 편의를 위해 10으로 셋팅할 것)
		pageLimit = 10;
		
		// * boardLimit : 한 페이지에 보여질 게시글의 최대 갯수
		// > 한 페이지 당 게시글이 몇 개 씩 보여질건지 임의의 값으로 지정하기
		//   (알고리즘 수식 계산의 편의를 위해 10으로 셋팅할 것)
		boardLimit = 10;

		// 매번 listCount, currentPage, pageLimit, boardLimit 를 통해
		// maxPage, startPage, endPage 를 일일이 계산하는 코드를 작성하기 귀찮음!!
		// > 마찬가지로 공통 코드 작업을 해둘 것!!
		
		// Pagination 클래스를 생성하고 그 안에 getPageInfo 라는 메소드를 만들것!!
		PageInfo pi = Pagination.getPageInfo(listCount, currentPage, 
											 pageLimit, boardLimit);
		
		// System.out.println(listCount);
		// System.out.println(currentPage);
		// System.out.println(pageLimit);
		// System.out.println(boardLimit);
		// System.out.println(maxPage);
		// System.out.println(startPage);
		// System.out.println(endPage);
		
		// * 페이징 처리의 원리
		// > 게시글들을 최신순으로 정렬 후 페이지 구간별로 끊어서 조회해오는 것!!
		//   이때 위에서 구한 7 개의 변수들이 쿼리문에 영향을 미치기 때문에,
		//   7 개의 변수를 DAO 전달값으로 넘겨줄 것임!!
		
		// > 위의 7 개의 변수를 각 필드로 갖고있는 VO 클래스를 하나 만들 것!!
		// > PageInfo 라는 VO 클래스를 만들어서 각 변수를 필드로 가공해서 한번에 매개변수로 넘길 예정
		//   (한번 만들어 두면 공지사항, 일반게시판, 사진게시판 등 목록 조회 시 
		//    페이징 처리가 필요할 때 마다 계속 재활용해서 쓸 수 있게됨!!)
		
		/*
		PageInfo pi = new PageInfo(listCount, currentPage, pageLimit, boardLimit,
								   maxPage, startPage, endPage);
		*/
				
		// pi 를 전달하면서 Service 로 요청 후 결과 받기
		ArrayList<Board> list = boardService.selectBoardList(pi);
		
		/*
		for(Board b : list) {
			
			System.out.println(b);
		}
		*/
		// > 107 ~ 98 / 97 ~ 88 / 87 ~ 78 / ...
		//   (최신 게시글 기준으로 위에서부터 10개씩)
		
		// 현재 사용자가 요청한 페이지 (currentPage) 에 보여질 게시글 리스트를
		// 응답데이터로 넘기기
		mv.addObject("list", list);
		
		// 또한, 요청 페이지 하단에 보여질 페이징바를 만드려면 위의 7개의 변수를 담은
		// pi 도 응답데이터로 넘겨줘야함!!
		mv.addObject("pi", pi);
		
		// 우선 응답페이지를 만들어서 띄워보기
		mv.setViewName("board/boardListView");
		// > /WEB-INF/views/board/boardListView.jsp
		
		return mv;
	}
	
	@GetMapping("search")
	public ModelAndView searchBoardList(String condition, String keyword,
										@RequestParam(value="cpage", defaultValue="1") int currentPage,
										ModelAndView mv) {
		
		// System.out.println(condition);
		// > 검색 조건 : "writer" / "title" / "content"
		
		// System.out.println(keyword);
		// > 사용자가 입력한 검색어 : "ad" / "10" / "입니다"
		
		// 위의 두 값을 가지고 검색을 하되, 페이징 처리까지 해 줘야 한다!!
		// > 기본적으로 검색 시 검색 결과들 중 1번 페이지가 보여져야함!!
		//   또한 검색 결과들 중 n번 페이지를 사용자가 요청할 수도 있음!!
		//   (currentPage 값도 얻어내야함, 또한 7개의 변수도 마저 셋팅해야함)
		
		// 우선 총 검색된 게시글의 갯수를 먼저 구할 것!!
		// > condition, keyword 두개 다 넘기면서 쿼리문을 실행하고 와야 함!!
		// 1. condition, keyword 라는 두개의 필드를 가진 VO 를 만들고 객체로 만들어서 넘기기
		// 2. condition, keyword 라는 값을 HashMap 에 담아서 넘기기
		
		// > HashMap 이용해보기!!
		HashMap<String, String> map = new HashMap<>();
		map.put("condition", condition);
		map.put("keyword", keyword);
		
		int searchCount = boardService.selectSearchCount(map);
		
		// int currentPage;
		
		int pageLimit = 10;
		int boardLimit = 10;
		
		// System.out.println(searchCount);
		// System.out.println(currentPage);
		
		// 위의 searchCount, currentPage, pageLimit, boardLimit 를 가지고
		// maxPage, startPage, endPage 를 계산해서 구해야함!!
		// > 그리고 이걸 모두 PageInfo 로 한번에 가공해야함!!
		PageInfo pi = Pagination.getPageInfo(searchCount, currentPage, 
											 pageLimit, boardLimit);
		
		// 위의 HashMap 과 PageInfo 둘 다 넘기면서 검색용 쿼리문을 실행해서 결과를 받아야함!!
		ArrayList<Board> list = boardService.searchBoardList(map, pi);
		
		/*
		for(Board b : list) {
			
			System.out.println(b);
		}
		*/
		
		// 위에서 구해진 list 와 pi 를 응답데이터로 넘기면서 결과 화면 포워딩
		// > 기존의 게시글 목록 페이지 (boardListView.jsp) 를 재활용
		
		// 이 때, 검색 결과 창에 검색 조건, 검색어가 그대로 노출되었으면 좋겠음!!
		// 검색 결과 창에서 페이징바를 클릭하면 다음 페이지로 넘어가면 검색이 풀리는 이슈도 있음!!
		
		// 해결방법)
		// > 응답 데이터로 condition, keyword 를 다시 넘겨 주면 됨!!
		
		/*
		mv.addObject("list", list);
		mv.addObject("pi", pi);
		mv.addObject("condition", condition);
		mv.addObject("keyword", keyword);
		
		mv.setViewName("board/boardListView");
		*/
		
		// * ModelAndView 객체의 addObject 메소드는 return 타입이 ModelAndView 다!!
		// > 즉, mv 객체 자기자신을 리턴한다.
		mv.addObject("list", list)
		  .addObject("pi", pi)
		  .addObject("condition", condition)
		  .addObject("keyword", keyword)
		  .setViewName("board/boardListView");
		// > 그래서 위와 같이 메소드 체이닝이 가능하다!! (호출 순서 주의)
		
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
		// > 스프링에서 요청 시 전달값 중 첨부파일을 받고싶다면
		//   MultipartFile 타입의 매개변수 객체로 받아준다!!
		//   (단, 입력 form 에서 enctype 속성을 작성해야함)
		// > 이 때, 파일 업로드와 관련된 Spring 설정을 추가해야한다!! (application.properties 파일)
		
		// 요청 시 전달값들을 VO 로 받기 (커맨드 객체 방식)
		// System.out.println(b);
		// > category, boardTitle, boardContent, boardWriter
		
		// System.out.println(upfile);
		// > MultipartFile 객체의 getOriginalFilename() 이라는 메소드를 호출해보자!!
		//   넘어온 첨부파일의 원래 파일 명을 문자열로 리턴해주는 메소드임
		// System.out.println(upfile.getOriginalFilename());
		// > "bono.jpg" / "kuromi.png" / "" (빈 문자열)
		//   넘어온 첨부파일이 있다면 원본파일명, 넘어온 첨부파일이 없다면 빈 문자열이 리턴됨!!
		
		// upfile 로 넘겨받은 첨부파일의 정보를 Attachment VO 객체로 가공
		Attachment at = null;
		
		// 요청 시 넘어온 첨부파일이 있는지 부터 검사
		if(!upfile.getOriginalFilename().equals("")) {
			// > 넘어온 첨부파일이 있을 경우
			
			String changeName = FileRenamePolicy.saveFile(upfile, session, "/resources/board_upfiles/");
			
			// 8. 첨부파일에 대한 정보를 Attachment at 객체에 담기
			at = new Attachment();
			at.setOriginName(upfile.getOriginalFilename()); // 원본명 (사용자가 넘긴 이름)
			at.setChangeName(changeName); // 수정명 (실제 서버에 업로드 되어있는 이름)
			at.setFilePath("resources/board_upfiles/");
		}
		
		// System.out.println(b);
		// System.out.println(at);
		// > 넘어온 첨부파일이 없다면 at == null
		//   넘어온 첨부파일이 있다면 at != null (originName, changeName, filePath)
		
		// 단, XSS 공격 방지 처리 하고 넘어가기!!
		
		// Service 로 b, at 모두 다 넘기면서 요청 후 결과 받기 (하나의 트랜잭션으로 묶기 위함)
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
	public String selectBoard(@PathVariable int boardNo, Model model) {
		
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
			
			// 댓글 목록 조회 기능 구현 방법 1
			// > 게시글 상세조회 시 그 게시글에 딸린 댓글들도 조회해와서 응답데이터로 넘겨서 뿌려주기
			//   (동기식 방식으로 구현)
			
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

	@PostMapping("update")
	public String updateBoard(Board b, MultipartFile reUpfile,
							  @RequestParam(defaultValue="0") int originalFileNo,
							  String originalFileChangeName,
							  HttpSession session,
							  Model model) {
		
		// 요청 시 전달값들을 얻어내기 (커맨드 객체 방식)
		// System.out.println(b);
		// > boardNo, category, boardTitle, boardContent
		//   (글번호와 변경할 내용들이 함께 넘어옴)
		
		// System.out.println(reUpfile.getOriginalFilename());
		// > 변경할 첨부파일의 원본명
		//   변경할 첨부파일이 있다면 제대로된 이름, 변경할 첨부파일이 없다면 "" (빈문자열)
		
		// System.out.println(originalFileNo);
		// > 기존 첨부파일의 파일번호
		//   단, 기존 파일이 없을 경우는 int 형 변수에 null 값이 못들어가므로
		//   defaultValue 속성으로 기본값 0 을 지정했음!!
		
		// DAO 까지 전달할 값들을 담기 위해 우선 null 로 초기화
		Attachment at = null;
		
		// 새로 넘어온 첨부파일이 있을 경우
		// > ATTACHMENT 테이블에 INSERT 또는 UPDATE 를 해야함!!
		if(!reUpfile.getOriginalFilename().equals("")) {
			
			// 새로 넘어온 첨부파일의 이름을 수정 후 서버로 업로드
			// > 첨부파일에 대한 정보를 at 의 필드에 담기
			
			// 공통코드 FileRenamePolicy 클래스의 saveFile 메소드 호출
			String changeName = FileRenamePolicy.saveFile(reUpfile, session, 
														  "/resources/board_upfiles/");
			
			// insert 를 하든 update 를 하든 간에 originName, changeName 은 무조건 넘겨야함
			at = new Attachment();
			at.setOriginName(reUpfile.getOriginalFilename());
			at.setChangeName(changeName);
			
			// 기존 첨부파일이 있었을 경우
			// > 기존 첨부파일의 번호가 제대로 넘어온 경우 (originalFileNo 이 0 이 아닌 경우)
			if(originalFileNo != 0) {
				
				// attachment 테이블에 update 문을 실행해야 하는 경우
				// > fileNo 필드값을 추가적으로 셋팅해줘야함
				at.setFileNo(originalFileNo);
				
				// 서버에 저장되어있던 기존의 첨부파일을 삭제시키기!! (용량만 차지)
				// > 기존의 첨부파일의 수정파일명이 필요함!! (originalFileChangeName)
				String savePath = session.getServletContext()
										 .getRealPath("/resources/board_upfiles/");
				new File(savePath + originalFileChangeName).delete();
				
			} else {
				
				// attachment 테이블에 insert 문을 실행해야 하는 경우
				// > refNo, filePath 필드값을 추가적으로 셋팅해줘야함
				at.setRefNo(b.getBoardNo());
				at.setFilePath("resources/board_upfiles/");
				
			}
		}
		
		// 이 시점 기준으로
		// System.out.println(b);
		// System.out.println(at);
		// > 새로 넘어온 첨부파일이 없을 경우 : at == null
		// > 기존 첨부파일 X, 새로 넘어온 첨부파일 O : at != null 
		//   (refNo, originName, changeName, filePath)
		// > 기존 첨부파일 O, 새로 넘어온 첨부파일 O : at != null
		//   (originName, changeName, fileNo)
		
		// 단, 이 시점에서 마찬가지로 XSS 공격 방지 처리 하기!!
		
		// 이 경우를 모두 하나의 트랜잭션으로 묶어서 처리할 것!!
		int result = boardService.updateBoard(b, at);
		
		// 최종적으로 넘어온 result 에 따라 응답페이지를 지정
		if(result > 0) {
			// > 게시글 수정 성공
			
			// 일회성 알림 문구를 담아서 해당 게시글의 상세보기 페이지로 url 재요청
			session.setAttribute("alertMsg", "성공적으로 게시글이 수정되었습니다.");
			
			return "redirect:/board/detail/" + b.getBoardNo();
			
		} else {
			// > 게시글 수정 실패
		
			// 에러 문구를 담아서 에러페이지로 포워딩
			model.addAttribute("errorMsg", "게시글 수정에 실패했습니다.");
			
			return "common/errorPage";
		}
	}
	
	// -------------------------
	
	@ResponseBody
	@PostMapping("rinsert")
	public String ajaxInsertReply(Reply r, HttpSession session) {
		
		// System.out.println(r);
		// > Ajax 에서도 요청 시 전달값을 커맨드 객체 방식으로 받을 수 있음!!
		// > replyContent, refBoardNo 값이 넘어왔음
		
		// 추가적으로 댓글 작성자 (현재 로그인한 회원) 의 회원번호도 session 으로 부터 뽑아내기
		// > r 의 replyWriter 필드로 집어넣을것!!
		int replyWriter = ((Member)(session.getAttribute("loginUser"))).getUserNo();
		
		// r.setReplyWriter(String.valueOf(replyWriter));
		r.setReplyWriter(replyWriter + "");
		// > replyWriter 변수를 String 으로 변환해서 필드에 담아야한다!!
		// > replyContent, refBoardNo, replyWriter
		
		// XSS 공격 방지
		
		int result = boardService.insertReply(r);
		// > result > 0 이면 댓글 작성 성공!!
		
		return (result > 0) ? "success" : "fail";
	}
	
	@ResponseBody
	@GetMapping("rlist")
	public ArrayList<Reply> ajaxSelectReplyList(int boardNo) {
		
		// System.out.println(boardNo);
		
		ArrayList<Reply> list = boardService.selectReplyList(boardNo);
		
		return list;
	}
	
}








