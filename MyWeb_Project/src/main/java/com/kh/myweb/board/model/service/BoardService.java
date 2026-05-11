package com.kh.myweb.board.model.service;

import java.util.ArrayList;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.myweb.board.model.dao.BoardDao;
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
}
