package com.kh.myweb.member.model.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.myweb.member.model.vo.Member;

// @Component
@Repository // 저장소라는 뜻, 외부 매체인 DB 와 입출력이 일어나는 DAO 역할을 하겠다.
public class MemberDao {

	public Member loginMember(SqlSessionTemplate sqlSession, Member m) {
		
		return sqlSession.selectOne("memberMapper.loginMember", m);
	}
	
	public int insertMember(SqlSessionTemplate sqlSession, Member m) {
		
		return sqlSession.insert("memberMapper.insertMember", m);
	}
	
	public int updateMember(SqlSessionTemplate sqlSession, Member m) {
		
		return sqlSession.update("memberMapper.updateMember", m);
	}
	
	public int updatePwd(SqlSessionTemplate sqlSession, Member m) {

		return sqlSession.update("memberMapper.updatePwd", m);
	}

	public int deleteMember(SqlSessionTemplate sqlSession, String userId) {

		return sqlSession.update("memberMapper.deleteMember", userId);
	}
}









