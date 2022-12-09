package com.dev6.rejordbe.domain.user.dto;

import com.dev6.rejordbe.domain.user.UserType;
import com.dev6.rejordbe.domain.user.Users;
import lombok.Builder;
import org.springframework.lang.NonNull;

/**
 * SignUpUserRequestDto
 */
@Builder
public class SignUpUserRequestDto {

    private String uid;
    private String userId;
    private String nickname;
    private String password;
    private UserType userType;


    public Users toUsers() {
        return Users.builder()
                .uid(this.uid)
                .userId(this.userId)
                .nickname(this.nickname)
                .password(this.password)
                .userType(this.userType)
                .build();
    }
}
