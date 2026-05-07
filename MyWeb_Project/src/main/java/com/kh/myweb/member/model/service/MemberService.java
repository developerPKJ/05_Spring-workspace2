package com.kh.myweb.member.model.service;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.myweb.member.model.dao.MemberDao;
import com.kh.myweb.member.model.vo.Member;

/*
 * * Service 단에서 해야할 일 (Before)
 * 1. SqlSession 객체 생성
 * 2. SqlSession 객체와 전달값을 DAO 로 넘기면서 요청 후 결과 받기
 * 3. DML 문의 경우 트랜잭션 처리
 * 4. SqlSession 객체 반납
 * 5. Controller 로 결과 리턴
 * 
 * - Spring 의 특징 중 IoC, DI 에 의해 지금부터는 SqlSession 객체를 내가 직접 생성하거나
 *   반납하지 않을 것임!! (즉, 스프링이 알아서 SqlSession 객체를 생성하고, 반납까지 시켜주겠다)
 * - Spring 의 특징 중 IoC 에 의해 지금부터는 내가 직접 트랜잭션 처리 또한 안할 것!!
 *   (즉, 스프링이 알아서 트랜잭션 처리까지 시켜주겠다)  
 * 
 * * 이제부터 Service 단에서 해야할 일 (Spring)
 * 1. SqlSessionTemplate 객체와 전달값을 DAO 로 넘기면서 요청 후 결과 받기
 * 2. Controller 로 결과 리턴
 * 
 * - 단, 순수 마이바티스에서는 SqlSession 객체를 이용했다면,
 *   스프링 + 마이바티스 조합에서는 SqlSessionTemplate 객체를 이용한다!!
 *   (SqlSessionTemplate : 스프링 전용 마이바티스 객체)
 */

// @Component // Bean 등록용 어노테이션 : 내가 필요할 때 스프링이 객체를 생성 할 수 있도록 유도
@Service // 마찬가지로 Bean 등록용 어노테이션, Service 단 역할을 하겠다라는 뜻도 겸함!! (더 명시적)
public class MemberService {

	@Autowired
	private SqlSessionTemplate sqlSession;
	// > 내가 필요할 때 마다 SqlSessionTemplate 객체가 주입되는것!! (DI)
	//   스프링 부트에서는 설정의 간소화로 인해 SqlSessionTemplate 객체가 자동으로 Bean 으로 등록됨!!
	//   (@Autowired 어노테이션만 붙여서 객체를 얻어내면 됨)
	
	@Autowired
	private MemberDao memberDao;
	
	public Member loginMember(Member m) {

		return memberDao.loginMember(sqlSession, m);
	}
	
	@Transactional // 이 메소드를 하나의 트랜잭션으로 관리하겠다.
	// > 이 메소드 실행 중 dml 문 처리 시 하나라도 오류가 발생하면 자동으로 롤백해주는 효과를 줌
	public int insertMember(Member m) {
		
		return memberDao.insertMember(sqlSession, m);
	}
	
	@Transactional
	public int updateMember(Member m) {
		
		return memberDao.updateMember(sqlSession, m);
	}

	@Transactional
	public int updatePwd(Member m) {
		
		return memberDao.updatePwd(sqlSession, m);
	}

	@Transactional
	public int deleteMember(String userId) {
		
		return memberDao.deleteMember(sqlSession, userId);
	}
	
}







