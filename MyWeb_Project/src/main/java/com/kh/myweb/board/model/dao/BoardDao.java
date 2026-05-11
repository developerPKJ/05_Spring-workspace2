package com.kh.myweb.board.model.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.myweb.board.model.vo.Board;
import com.kh.myweb.common.model.vo.PageInfo;

@Repository
public class BoardDao {
	public int selectListCount(SqlSessionTemplate sqlSession) {
		return sqlSession.selectOne("boardMapper.selectListCount");
	}
	
	public ArrayList<Board> selectBoardList(SqlSessionTemplate sqlSession, PageInfo pi) {
		/*
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit() + 1;
		int limit = offset + pi.getBoardLimit() - 1;
		
		HashMap<String, Integer> map = new HashMap<>();
		map.put("offset", offset);
		map.put("limit", limit);
		
		return (ArrayList)sqlSession.selectList("boardMapper.selectBoardList", map);
		*/
//		위의 방식은 페이징 처리의 가장 정석적인 방법
//		> 쿼리문이 여러 겹의 인라인 뷰로 복잡해짐
		
		/*
		 * 페이징 처리 구현 방법
		 * 1. 기존의 방법 ( 인라인 뷰 여러 겹 )
		 * 2. 마이바티스에서 제공하는 페이징 처리용 객체 이용 방법 ( RowBounds )
		 * - 게시글을 전체를 조회한 후, 앞에서부터 n개를 건너뛰고, 그 다음 m개를 조회하는 방식
		 * */
		RowBounds rowBounds = new RowBounds((pi.getCurrentPage() - 1) * pi.getBoardLimit(), pi.getBoardLimit());
		
		return (ArrayList)sqlSession.selectList("boardMapper.selectBoardList2", null, rowBounds);
//		해당 SQL문이 완성된 형태라 두번째 매개변수가 필요 없는 경우 null로 전달
		
		/*
		 * RowBounds 객체의 단점
		 * - 기존의 인라인뷰를 쓰는 방식
		 * > 처음부터 쿼리문을 이용해서 boardLimit 갯수만큼 조회함
		 * - RowBounds 방식
		 * > 게시글 전체를 조회한 후, RowBounds 객체에 담긴 offset과 limit 값을 이용해서 원하는 부분만 자르는 방식
		 * 
		 * >> RowBounds의 경우 전체를 조회하기 때문에, 데이터가 많으면 성능 저하가 발생할 수 있음
		 * >> 실무에서는 사용하지 않음
		 * */
	}
}
