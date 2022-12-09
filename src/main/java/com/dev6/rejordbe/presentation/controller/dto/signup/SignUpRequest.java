package com.dev6.rejordbe.presentation.controller.dto.signup;

import com.dev6.rejordbe.domain.user.UserType;
import com.dev6.rejordbe.domain.user.Users;
import lombok.Builder;

import java.io.Serializable;

/**
 * SignUpUserRequestDto
 */
@Builder
public class SignUpRequest implements Serializable {

    private String uid;
    private String userId;
    private String nickname;
    private String password;
    private UserType userType;


    public Users toUser() {
        return Users.builder()
                .uid(this.uid)
                .userId(this.userId)
                .nickname(this.nickname)
                .password(this.password)
                .userType(this.userType)
                .build();
    }
}
