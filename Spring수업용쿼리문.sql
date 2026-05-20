
-- 1. 로그인용 쿼리문
-- 사용자로부터 입력받은 아이디와 비번이 모두 일치해야 통과!!
-- 단, STATUS = 'N' 인 경우는 통과되면 안됨!! (탈퇴된 회원이기 때문)
SELECT * 
  FROM MEMBER
 WHERE USER_ID = ?
   AND USER_PWD = ?
   AND STATUS = 'Y'
   
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
                 , ?)         

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
   AND STATUS = 'Y'
--> 탈퇴되지 않은 회원의 경우만 정확히 수정하기 위해

-- 4. 비밀번호 변경용 쿼리문
-- 현재 비밀번호, 변경할 비밀번호 를 입력받아서 넘겼었음!!
UPDATE MEMBER
   SET USER_PWD = ?
     , MODIFY_DATE = SYSDATE
 WHERE USER_ID = ?
   AND STATUS = 'Y'

-- 5. 회원 탈퇴용 쿼리문
-- DELETE 문이 아닌 UPDATE 문으로 처리할 예정!!
-- 특히 외래키 이슈 때문!!
UPDATE MEMBER
   SET STATUS = 'N'
     , MODIFY_DATE = SYSDATE
 WHERE USER_ID = ?
   AND STATUS = 'Y'

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
 ORDER BY NOTICE_NO DESC

-- 7. 공지사항 작성용 쿼리문
INSERT INTO NOTICE(NOTICE_NO
                 , NOTICE_TITLE
                 , NOTICE_CONTENT
                 , NOTICE_WRITER)
            VALUES(SEQ_NNO.NEXTVAL
                 , ?
                 , ?
                 , ?)

-- 8. 공지사항 조회수 증가용 쿼리문
-- 기존의 조회수에 1 증가한값을 다시 대입
UPDATE NOTICE
   SET COUNT = COUNT + 1
 WHERE NOTICE_NO = ?
   AND STATUS = 'Y'

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
   AND N.STATUS = 'Y'

-- 10. 공지사항 수정용 쿼리문
UPDATE NOTICE
   SET NOTICE_TITLE = ?
     , NOTICE_CONTENT = ?
 WHERE NOTICE_NO = ?
   AND STATUS = 'Y'

-- 11. 공지사항 삭제용 쿼리문
-- 회원탈퇴와 마찬가지로 공지사항 삭제 또한 UPDATE 로 처리할 것
UPDATE NOTICE
   SET STATUS = 'N'
 WHERE NOTICE_NO = ?
   AND STATUS = 'Y'

--------------------------------------------------------------------------------

-- 12. 일반게시글의 총 갯수를 구하는 쿼리문
-- 단, 일반게시글로 삭제되지 않은 것들만 세와야함!!
SELECT COUNT(*)
  FROM BOARD
 WHERE BOARD_TYPE = 1
   AND STATUS = 'Y'

