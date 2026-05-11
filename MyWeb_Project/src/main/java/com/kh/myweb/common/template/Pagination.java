package com.kh.myweb.common.template;

import com.kh.myweb.common.model.vo.PageInfo;

public class Pagination {
	
	public static PageInfo getPageInfo(int listCount, int currentPage,
									   int pageLimit, int boardLimit) {
		int maxPage = (int)Math.ceil((double)listCount / boardLimit);
//		System.out.println("maxPage : " + maxPage);
		int startPage = ((currentPage - 1) / pageLimit) * pageLimit + 1;
		int endPage = startPage + pageLimit - 1;
		if (endPage > maxPage) {
			endPage = maxPage;
		}
		
		return new PageInfo(listCount, currentPage, pageLimit, boardLimit, 
							maxPage, startPage, endPage);
	}
	
}
