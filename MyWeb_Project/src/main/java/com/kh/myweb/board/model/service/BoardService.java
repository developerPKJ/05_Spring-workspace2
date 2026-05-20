package com.kh.myweb.board.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.myweb.board.model.dao.BoardDao;
import com.kh.myweb.board.model.vo.Attachment;
import com.kh.myweb.board.model.vo.Board;
import com.kh.myweb.board.model.vo.Category;
import com.kh.myweb.board.model.vo.Reply;
import com.kh.myweb.common.model.vo.PageInfo;

@Service
public class BoardService {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	@Autowired
	private BoardDao boardDao;
	
	public int selectListCount() {
		
		return boardDao.selectListCount(sqlSession);
	}
	
	public ArrayList<Board> selectBoardList(PageInfo pi) {
		
		return boardDao.selectBoardList(sqlSession, pi);
	}
	
	public int selectSearchCount(HashMap<String, String> map) {
		
		// map 에 담겨 넘어온 값 중 검색 조건을 나타내는 condition 값이
		// "writer" 또는 "title" 또는 "content" 와 일치하는지를 따져서
		// 각 경우의 수 마다 알맞은 조건에 따른 검색된 결과의 갯수를
		// 각각 조회해와야 한다!! (조건문을 통해)
		// > 원칙이긴 하나 길고 복잡해짐!!
		
		// 그래서 우리는 map 을 곧바로 dao 로 넘기면서 한번만 호출할 것!!
		// > 마이바티스의 동적 SQL 을 이용함
		return boardDao.selectSearchCount(sqlSession, map);
	}
	
	public ArrayList<Board> searchBoardList(HashMap<String, String> map,
											PageInfo pi) {
		
		return boardDao.searchBoardList(sqlSession, map, pi);
	}
	
	@Transactional
	public int insertBoard(Board b, Attachment at) {
		
		// 넘어온 첨부파일이 있든 없든 간에 무조건 BOARD 테이블에 먼저 INSERT 는 해야함!!
		// > BOARD 테이블에 INSERT 구문을 실행시켜줄 DAO 만 먼저 다녀오기!!
		int result1 = boardDao.insertBoard(sqlSession, b);
		// > BOARD 에 INSERT 가 잘 되었다면 1, 아니라면 0
		
		int result2 = 1;
		// > 단, 첨부파일이 없는 경우 위의 BOARD INSERT 가 성공했을때도
		//   result2 를 0으로 초기화 하면 최종적으로 곱셈에 의해 실패로 간주됨!!
		// > 그래서 우리는 애초에 1로 셋팅하는것임!!
		
		// 넘어온 첨부파일이 있을 경우에는 ATTACHMENT 테이블에 INSERT 를 또 해야함!!
		// > 넘어온 첨부파일이 없으면 이 과정은 패스!!
		if(at != null) {
			// > 넘어온 첨부파일이 있을 경우
			
			result2 = boardDao.insertAttachment(sqlSession, at);
			// > ATTACHMENT 에 INSERT 가 잘 되었다면 1, 아니라면 0
		}
		
		// result1 과 result2 가 모두 1이라면 최종 성공
		// 하나라도 0이라면 실패
		return result1 * result2;
	}
	
	@Transactional
	public int increaseCount(int boardNo) {
		
		return boardDao.increaseCount(sqlSession, boardNo);
	}
	
	public Board selectBoard(int boardNo) {
		
		return boardDao.selectBoard(sqlSession, boardNo);
	}
	
	public Attachment selectAttachment(int boardNo) {
		
		return boardDao.selectAttachment(sqlSession, boardNo);
	}
	
	@Transactional
	public int deleteBoard(int boardNo) {
		
		return boardDao.deleteBoard(sqlSession, boardNo);
	}
	
	public ArrayList<Category> selectCategoryList() {
		
		return boardDao.selectCategoryList(sqlSession);
	}
	
	@Transactional
	public int updateBoard(Board b, Attachment at) {
		
		// 뭐가 되었든 간에 일단 BOARD 테이블에 UPDATE 는 무조건 먼저 해야함!!
		int result1 = boardDao.updateBoard(sqlSession, b);
		
		int result2 = 1;
		
		// 새로 넘어온 첨부파일이 있을 경우 ATTACHMENT 테이블에 DML 문을 실행함!!
		if(at != null) {
			
			// UPDATE 할건지 INSERT 할건지를 가를 예정!!
			if(at.getFileNo() != 0) {
				// > fileNo 값이 제대로 있을 경우 (UPDATE 해야 할 경우)
				
				result2 = boardDao.updateAttachment(sqlSession, at);
				
			} else {
				// > fileNo 값이 0 일 경우 (INSERT 해야 할 경우)
				
				result2 = boardDao.insertNewAttachment(sqlSession, at);
			}
		}
		
		// 이 시점 기준으로 쿼리문들이 모두 실행 됨!!
		return result1 * result2;
	}

	@Transactional
	public int insertThumbnailBoard(Board b, ArrayList<Attachment> list) {
		
		// 우선 b 를 가지고 먼저 BOARD 에 INSERT 하고 돌아오기
		int result1 = boardDao.insertThumbnailBoard(sqlSession, b);
		
		// list 를 가지고 ATTACHMENT 에 INSERT 하고 돌아오기
		// > 그냥 무조건 다녀올것!! 
		//   (대표이미지는 필수입력사항이였기 때문에 첨부파일이 항상 넘어오는 구조라서)
		int result2 = boardDao.insertAttachmentList(sqlSession, list);
		
		return result1 * result2;
	}

	public ArrayList<Board> selectThumbnailList() {
		
		return boardDao.selectThumbnailList(sqlSession);
	}

	public ArrayList<Attachment> selectAttachmentList(int boardNo) {
		
		// DAO 단도 마찬가지로 리턴타입이 다르기 때문에 DAO 메소드 재활용은 불가!!
		return boardDao.selectAttachmentList(sqlSession, boardNo);
	}

	@Transactional
	public int insertReply(Reply r) {
		
		return boardDao.insertReply(sqlSession, r);
	}

	public ArrayList<Reply> selectReplyList(int boardNo) {
		
		return boardDao.selectReplyList(sqlSession, boardNo);
	}
	
}





