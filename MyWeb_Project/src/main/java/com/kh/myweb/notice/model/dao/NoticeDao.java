package com.kh.myweb.notice.model.dao;

import java.util.ArrayList;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.kh.myweb.notice.model.vo.Notice;

@Repository
public class NoticeDao {
    
	public ArrayList<Notice> selectNoticeList(SqlSessionTemplate sqlSession) {
		return (ArrayList)sqlSession.selectList("noticeMapper.selectNoticeList");
	}
	
}
