package com.dev6.rejordbe.presentation.controller.dto.exception;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * ErrorResponse
 */
@lombok.Builder
@lombok.Getter
public class ErrorResponse {
    @Schema(description = "에러 메시지")
    private String message;
}
