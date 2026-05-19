package com.kh.myweb.board.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.kh.myweb.board.model.service.BoardService;
import com.kh.myweb.board.model.vo.Attachment;
import com.kh.myweb.board.model.vo.Board;
import com.kh.myweb.common.template.FileRenamePolicy;

import jakarta.servlet.http.HttpSession;

// 사진게시판에 대한 요청을 받을 Controller 만 쪼갬!!
// Service, Dao, mapper 는 그대로 Board 껄로 쓸거임!!

@Controller
@RequestMapping("thumbnail")
public class ThumbnailController {
	
	@Autowired
	private BoardService boardService;

	@GetMapping("list")
	public String selectThumbnailList(Model model) {
		
		// 사실 페이징 처리도 해야함!!
		
		// 사진게시글 목록 조회용 서비스 요청 후 결과 받기
		ArrayList<Board> list = boardService.selectThumbnailList();
		
		// 응답데이터로 조회된 목록을 담고
		model.addAttribute("list", list);
		
		// 사진게시글 목록조회 화면 포워딩
		return "board/thumbnailListView";
		// > /WEB-INF/views/board/thumbnailListView.jsp
	}
	
	@GetMapping("enrollForm")
	public String enrollForm() {
		
		// 사진게시글 작성 페이지 포워딩
		return "board/thumbnailEnrollForm";
		// > /WEB-INF/views/board/thumbnailEnrollForm.jsp
	}
	
	@PostMapping("insert")
	public String insertThumbnailBoard(Board b,
									   MultipartFile[] files,
									   HttpSession session,
									   Model model) {
		
		// 요청 시 전달값들을 받아내기 (커맨드 객체 방식)
		// System.out.println(b);
		// > boardTitle, boardContent, boardWriter 가 잘 넘어옴!!
		// > 이걸 가지고 BOARD 테이블에 INSERT 할 예정
		
		// 첨부파일의 경우도 checkbox 와 마찬가지로
		// files 라는 동일한 키값으로 동시에 여러개의 첨부파일을 보내면
		// Controller 에서 MultipartFile[] 로 받아줄 수 있다!!
		// > ATTACHMENT 테이블에 INSERT 할 예정
		
		// MultipartFile 객체 1개 <--> Attachment VO 객체 1개
		// > 이번에는 여러개의 첨부파일이 넘어오고,
		//   상황에 따라 1개 ~ 4개 사이로 몇개인지는 모르지만 여러개가 넘어올 것!!
		//   (대표이미지는 필수입력사항이므로 최소 1개 이상은 넘어옴)
		// > ArrayList<Attachment> 에 담아두기!!
		ArrayList<Attachment> list = new ArrayList<>();
		
		// MultipartFile[] files 크기만큼 반복 돌리기!!
		for(int i = 0; i < files.length; i++) {
			
			// files[i].getOriginalFilename() : 원본파일명 리턴
			// > 넘어온 첨부파일이 있다면 제대로된 파일명 문자열,
			//   넘어온 첨부파일이 없다면 "" (빈문자열) 이 리턴됨!!
			
			if(!files[i].getOriginalFilename().equals("")) {
				// > i 번째로 넘어온 첨부파일이 있을 경우
				
				// 그 i 번째 첨부파일을 파일명 수정 후 서버로 업로드 작업 (공통코드)
				String changeName = FileRenamePolicy.saveFile(files[i], session, 
													"/resources/thumbnail_upfiles/");
				// > 사진게시글에 딸린 첨부파일들은 webapp 의 resources 의 thumbnail_upfiles 폴더에 보관할 것!!
				
				// Attachment 객체 생성 후 원본명, 수정명, 파일경로, 파일레벨을 담아서 list 에 add
				Attachment at = new Attachment();
				at.setOriginName(files[i].getOriginalFilename());
				at.setChangeName(changeName);
				at.setFilePath("resources/thumbnail_upfiles/");
				
				if(i == 0) {
					// > 대표이미지일 경우 (fileLevel 을 1 로)
					
					at.setFileLevel(1);
					
				} else {
					// > 상세이미지일 경우 (fileLevel 을 2 로)
					
					at.setFileLevel(2);
				}
				
				// 이 시점 기준으로 i 번째 첨부파일이 있다면
				// 그 첨부파일의 정보가 at 객체에 잘 담겨있을 것!!
				list.add(at);
			}
		}
		
		// 이 시점 기준으로 list 에는 넘어온 첨부파일들의 정보가 잘 담겨있을 것!!
		// > 원본명, 수정명, 파일경로, 파일레벨
		/*
		for(Attachment at : list) {
			
			System.out.println(at);
		}
		*/
		
		// 셋팅된 b, list 를 넘기면서 Service -> DAO 를 다녀올 것!! (하나의 트랜잭션)
		// > 마찬가지로 XSS 공격 방지도 하고 갔다와야함!!
		
		int result = boardService.insertThumbnailBoard(b, list);
		
		if(result > 0) { 
			// > 사진게시글 등록 성공
			
			// 1회성 알림 문구를 담아서 사진게시글 목록조회 쪽으로 url 재요청
			session.setAttribute("alertMsg", "성공적으로 사진게시글이 업로드 되었습니다.");
			
			return "redirect:/thumbnail/list";
			
		} else {
			// > 사진게시글 등록 실패
			
			// 에러 문구를 담아서 에러페이지로 포워딩
			model.addAttribute("errorMsg", "사진게시글 업로드에 실패했습니다.");
			
			return "common/errorPage";
		}
		
	}
	
	@GetMapping("detail/{boardNo}")
	public ModelAndView selectThumbnail(@PathVariable int boardNo, ModelAndView mv) {
		
		// 글번호 먼저 확인
		// System.out.println(boardNo);
		
		// 일반게시글이든 사진게시글이든 똑같이 BOARD 테이블에 담겨있는 데이터임!!
		// > 우선 뭐가 되었든 간에 상세조회를 한다면 "조회수 증가" 부터 하고 돌아와야함
		
		int result = boardService.increaseCount(boardNo);
		// > 기존 조회수 증가용 서비스 재활용
		
		// 조회수 증가에 성공했다면 단일행 조회 해오기
		if(result > 0) {
			// > 조회수 증가 성공
			
			// 게시글 정보 조회해오기
			Board b = boardService.selectBoard(boardNo);
			// > 기존의 일반게시글 상세조회용 서비스 재활용 
			//   (단 쿼리문을 CATEGORY 테이블 JOIN 시 INNER 에서 LEFT 로 변경한다)
			//   category 정보가 null 인 사진게시글도 누락 없이 잘 조회됨!!
			
			// 그 게시글에 딸린 첨부파일들의 정보 조회해오기
			ArrayList<Attachment> list 
						= boardService.selectAttachmentList(boardNo);
			// > 기존의 첨부파일 상세조회용 쿼리문 재활용 
			//   (서비스는 재활용 불가 - 리턴 타입이 달라서)
			
			// 게시글, 첨부파일들의 정보를 응답데이터로 담고
			// 화면 포워딩 (사진게시글 상세조회 페이지)
			mv.addObject("b", b)
			  .addObject("list", list)
			  .setViewName("board/thumbnailDetailView");
			// > /WEB-INF/views/board/thumbnailDetailView.jsp
			
		} else {
			// > 조회수 증가 실패
			
			// 에러문구를 담아서 에러페이지로 포워딩
			mv.addObject("errorMsg", "사진게시글 상세조회에 실패했습니다.")
			  .setViewName("common/errorPage");
		}
		
		return mv;
	}
	
}







