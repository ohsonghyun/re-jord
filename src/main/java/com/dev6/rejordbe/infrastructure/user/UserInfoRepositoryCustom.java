package com.dev6.rejordbe.infrastructure.user;

import com.dev6.rejordbe.domain.user.dto.UserInfoForMyPage;

import java.util.Optional;

/**
 * UserInfoRepositoryCustom
 */
public interface UserInfoRepositoryCustom {

    /**
     * uid가 동일한 마이페이지 유저 정보를 반환
     *
     * @param uid {@code String} 유저 UID
     * @return {@code Optional<UserInfoForMyPage>}
     */
    Optional<UserInfoForMyPage> searchUserInfoByUid(String uid);
}
