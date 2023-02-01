package com.dev6.rejordbe.application.user.validate;

import java.util.List;

public interface UserInfoValidateService {

    /**
     * 유저ID 정책 체크
     *
     * @param userId {@code String}
     * @param errors {@code List<RuntimeException>} validation 실패 정보를 저장할 리스트
     * @return {@code boolean} validation 성공시 true
     */
    boolean validateUserId(final String userId, final List<RuntimeException> errors);

    /**
     * 닉네임 정책 체크
     *
     * @param nickname {@code String}
     * @param errors {@code List<RuntimeException>} validation 실패 정보를 저장할 리스트
     * @return {@code boolean} validation 성공시 true
     */
    boolean validateNickname(final String nickname, final List<RuntimeException> errors);

    /**
     * 패스워드 정책 체크
     *
     * @param password {@code String}
     * @param errors {@code List<RuntimeException>} validation 실패 정보를 저장할 리스트
     * @return {@code boolean} validation 성공시 true
     */
    boolean validatePassword(final String password, final List<RuntimeException> errors);

    /**
     * ROLE 정책 체크
     *
     * @param roleNames {@code List<String>} role 목록
     * @param errors {@code List<RuntimeException>} validation 실패 정보를 저장할 리스트
     * @return {@code boolean} validation 성공시 true
     */
    boolean validateRoleTypes(final List<String> roleNames, final List<RuntimeException> errors);
}

