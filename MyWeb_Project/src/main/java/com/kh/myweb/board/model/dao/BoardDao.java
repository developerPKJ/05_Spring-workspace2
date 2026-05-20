package com.kh.myweb.board.model.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.myweb.board.model.vo.Attachment;
import com.kh.myweb.board.model.vo.Board;
import com.kh.myweb.board.model.vo.Category;
import com.kh.myweb.board.model.vo.Reply;
import com.kh.myweb.common.model.vo.PageInfo;

@Repository
public class BoardDao {

	public int selectListCount(SqlSessionTemplate sqlSession) {
		
		return sqlSession.selectOne("boardMapper.selectListCount");
	}
	
	public ArrayList<Board> selectBoardList(SqlSessionTemplate sqlSession, PageInfo pi) {
		
		/*
		 * boardLimit 가 10 이라는 가정 하에
		 * 
		 * currentPage = 1 : 시작수 = 1, 끝수 = 10
		 * currentPage = 2 : 시작수 = 11, 끝수 = 20
		 * currentPage = 3 : 시작수 = 21, 끝수 = 30
		 * currentPage = 4 : 시작수 = 31, 끝수 = 40
		 * ...
		 * 
		 * => 즉, 시작수 = (currentPage - 1) * boardLimit + 1;
		 * 
		 * 1 = (1 - 1) * 10 + 1;
		 * 11 = (2 - 1) * 10 + 1;
		 * ...
		 * 
		 * 끝수 = 시작수 + boardLimit - 1;
		 */
		
		/*
		int startRow = (pi.getCurrentPage() - 1) * pi.getBoardLimit() + 1;
		int endRow = startRow + pi.getBoardLimit() - 1;
		
		
		HashMap<String, Integer> map = new HashMap<>();
		
		map.put("startRow", startRow);
		map.put("endRow", endRow);
		
		return (ArrayList)sqlSession.selectList("boardMapper.selectBoardList", map);
		*/
		// > 여태 구현했던 방법이 페이징 처리의 가장 정석적인 방법임!!
		//   단, 쿼리문이 여러 겹의 인라인뷰로 인해 복잡해짐!!
		
		/*
		 * - 마이바티스에서는 아무리 메꿀 데이터가 여러개더라도
		 *   sqlSession 에서 제공하는 메소드를 호출할 때 전달값을 무조건 하나의 변수로 묶어 보내야한다.
		 * 
		 * 1. VO 클래스를 만들어서 객체로 두 값을 필드로 넣어서 가공한다.
		 * 2. HashMap 으로 여러 개의 값들을 묶어서 넘겨준다. (키 + 밸류 세트로)
		 */
		
		/*
		 * * 마이바티스를 이용해서 페이징 처리를 구현하는 방법
		 * 1. 기존의 방법 (인라인뷰 여러겹 쓰기)
		 * 2. 마이바티스에서 제공하는 페이징처리용 객체 (RowBounds) 를 이용하는 방법
		 * 
		 * * RowBounds 객체의 작동 원리
		 * > 우선 전체 테이블의 조회 결과 (ResultSet) 를 다 불러온 뒤,
		 *   "앞에서부터 n 개를 건너 뛰고 m 개만 조회" 하겠다.
		 * 
		 * 예) 게시글을 최신순으로 107 개를 모두 다 조회해온 뒤
		 * 앞에서부터 0 개를 건너 뛰고 10 개만 조회 => 1번 페이지
		 * 앞에서부터 10 개를 건너 뛰고 10 개만 조회 => 2번 페이지
		 * ...
		 * > RowBounds 객체에 의해 구간별로 리스트 조회가 알아서 일어나기 때문에
		 *   쿼리문에 내가 직접 굳이 인라인뷰를 적용할 필요가 없음!!
		 *   
		 * * RowBounds 객체 생성 방법
		 * [ 표현법 ]
		 * RowBounds rowBounds = new RowBounds(offset, limit);
		 * 
		 * - offset : 앞에서부터 몇개를 건너뛸건지에 대한 갯수 
		 * - limit : 몇개를 조회해올건지에 대한 갯수
		 * 
		 * 예) boardLimit 가 10 일 경우
		 * 								offset (건너 뛸 갯수)		limit (조회할 갯수)
		 * currentPage = 1 => 1 ~ 10 			0						10
		 * currentPage = 2 => 11 ~ 20 			10						10
		 * currentPage = 3 => 21 ~ 30 			20						10
		 * ...
		 */
		
		int limit = pi.getBoardLimit();
		int offset = (pi.getCurrentPage() - 1) * limit;
		
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		// 이 RowBounds 객체를 같이 넘기면서 selectList 메소드를 호출 (3번째 매개변수)
		return (ArrayList)sqlSession.selectList("boardMapper.selectBoardList", null, rowBounds);
		// > 이 때 쿼리문은 전체 조회용 쿼리문으로 수정하면 됨!! (단, 최신순으로 정렬은 하고)
		// > RowBounds 객체를 넘겨야 할 경우
		//   만약 해당 SQL 문이 완성된 형태라 두번째 매개변수 (완성시킬 데이터) 가
		//   필요 없는 경우에는 null 을 넘기면 된다!! 
		
		/*
		 * * RowBounds 객체의 단점
		 * - 기존의 인라인뷰를 쓰는 방식
		 * : 처음부터 쿼리문을 이용해서 boardLimit 갯수만큼만 조회해오는 원리
		 * - RowBounds 를 쓰는 방식
		 * : 해당 쿼리문에 의해서 전체 행을 모두 조회하고,
		 *   selectList 메소드와 RowBounds 객체에 의해
		 *   구간별로 boardLimit 갯수만큼 자바 코드로 끊어서 보여주는 원리
		 * 
		 * => 사실 RowBounds 객체를 쓴느 방법은 권장사항이 아님!! (성능 이슈 때문)
		 */
	}
	
