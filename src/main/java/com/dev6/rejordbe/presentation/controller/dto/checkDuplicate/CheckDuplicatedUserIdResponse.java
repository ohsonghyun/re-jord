package com.dev6.rejordbe.presentation.controller.dto.checkDuplicate;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * CheckDuplicatedUserIdResponse
 */
@lombok.Builder
@lombok.Getter
public class CheckDuplicatedUserIdResponse implements Serializable {

    @Schema(description = "유저 ID", required = true)
    private String userId;
}