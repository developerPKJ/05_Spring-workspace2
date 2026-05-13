
-- 1. 로그인용 쿼리문
-- 사용자로부터 입력받은 아이디와 비번이 모두 일치해야 통과!!
-- 단, STATUS = 'N' 인 경우는 통과되면 안됨!! (탈퇴된 회원이기 때문)
SELECT * 
  FROM MEMBER
 WHERE USER_ID = ?
   AND USER_PWD = ?
   AND STATUS = 'Y';
   
-- 2. 회원가입용 쿼리문
INSERT INTO MEMBER(USER_NO
                 , USER_ID
                 , USER_PWD
                 , USER_NAME
                 , PHONE
                 , EMAIL
                 , ADDRESS)
            VALUES(SEQ_UNO.NEXTVAL
                 , ?
                 , ?
                 , ?
                 , ?
                 , ?
                 , ?)        ; 

-- 3. 회원 정보 변경용 쿼리문
-- 아이디 (고정, 식별자 역할), 이름, 전화번호, 이메일, 주소
-- + MODIFY_DATE (회원정보수정일) 까지 같이 변경
UPDATE MEMBER
   SET USER_NAME = ?
     , PHONE = ?
     , EMAIL = ?
     , ADDRESS = ?
     , MODIFY_DATE = SYSDATE
 WHERE USER_ID = ?
   AND STATUS = 'Y';
--> 탈퇴되지 않은 회원의 경우만 정확히 수정하기 위해

-- 4. 비밀번호 변경용 쿼리문
-- 현재 비밀번호, 변경할 비밀번호 를 입력받아서 넘겼었음!!
UPDATE MEMBER
   SET USER_PWD = ?
     , MODIFY_DATE = SYSDATE
 WHERE USER_ID = ?
   AND STATUS = 'Y';

-- 5. 회원 탈퇴용 쿼리문
-- DELETE 문이 아닌 UPDATE 문으로 처리할 예정!!
-- 특히 외래키 이슈 때문!!
UPDATE MEMBER
   SET STATUS = 'N'
     , MODIFY_DATE = SYSDATE
 WHERE USER_ID = ?
   AND STATUS = 'Y';

--------------------------------------------------------------------------------

-- 6. 공지사항 목록 조회용 쿼리문
SELECT NOTICE_NO
     , NOTICE_TITLE
     , USER_ID
     , COUNT
     , CREATE_DATE
  FROM NOTICE N
  JOIN MEMBER ON (NOTICE_WRITER = USER_NO)
 WHERE N.STATUS = 'Y'
 ORDER BY NOTICE_NO DESC;

-- 7. 공지사항 작성용 쿼리문
INSERT INTO NOTICE(NOTICE_NO
                 , NOTICE_TITLE
                 , NOTICE_CONTENT
                 , NOTICE_WRITER)
            VALUES(SEQ_NNO.NEXTVAL
                 , ?
                 , ?
                 , ?);

-- 8. 공지사항 조회수 증가용 쿼리문
-- 기존의 조회수에 1 증가한값을 다시 대입
UPDATE NOTICE
   SET COUNT = COUNT + 1
 WHERE NOTICE_NO = ?
   AND STATUS = 'Y';

-- 9. 공지사항 상세조회용 쿼리문
-- 제목, 내용, 작성자의 아이디, 작성일이 출력되야함
-- 눈에 노출되지는 않지만 상세보기 페이지에서 추후 수정, 삭제까지 이어갈려면
-- PRIMARY KEY 제약조건에 해당하는 컬럼인 NOTICE_NO 까지 같이 조회해야함!!
SELECT NOTICE_NO
     , NOTICE_TITLE
     , NOTICE_CONTENT
     , USER_ID
     , CREATE_DATE
  FROM NOTICE N
  JOIN MEMBER ON (NOTICE_WRITER = USER_NO)
 WHERE NOTICE_NO = ?
   AND N.STATUS = 'Y';

-- 10. 공지사항 수정용 쿼리문
UPDATE NOTICE
   SET NOTICE_TITLE = ?
     , NOTICE_CONTENT = ?
 WHERE NOTICE_NO = ?
   AND STATUS = 'Y';

-- 11. 공지사항 삭제용 쿼리문
-- 회원탈퇴와 마찬가지로 공지사항 삭제 또한 UPDATE 로 처리할 것
UPDATE NOTICE
   SET STATUS = 'N'
 WHERE NOTICE_NO = ?
   AND STATUS = 'Y';

-----------------------------------------------------------

