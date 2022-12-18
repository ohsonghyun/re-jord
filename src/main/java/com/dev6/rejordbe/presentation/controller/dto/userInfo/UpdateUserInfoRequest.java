package com.dev6.rejordbe.presentation.controller.dto.userInfo;

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
    @Schema(description = "변경 닉네임")
    private String nickname;
}
