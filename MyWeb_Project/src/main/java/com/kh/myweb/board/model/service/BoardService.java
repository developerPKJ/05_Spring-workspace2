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
		int result1 = boardDao.insertBoard(sqlSession, b);
//		> BOARD INSERT 성공 시 1, 실패 시 0 리턴
	
//		넘어온 첨부파일이 있을 경우 ATTACHMENT 테이블에 INSERT 작업 추가 진행
//		> 첨부파일이 없으면 pass
		if(at != null) {
			int result2 = boardDao.insertAttachment(sqlSession, at);
		}
		
//		result1, 2 모두 1이면 성공 아니면 실패
	}
}
