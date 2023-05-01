package com.dev6.rejordbe.presentation.controller.dto.updatePost;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * UpdatePostRequest
 */
@lombok.Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Getter
public class UpdatePostRequest implements Serializable {
    @Schema(description = "게시물 아이디")
    private String postId;
    @Schema(description = "게시물 내용")
    private String contents;
}
