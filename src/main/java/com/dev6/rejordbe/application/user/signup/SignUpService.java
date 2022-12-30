package com.dev6.rejordbe.application.user.signup;

import com.dev6.rejordbe.domain.user.Users;
import com.dev6.rejordbe.domain.user.dto.UserResult;
import com.dev6.rejordbe.exception.DuplicatedUserIdException;
import com.dev6.rejordbe.exception.IllegalParameterException;

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
     * 아이디 중복 체크
     *
     * @param userId {@code String} 확인대상 유저ID
     * @return {@code String} 확인대상 유저ID
     * @throws IllegalParameterException {@code userId} 가 정책에 어긋나는 경우
     * @throws DuplicatedUserIdException {@code userId} 가 중복되는 경우
     */
    String isNotDuplicatedUserId(String userId);
}
