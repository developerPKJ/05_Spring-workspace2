
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













