package com.kh.myweb.board.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.kh.myweb.board.model.service.BoardService;
import com.kh.myweb.board.model.vo.Board;
import com.kh.myweb.common.model.vo.PageInfo;
import com.kh.myweb.common.template.Pagination;

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
		
		int maxPage; 		// 가장 마지막 페이지 번호(총 페이지 수)
		int startPage; 		// 하단 페이지 버튼의 시작 번호
		int endPage; 		// 하단 페이지 버튼의 끝 번호
		
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
}
