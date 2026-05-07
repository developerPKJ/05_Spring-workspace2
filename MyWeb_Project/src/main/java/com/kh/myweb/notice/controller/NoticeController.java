package com.kh.myweb.notice.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kh.myweb.notice.model.service.NoticeService;
import com.kh.myweb.notice.model.vo.Notice;

@Controller
@RequestMapping("notice")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    @GetMapping("list")
    public String selectNoticeList(Model model) {
        // 공지사항 리스트 조회 페이지에서 필요로 하는 응답데이터를 구해야함
        // > NOTICE 테이블로부터 SELECT 문으로 전체 공지사항 데이터를 조회해와야함
        ArrayList<Notice> list = noticeService.selectNoticeList();
        // > 여러행 조회임

        /*
        for(Notice notice : list) {
            System.out.println(notice);
        }
        */

        model.addAttribute("list", list);

        // 응답페이지 만들어 포워딩
        return "notice/noticeListView";
    }
    
    @GetMapping("enrollForm")
    public String enrollForm() {
		// 공지사항 작성 페이지로 포워딩
        return "notice/noticeEnrollForm";
	}
}
