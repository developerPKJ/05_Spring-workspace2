package com.kh.myweb.common.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor // 기본생성자를 자동완성 해주는 어노테이션
@AllArgsConstructor // 모든 필드에 대한 매개변수 생성자를 자동완성 해주는 어노테이션
@Setter // setter 메소드들을 자동완성 해주는 어노테이션
@Getter // getter 메소드들을 자동완성 해주는 어노테이션
@ToString // toString 메소드를 오버라이딩 해주는 어노테이션
public class PageInfo {
	// field
	private int listCount; 		// 총 게시글 수
	private int currentPage;	// 현재 페이지 번호
	private int pageLimit; 		// 한 페이지 당 보여질 최대 페이지 버튼 수
	private int boardLimit; 	// 한 페이지 당 보여질 최대 게시글 수
	
	private int maxPage; 		// 가장 마지막 페이지 번호(총 페이지 수)
	private int startPage; 		// 하단 페이지 버튼의 시작 번호
	private int endPage; 		// 하단 페이지 버튼의 끝 번호
}
