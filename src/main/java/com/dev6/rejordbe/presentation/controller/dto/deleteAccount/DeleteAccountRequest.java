package com.dev6.rejordbe.presentation.controller.dto.deleteAccount;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * deleteAccountRequest
 */
@lombok.Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Getter
public class DeleteAccountRequest implements Serializable {
    @Schema(description = "탈퇴할 유저 비밀번호")
    private String password;
}
