package com.dev6.rejordbe.application.user.userinfo;

import com.dev6.rejordbe.domain.user.Users;
import com.dev6.rejordbe.domain.user.dto.UserResult;
import org.springframework.lang.NonNull;

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
}
