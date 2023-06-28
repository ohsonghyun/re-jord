package com.dev6.rejordbe.presentation.controller.dto.deleteAccount;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * DeleteAccountResponse
 */
@lombok.Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Getter
public class DeleteAccountResponse implements Serializable {
    @Schema(description = "탈퇴할 유저 id")
    private String userId;
}
