package com.dev6.rejordbe.application.user.userinfo;

import com.dev6.rejordbe.domain.user.Users;
import com.dev6.rejordbe.domain.user.dto.UserInfoForMyPage;
import com.dev6.rejordbe.domain.user.dto.UserResult;
import com.dev6.rejordbe.exception.UserNotFoundException;
import com.dev6.rejordbe.exception.WrongPasswordException;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * UserInfoService
 */
public interface UserInfoService {

    /**
     * 유저의 닉네임을 변경
     * <p>20221215: 닉네임만 변경 가능</p>
     *
     * @param newUserInfo {@code Users} 변경할 유저 정보
     * @return {@code Users} 변경된 유저 정보
     */
    UserResult updateUserInfo(@NonNull final Users newUserInfo);

    /**
     * UID로 유저 정보 획득
     *
     * @param uid {@code String}
     * @return {@code Optional<UserResult>}
     */
    Optional<UserResult> findUserByUid(@NonNull final String uid);


    /**
     * 마이페이지 유저 정보
     *
     * @param uid {@code String} 마이페이지 조회 대상 uid
     * @return {@code UserInfoForMyPage} 마이페이지 유저 정보
     * @throws UserNotFoundException {@code uid} 가 존재하지 않는 유저일 경우
     */
    UserInfoForMyPage findUserInfoByUid(@NonNull final String uid);

    /**
     * 탈퇴할 유저 정보
     *
     * @param uid {@code String} 회원 탈퇴 조회 대상 uid
     * @param password {@code String} 회원 탈퇴 조회 대상 비밀번호
     * @return {@code String} 탈퇴할 유저 id
     * @throws UserNotFoundException {@code uid} 가 존재하지 않는 유저일 경우}
     * @throws WrongPasswordException {@code password} 가 일치하지 않는 경우}
     */
    String deleteAccountByUid(@NonNull final String uid, @NonNull final String password);

}