-- 13. 페이징 처리가 적용된 게시글의 목록을 조회하는 쿼리문
-- 페이징 처리의 원리 : 우선 게시글들을 최신순으로 정렬 후 위에서부터 boardLimit 만큼 게시글을 조회해옴
-- 글번호, 카테고리명, 글제목, 작성자의 아이디, 조회수, 작성일
SELECT *
  FROM
    (
    SELECT ROWNUM RNUM, A.*
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
 WHERE RNUM BETWEEN ? AND ?
--> 우선 최신순으로 정렬이 먼저 일어나도록 인라인뷰 사용!!
--> ROWNUM 컬럼 특성 상 1부터 시작하는 값만 조회 가능하기 때문에 해결하기 위해서는 ROWNUM 컬럼에 별칭 부여!!
--  (단, WHERE 절에서는 SELECT 절에서 제시한 별칭 사용 불가!! 그래서 인라인뷰를 한겹 더 쓴것!!)

-- 14. 검색된 게시글의 갯수를 구하는 쿼리문
SELECT COUNT(*)
  FROM BOARD B
  JOIN MEMBER ON (BOARD_WRITER = USER_NO)
 WHERE BOARD_TYPE = 1
   AND B.STATUS = 'Y' -- 전체 107 건
--   AND USER_ID LIKE '%' || ? || '%' -- 작성자로 검색 (writer=ad) - 총 54 건
--   AND BOARD_TITLE LIKE '%' || ? || '%' -- 제목으로 검색 (title=10) - 총 9 건
   AND BOARD_CONTENT LIKE '%' || ? || '%' -- 내용으로 검색 (content=입니다) - 총 107 건
--> 하단 AND 절은 사용자가 어떤 condition 값을 넘겼냐에 따라 바뀌고, 
--  ? (위치홀더, 구멍) 자리에는 사용자가 입력한 keyword 가 들어가야 한다!!

-- 15. 검색 결과 목록을 구하는 쿼리문
-- 검색 결과 목록 화면은 기존의 일반게시글 목록 화면을 재활용해서 포워딩해줄 예정!!
-- 글번호, 카테고리명, 제목, 작성자의 아이디, 조회수, 작성일
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
--   AND USER_ID LIKE '%' || ? || '%' -- 작성자로 검색할 경우 (writer=ad) - 54 건
--   AND BOARD_TITLE LIKE '%' || ? || '%' -- 제목으로 검색할 경우 (title=10) - 9 건
   AND BOARD_CONTENT LIKE '%' || ? || '%' -- 내용으로 검색할 경우 (content=입니다) - 107 건
 ORDER BY BOARD_NO DESC
--> 마이바티스의 RowBounds 객체를 이용할 경우의 쿼리문 (인라인뷰 X)

-- 16. 일반게시글 작성용 쿼리문
-- 일반게시글 작성 시 필요하다면 첨부파일도 1개 같이 업로드 하기로 했음!!
--> 경우의 수를 따져보자!!
-- 16_1. 첨부파일 없이 게시글을 등록하고자 할 때
-- BOARD 테이블에 INSERT 만 한번 하면 끝
-- 16_2. 첨부파일이 있는 상태에서 게시글을 등록하고자 할 때
-- BOARD 테이블에 INSERT 한 뒤
-- 연달아 ATTACHMENT 테이블에도 INSERT 해줘야함
INSERT INTO BOARD(BOARD_NO
                , BOARD_TYPE
                , CATEGORY_NO
                , BOARD_TITLE
                , BOARD_CONTENT
                , BOARD_WRITER)
           VALUES(SEQ_BNO.NEXTVAL
                , 1
                , ?
                , ?
                , ?
                , ?)
--> 이 중 카테고리번호, 제목, 내용을 사용자로부터 직접 입력받아야함!!
--  또한, 작성자의 회원번호는 현재 로그인한 회원의 회원번호를 대면 된다!!

INSERT INTO ATTACHMENT(FILE_NO
                     , REF_BNO
                     , ORIGIN_NAME
                     , CHANGE_NAME
                     , FILE_PATH)
                VALUES(SEQ_FNO.NEXTVAL
                     , SEQ_BNO.CURRVAL
                     , ?
                     , ?
                     , ?)
--> 첨부파일 또한 입력받을 수 있도록 유도해야함!! (선택적으로 INSERT)
--> 대전제 : 이 ATTACHMENT INSERT 구문은 반드시 BOARD INSERT 가 실행된 후에 실행되야함!!
--          BOARD INSERT 후 COMMIT 은 아직 안된 상태!! (같은 트랜잭션으로 묶을거라서)
--> 첨부파일의 참조게시글번호는 직전 INSERT 에서 발생되었던 게시글 시퀀스의 CURRVAL 값으로 넣어주면됨

-- 17. 게시글 조회수 증가용 쿼리문
UPDATE BOARD
   SET COUNT = COUNT + 1
 WHERE BOARD_NO = ?
   AND STATUS = 'Y'

-- 18. 게시글 상세조회용 쿼리문
-- 카테고리명, 제목, 작성자의 아이디, 작성일, 내용을 출력해줄 예정
-- + 눈에 보이지는 않지만 글번호도 함께 조회해올 예정!! (상세조회에서 수정, 삭제로 로직을 이어붙이려면 필요)
SELECT BOARD_NO
     , CATEGORY_NAME AS CATEGORY
     , BOARD_TITLE
     , USER_ID
     , CREATE_DATE
     , BOARD_CONTENT
  FROM BOARD B
  LEFT JOIN CATEGORY USING (CATEGORY_NO)
  JOIN MEMBER ON (BOARD_WRITER = USER_NO)
 WHERE BOARD_NO = ?
   AND B.STATUS = 'Y'

-- 19. 첨부파일 상세조회용 쿼리문
SELECT FILE_NO
     , ORIGIN_NAME
     , CHANGE_NAME
     , FILE_PATH
  FROM ATTACHMENT
 WHERE REF_BNO = ?
 ORDER BY FILE_NO ASC
--> 정확도를 위해 파일번호가 작은 순서에서 부터 오름차순으로 정렬해서 보여지게끔 수정!!
--  (썸네일 이미지가 제일 먼저 조회되라고, 아까 INSERT 했던 순서를 유지해서 조회하려고)

-- 20. 게시글 삭제용 쿼리문 (UPDATE 문)   
UPDATE BOARD
   SET STATUS = 'N'
 WHERE BOARD_NO = ?
   AND STATUS = 'Y'

-- 21. 게시글 수정용 쿼리문
--> 상황에 따라 DML 문이 다르게 들어간다!!

-- 1. 기존 첨부파일이 있든 없든 간에, 새로운 첨부파일이 없을 경우
--    (ATTACHMENT 테이블을 조작하지 않아도 되는 상황)
--> BOARD 테이블에 대해서만 UPDATE 진행

-- 2. 기존 첨부파일이 없는 상황에서, 새로운 첨부파일이 있는 경우
--> BOARD 테이블에 UPDATE 후, ATTACHMENT 테이블에 INSERT 진행

-- 3. 기존 첨부파일이 있는 상황에서, 새로운 첨부파일이 있는 경우
--> BOARD 테이블에 UPDATE 후, ATTACHMENT 테이블에 UPDATE 진행

-- 21_1. 게시글만 수정해주는 쿼리문
UPDATE BOARD
   SET CATEGORY_NO = ?
     , BOARD_TITLE = ?
     , BOARD_CONTENT = ?
 WHERE BOARD_NO = ?
   AND STATUS = 'Y'

-- 21_2. 게시글 수정 시 새로 넘어온 첨부파일을 추가해주는 쿼리문
-- (기존 게시글 작성 시 ATTACHMENT 의 INSERT 구문은 CURRVAL 때문에 재활용 불가)
INSERT INTO ATTACHMENT(FILE_NO
                     , REF_BNO
                     , ORIGIN_NAME
                     , CHANGE_NAME
                     , FILE_PATH)
                VALUES(SEQ_FNO.NEXTVAL
                     , ?
                     , ?
                     , ?
                     , ?)

-- 21_3. 게시글 수정 시 새로 넘어온 첨부파일로 수정해주는 쿼리문
UPDATE ATTACHMENT
   SET ORIGIN_NAME = ?
     , CHANGE_NAME = ?
     , UPLOAD_DATE = SYSDATE
 WHERE FILE_NO = ?
--> REF_BNO 로 변경 (해도 됨 - 게시글 한개 당 첨부파일이 하나만 들어가는 구조이기 때문)
--  그래도 안전하게 한놈만 정확히 변경하기 위해 PRIMARY KEY 제약조건인 FILE_NO 으로 조건식을 작성!!
--  (지금 BOARD 테이블과 ATTACHMENT 테이블은 첨부파일이 한개짜리 혹은 여러개짜리 일반게시글, 사진게시글을
--   다 한번에 모아서 저장하는 구조이기 때문!!)

-- 22. 사진게시글 등록용 쿼리문
--> 사진게시글도 똑같이 BOARD 테이블에 INSERT 해야함!!
--  또한 카테고리 정보는 굳이 안받을 예정임 (NULL)
INSERT INTO BOARD(BOARD_NO
                , BOARD_TYPE
                , BOARD_TITLE
                , BOARD_CONTENT
                , BOARD_WRITER)
           VALUES(SEQ_BNO.NEXTVAL
                , 2
                , ?
                , ?
                , ?)

-- 첨부파일들에 대한 ATTACHMENT INSERT 구문도 작성
-- 넘어온 파일마다 원본명, 수정명, 파일경로, 파일레벨 까지는 필드로 셋팅해둠
INSERT INTO ATTACHMENT(FILE_NO
                     , REF_BNO
                     , ORIGIN_NAME
                     , CHANGE_NAME
                     , FILE_PATH
                     , FILE_LEVEL)
                VALUES(SEQ_FNO.NEXTVAL
                     , SEQ_BNO.CURRVAL
                     , ?
                     , ?
                     , ?
                     , ?)
--> 위에서 이미 BOARD 에 INSERT 된 게시글에 딸린 첨부파일 정보이므로
--  REF_BNO 컬럼값은 SEQ_BNO.CURRVAL 이 되야함!! (하나의 트랜잭션)

-- 23. 사진게시글 목록조회용 쿼리문
--> 첨부파일의 정보를 JOIN 을 통해 불러오되, 썸네일 정보만 불러와야함!!
SELECT BOARD_NO
     , BOARD_TITLE
     , COUNT
     , FILE_PATH || CHANGE_NAME AS "TITLEIMG"
  FROM BOARD B
  JOIN ATTACHMENT ON (BOARD_NO = REF_BNO)
 WHERE BOARD_TYPE = 2
   AND B.STATUS = 'Y'
   AND FILE_LEVEL = 1
 ORDER BY BOARD_NO DESC
--> 이번에는 이왕이면 저장경로와 수정파일명을 || (연결연산자) 를 통해 한번에 풀 경로로 받아온다!!

-- 24. 사진게시글 상세조회용 쿼리문
SELECT BOARD_NO
     , BOARD_TITLE
     , BOARD_CONTENT
     , USER_ID
     , CREATE_DATE
  FROM BOARD B
  JOIN MEMBER ON (BOARD_WRITER = USER_NO)
 WHERE BOARD_NO = ?
   AND B.STATUS = 'Y'
--> 우리는 사진게시글 상세조회용 퀴리문을 작성했고, 제대로 동작하는 것 까지 확인했음!!
--  그런데 혹시 몰라서 기존의 일반게시글 상세조회용 쿼리문에서 CATEGORY 테이블을 조인하는 과정에서
--  CATEGORY_NO 컬럼이 일치하는게 없더라도 BOARD 테이블에서 무조건 조회되게끔 기존의 INNER JOIN 을
--  BOARD 테이블을 기준으로 삼아서 LEFT OUTER JOIN 으로 바꿔봤더니
--  기존 일반게시글 상세조회때도 문제 없고, 이번 사진게시글 상세조회때도 잘 동작하고 있음!!
--> 기존의 쿼리문을 재활용 할 예정!! (24 번 쿼리문을 쓰지 않고 18 번 쿼리문을 수정해서 쓸 것임)

-- 25. 첨부파일 목록 조회용 쿼리문
--> 마침 이전에 일반게시글에 딸린 첨부파일을 조회하는 19 번 쿼리문 에서
--  참조게시글번호 (REF_BNO) 로 일치하는지를 조회하고 있기 때문에 충분히 사진게시글 상세조회 시에도 재활용 가능!!
--> 단, 기존의 19 번 쿼리문에서 INSERT 한 순서대로 SELECT 해오기 위해 ORDER BY 절만 추가함

--------------------------------------------------------------------------------

-- 26. 아이디 중복 체크용 쿼리문
SELECT COUNT(*)
  FROM MEMBER
 WHERE USER_ID = ?
--> 중복된 아이디가 이미 있다면 1, 없다면 0
--> 탈퇴를 했든 안했든 간에 무조건 UNIQUE 제약조건인 USER_ID 컬럼에 있는 아이디값으로는 회원가입 불가!!



















