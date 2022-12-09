package com.dev6.rejordbe.application.user.signup;

import com.dev6.rejordbe.domain.user.Users;
import com.dev6.rejordbe.domain.user.dto.UserResult;

/**
 * SignUpService
 */
public interface SignUpService {

    /**
     * 회원가입
     *
     * @param newUser {@code Users} 신규가입 유저 정보(uid를 제외한 정보)
     * @return {@code UserResult} 가입처리 결과
     */
    UserResult signUp(final Users newUser);
}
