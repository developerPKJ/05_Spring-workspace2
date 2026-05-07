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
                 , ?);   

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
-- 아이디 (고정, 식별자 역할), 비밀번호
-- + MODIFY_DATE (회원정보수정일) 까지 같이 변경
UPDATE MEMBER
		SET USER_PWD = ?
		  , MODIFY_DATE = SYSDATE
		WHERE USER_ID = ?
		  AND STATUS = 'Y';

-- 5. 회원 탈퇴용 쿼리문
-- DELETE 문이 아닌 UPDATE 문으로 STATUS 컬럼의 값을 'N' 으로 변경하는 방식으로 탈퇴 처리
-- > 외래키 문제 때문에
UPDATE MEMBER
   SET STATUS = 'N'
     , MODIFY_DATE = SYSDATE
 WHERE USER_ID = ?
   AND STATUS = 'Y';

--------------------------------------------------------------

-- 6. 공지사항 목록 조회용 쿼리문
SELECT NOTICE_NO
     , NOTICE_TITLE
     , USER_ID
     , COUNT
     , CREATE_DATE
  FROM NOTICE N
  JOIN MEMBER M ON (N.NOTICE_WRITER = M.USER_NO)
   AND N.STATUS = 'Y'
ORDER BY N.NOTICE_NO DESC;

SELECT * FROM MEMBER;
SELECT * FROM NOTICE;

UPDATE MEMBER 
SET USER_PWD = '$2a$10$1g916SdWiPUOMkl/mzNSJ.1Ib.mlJgPYPlIdBLa/tI4a5IDjyqqf6'
WHERE USER_ID = 'admin';

UPDATE MEMBER 
SET USER_PWD = '$2a$10$W3mShuyZx/aBJKdiK14.yulcp90w07kYt774LgvwLBPDj1OkfyrY.'
WHERE USER_ID = 'user01';

UPDATE MEMBER 
SET USER_PWD = '$2a$10$ih5ZPCHksMCdONvL5hlhfOQQ2b3YRYOu1dSBc57NlyD0VzASkLHS2'
WHERE USER_ID = 'user02';

COMMIT;