-- 12. 일반게시글의 총 갯수를 구하는 쿼리문
-- > 일반게시글로 삭제되지 않은 것들만 조회해야함
SELECT COUNT(*)
  FROM BOARD
 WHERE BOARD_TYPE = 1
   AND STATUS = 'Y';

-- 13. 페이징 처리가 적용된 게시글의 목록을 조회하는 쿼리문
-- 글번호, 카테고리명, 제목, 작성자, 조회수, 작성일
-- > 게시글을 최신순으로 정렬 후 BOARD_LIMIT만큼 게시글을 조회
SELECT *
FROM
(
   SELECT ROWNUM AS RNUM, A.*
   FROM 
   (
      SELECT BOARD_NO
         , CATEGORY_NAME AS CATEGORY
         , BOARD_TITLE
         , USER_ID
         , COUNT
         , CREATE_DATE
      FROM BOARD B
      JOIN CATEGORY USING (CATEGORY_NO)
      JOIN MEMBER ON (BOARD_WRITER = USER_NO)
      WHERE BOARD_TYPE = 1
         AND B.STATUS = 'Y'
      ORDER BY BOARD_NO DESC
   ) A
)
WHERE RNUM BETWEEN ? AND ?;
-- ROWNUM을 1이 아닌 다른 숫자부터 시작하게 하고 싶으면 별칭 필요
-- > SELECT 뒤에 사용한 별칭은 WHERE 절에서 사용 불가능하기에 FROM으로 한번 더 포장

-- 14. 검색된 게시글의 갯수를 구하는 쿼리문
SELECT COUNT(*)
  FROM BOARD B
  JOIN MEMBER ON (BOARD_WRITER = USER_NO)
 WHERE BOARD_TYPE = 1
   AND B.STATUS = 'Y'
   AND (
         (BOARD_TITLE LIKE '%' || ? || '%')
      OR (BOARD_CONTENT LIKE '%' || ? || '%')
      OR (USER_ID LIKE '%' || ? || '%')
       );

-- 15. 검색 결과 목록을 구하는 쿼리문
-- 검색 결과 목록 화면은 기존의 일반게시글 목록 화면을 재활용해 포워딩
SELECT BOARD_NO
     , CATEGORY_NAME AS CATEGORY
     , BOARD_TITLE
     , USER_ID
     , COUNT
     , CREATE_DATE
  FROM BOARD B
  JOIN CATEGORY USING (CATEGORY_NO)
  JOIN MEMBER ON (BOARD_WRITER = USER_NO)
 WHERE BOARD_TYPE = 1
   AND B.STATUS = 'Y'
   AND (
         (BOARD_TITLE LIKE '%' || ? || '%')
      OR (BOARD_CONTENT LIKE '%' || ? || '%')
      OR (USER_ID LIKE '%' || ? || '%')
       )
 ORDER BY BOARD_NO DESC;

-- 16. 일반게시글 작성용 쿼리문
-- 16_1. 첨부파일이 없는 경우
-- > BOARD 테이블에 INSERT 하면 끝
-- 16_2. 첨부파일이 있는 경우
-- > BOARD 테이블에 INSERT 후 ATTACHMENT 테이블에 INSERT 해야함
INSERT INTO BOARD(BOARD_NO
                , BOARD_TYPE
                , CATEGORY_NO
                , BOARD_TITLE
                , BOARD_CONTENT
                , BOARD_WRITER)
           VALUES(SEQ_BNO.NEXTVAL
                 , 1  -- 일반게시글은 BOARD_TYPE이 1로 고정
                 , ?
                 , ?
                 , ?
                 , ?);
--> 카테고리 번호, 제목, 내용은 사용자로부터 직접 받고
--> 작성자는 현재 로그인한 회원 번호로 바로 입력
-- 첨부파일의 경우 입력 받을 수도 있음(선택)
INSERT INTO ATTACHMENT(FILE_NO
                     , REF_BNO
                     , ORIGIN_NAME
                     , CHANGE_NAME
                     , FILE_PATH)
               VALUES(SEQ_FNO.NEXTVAL
                     , ?  -- BOARD_NO (방금 INSERT한 게시글 번호)
                     , ?  -- 원본 파일명 (사용자에게 입력받은 값)
                     , ?  -- 변경 파일명 (서버에 업로드하면서 변경된 파일명)
                     , ?);-- 파일 경로 (서버에 업로드하면서 결정된 경로)
-- 전제조건 : BOARD 테이블에 게시글이 성공적으로 INSERT 되어야 ATTACHMENT 테이블에 INSERT 해야함