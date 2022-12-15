package com.dev6.rejordbe.presentation.controller.dto.userInfo;

import com.dev6.rejordbe.domain.user.Users;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * UpdateUserInfoRequest
 */
@lombok.Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Getter
public class UpdateUserInfoRequest implements Serializable {

    @Schema(description = "유저 UID", required = true)
    private String uid;

    @Schema(description = "변경 닉네임")
    private String nickname;

    /**
     * UpdateUserInfoRequest를 Users객체로 변환
     */
    public Users toUser() {
        return Users.builder()
                .uid(this.uid)
                .nickname(this.nickname)
                .build();
    }
}
