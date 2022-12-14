package com.dev6.rejordbe.application.user.userinfo;

import com.dev6.rejordbe.domain.user.Users;
import org.springframework.lang.NonNull;

/**
 * UserInfoService
 */
public interface UserInfoService {

    /**
     * 유저의 닉네임을 변경
     *
     * @param userInfo {@code Users} 변경할 유저 정보
     */
    void updateNickname(@NonNull final Users userInfo);
}
