package com.itwillbs.bookjuk.dto;

import com.itwillbs.bookjuk.domain.login.LoginType;
import com.itwillbs.bookjuk.domain.login.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {

    //내 정보
    //유저테이블 PK
    private Long userNum;
    //유저 id
    private String userId;
    //유저 password
    private String userPassword;
    //유저 이름
    private String userName;
    //유저 생년월일 1994-10-00(패턴)
    private String userBirthday;
    //유저 email
    private String userEmail;
    //유저 휴대폰번호 010-0000-0000(패턴)
    private String userPhone;
    //유저 Role 값 (enum 클래스의 정의된 것만 사용)
    private UserRole userRole;
    //유저 LoginType (enum 클래스의 정의된 것만 사용)
    private LoginType loginType;

    //======================================================================

    //이용 정보
    //대여 도서수
    private int bringBook;
    //유저 포인트
    private int userPoint;

}
