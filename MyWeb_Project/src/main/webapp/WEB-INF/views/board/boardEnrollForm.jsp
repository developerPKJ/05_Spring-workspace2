<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
    #enroll-form table {
        margin : auto;
        width : 90% !important;
    }
    
    #enroll-form input, #enroll-form textarea, #enroll-form select {
        padding: 5px;
        margin: 5px;
    }

    #enroll-form input, #enroll-form textarea {
        width: 100%;
    }

    #enroll-form textarea {
        height: 300px;
        resize: none;
    }
</style>
</head>
<body>
    <jsp:include page="../common/menubar.jsp" />

    <div class="outer">
        <br>
        <h2 align="center">일반게시글 작성</h2>
        <br>

        <!-- 
            일반 게시글 작성 기능 구현
            - 로그인된 상태에서 사용자가 요구사항 입력하고 등록하기 버튼을 클릭하면
            http://localhost:8006/myweb/board/insert로 POST 방식으로 요청(너무 길어지니까)
        -->
        <form id="enroll-form" action="/myweb/board/insert" 
              method="post" enctype="multipart/form-data">
            <!-- 
                사용자는 카테고리 번호, 제목, 내용, 첨부파일 입력 가능
                
                hidden 태그 이용해 작성자 아이디도 전달하기
            -->
            <input type="hidden" name="boardWriter" value="${ sessionScope.loginUser.userNo }">

            <table class="table">
                <tr>
                    <th>카테고리</th>
                    <td>
                        <select name="category">
                            <option value="10">공통</option>
                            <option value="20">운동</option>
                            <option value="30">등산</option>
                            <option value="40">게임</option>
                            <option value="50">낚시</option>
                            <option value="60">요리</option>
                            <option value="70">기타</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th>제목</th>
                    <td>
                        <input type="text" name="boardTitle" required>
                    </td>
                </tr>
                <tr>
                    <th>내용</th>
                    <td>
                        <textarea name="boardContent" required></textarea>
                    </td>
                </tr>
                <tr>
                    <th>첨부파일</th>
                    <td>
                        <input type="file" name="upfile">
                    </td>
                </tr>
            </table>

            <br><br>

            <div align="center">
                <button type="submit" class="btn btn-primary btn-sm">등록하기</button>
                <button type="reset" class="btn btn-secondary btn-sm">초기화</button>
            </div>

        </form>

        <br><br>
    </div>
</body>
</html>