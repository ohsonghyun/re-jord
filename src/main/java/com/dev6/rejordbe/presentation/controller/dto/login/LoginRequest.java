package com.dev6.rejordbe.presentation.controller.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * LoginRequest
 */
@lombok.Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Getter
public class LoginRequest {
    @Schema(description = "유저 ID", required = true)
    private String userId;
    @Schema(description = "유저 패스워드", required = true)
    private String password;
}