	public int selectSearchCount(SqlSessionTemplate sqlSession, 
								 HashMap<String, String> map) {
		
		return sqlSession.selectOne("boardMapper.selectSearchCount", map);
	}
	
	public ArrayList<Board> searchBoardList(SqlSessionTemplate sqlSession,
											HashMap<String, String> map,
											PageInfo pi) {
		
		int limit = pi.getBoardLimit();
		int offset = (pi.getCurrentPage() - 1) * limit;
		
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		return (ArrayList)sqlSession.selectList("boardMapper.searchBoardList", map, rowBounds);
	}
	
	public int insertBoard(SqlSessionTemplate sqlSession, Board b) {
		
		return sqlSession.insert("boardMapper.insertBoard", b);
	}
	
	public int insertAttachment(SqlSessionTemplate sqlSession, Attachment at) {
		
		return sqlSession.insert("boardMapper.insertAttachment", at);
	}
	
	public int increaseCount(SqlSessionTemplate sqlSession, int boardNo) {
		
		return sqlSession.update("boardMapper.increaseCount", boardNo);
	}
	
	public Board selectBoard(SqlSessionTemplate sqlSession, int boardNo) {
		
		return sqlSession.selectOne("boardMapper.selectBoard", boardNo);
	}
	
	public Attachment selectAttachment(SqlSessionTemplate sqlSession, int boardNo) {
		
		return sqlSession.selectOne("boardMapper.selectAttachment", boardNo);
	}
	
	public int deleteBoard(SqlSessionTemplate sqlSession, int boardNo) {
		
		return sqlSession.update("boardMapper.deleteBoard", boardNo);
	}
	
	public ArrayList<Category> selectCategoryList(SqlSessionTemplate sqlSession) {
		
		return (ArrayList)sqlSession.selectList("boardMapper.selectCategoryList");
	}
	
	public int updateBoard(SqlSessionTemplate sqlSession, Board b) {
		
		return sqlSession.update("boardMapper.updateBoard", b);
	}
	
	public int updateAttachment(SqlSessionTemplate sqlSession, Attachment at) {
		
		return sqlSession.update("boardMapper.updateAttachment", at);
	}
	
	public int insertNewAttachment(SqlSessionTemplate sqlSession, Attachment at) {
		
		return sqlSession.insert("boardMapper.insertNewAttachment", at);
	}

	public int insertThumbnailBoard(SqlSessionTemplate sqlSession, Board b) {
		
		return sqlSession.insert("boardMapper.insertThumbnailBoard", b);
	}

	public int insertAttachmentList(SqlSessionTemplate sqlSession, ArrayList<Attachment> list) {
		
		// 넘어온 첨부파일 갯수가 몇개인지 정확히 모름!!
		// > 우리의 insert into 구문은 한번에 한개의 정보만 insert 가능!!
		// > 반복문을 이용해서 insert into 구문을 첨부파일의 갯수만큼 실행시키기!!
		
		int result = 1;
		// > 초기값을 1로 셋팅해두고 누적 곱을 구할 예정!!
		//   하나라도 실패하면 최종 결과가 0, 모두 성공해야지만 1
		
		for(Attachment at : list) {
			
			result *= sqlSession.insert("boardMapper.insertAttachmentList", at);
		}
		
		return result;
	}

	public ArrayList<Board> selectThumbnailList(SqlSessionTemplate sqlSession) {
		
		return (ArrayList)sqlSession.selectList("boardMapper.selectThumbnailList");
	}

	public ArrayList<Attachment> selectAttachmentList(SqlSessionTemplate sqlSession, int boardNo) {
		
		// 드디어 쿼리문을 찾아서 실행할 경우에는 재활용이 됨!!
		return (ArrayList)sqlSession.selectList("boardMapper.selectAttachment", boardNo);
	}

	public int insertReply(SqlSessionTemplate sqlSession, Reply r) {
		
		return sqlSession.insert("boardMapper.insertReply", r);
	}

	public ArrayList<Reply> selectReplyList(SqlSessionTemplate sqlSession, int boardNo) {
		
		return (ArrayList)sqlSession.selectList("boardMapper.selectReplyList", boardNo);
		// > 사실 댓글 목록 조회도 페이징처리 해야함!!
	}
}














