<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- taglib 지시어는 매 페이지마다 일일이 필요하면 적어줘야함 --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
    .list-area {
        width : 90% !important;
        text-align: center;
        margin: auto;
    }

    .list-area>tbody>tr:hover {
        cursor: pointer;
    }
</style>
</head>
<body>
    <jsp:include page="../common/menubar.jsp" />

    <div class="outer">
        <br>
        <h2 align="center">공지사항</h2>
        <br>

        <br>

        <!-- 관리자만 보이는 글작성 버튼 배치 -->
        <c:if test="${ not empty sessionScope.loginUser && sessionScope.loginUser.userId eq 'admin' }">
	        <div align="right" style="margin: 0 5% 0 0;">
	            <a href="/myweb/notice/enrollForm" class="btn btn-secondary btn-sm">글작성</a>
	            <br><br>
	        </div>
        </c:if>

        <table class="list-area table table-hover">
            <thead>
                <tr>
                    <th>번호</th>
                    <th>제목</th>
                    <th>작성자</th>
                    <th>조회수</th>
                    <th>작성일</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${ empty list }">
                    	<!-- case1 : 조회된 공지사항이 없다면 -->
                    	<tr>
                        	<td colspan="5">공지사항이 없습니다.</td>	                    	
                    	</tr>
                    </c:when>
                    <c:otherwise>
                    	<!-- case2 : 공지사항이 있는 경우 -->
                    	<c:forEach var="n" items="${ requestScope.list }">
                        	<tr>
                            	<td>${ n.noticeNo }</td>
                            	<td>${ n.noticeTitle }</td>
                            	<td>${ n.noticeWriter }</td>
                            	<td>${ n.count }</td>
                            	<td>${ n.createDate }</td>
                        	</tr>
                    	</c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>

        <br><br>
        
    </div>

    <br><br>

</body>
</html>