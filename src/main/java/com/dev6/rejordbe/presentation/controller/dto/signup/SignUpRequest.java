package com.dev6.rejordbe.presentation.controller.dto.signup;

import com.dev6.rejordbe.domain.user.Users;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;

/**
 * SignUpUserRequestDto
 */
@lombok.Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Getter
public class SignUpRequest implements Serializable {

    @Schema(description = "유저 ID", required = true)
    private String userId;

    @Schema(description = "유저 패스워드", required = true)
    private String password;

    @Schema(description = "유저 타입", example = "[ROLE_USER, ROLE_ADMIN]", required = true)
    private List<String> roles;

    /**
     * SignUpRequest를 Users객체로 변환
     */
    public Users toUser() {
        return Users.builder()
                .userId(this.userId)
                .password(this.password)
                .build();
    }
}
