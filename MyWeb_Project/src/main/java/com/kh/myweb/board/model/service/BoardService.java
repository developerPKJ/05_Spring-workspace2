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
//		마이바티스의 동적 sql을 이용해서 한번만 호출해서 해결
		return boardDao.selectSearchCount(sqlSession, map);
	}
	
	public ArrayList<Board> searchBoardList(HashMap<String, String> map, PageInfo pi) {
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
}
