package com.dev6.rejordbe.application.user.login;

import com.dev6.rejordbe.domain.user.dto.UserResult;
import com.dev6.rejordbe.exception.UserNotFoundException;

/**
 * LoginService
 */
public interface LoginService {

    /**
     * 로그인
     *
     * @param userId {@code String} 로그인 대상 userId
     * @return {@code UserResult} 로그인 유저 정보
     * @throws UserNotFoundException {@code userId} 와 일치하는 정보가 없는 경우
     */
    UserResult findUserByUserId(final String userId);
}
