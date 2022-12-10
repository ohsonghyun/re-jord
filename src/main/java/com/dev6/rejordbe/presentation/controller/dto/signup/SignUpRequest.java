package com.dev6.rejordbe.presentation.controller.dto.signup;

import com.dev6.rejordbe.domain.user.UserType;
import com.dev6.rejordbe.domain.user.Users;

import java.io.Serializable;

/**
 * SignUpUserRequestDto
 */
@lombok.Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Getter
public class SignUpRequest implements Serializable {

    private String userId;
    private String nickname;
    private String password;
    private UserType userType;


    public Users toUser() {
        return Users.builder()
                .userId(this.userId)
                .nickname(this.nickname)
                .password(this.password)
                .userType(this.userType)
                .build();
    }
}
