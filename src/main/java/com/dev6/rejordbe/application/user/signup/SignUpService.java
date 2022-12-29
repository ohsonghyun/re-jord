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

    /**
     * 회원가입시 아이디 중복 체크
     *
     * @param userId
     * @return 아이디가 없으면 ture, 있으면 false
     */
    String checkDuplicatedUserId(String userId);
}
