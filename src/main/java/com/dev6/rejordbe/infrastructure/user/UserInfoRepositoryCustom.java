package com.dev6.rejordbe.infrastructure.user;

import com.dev6.rejordbe.domain.user.dto.UserInfoForMyPage;

import java.util.Optional;

/**
 * UserInfoRepositoryCustom
 */
public interface UserInfoRepositoryCustom {

    /**
     * 마이페이지 유저 정보 반환
     *
     * @param uid {@code String} 유저 UID
     * @return {@code UserInfoForMyPage}
     */
    Optional<UserInfoForMyPage> searchUserInfoByUid(final String uid);
}
