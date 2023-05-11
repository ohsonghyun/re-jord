package com.dev6.rejordbe.presentation.controller.dto.deletePost;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * DeletePostResponse
 */
@lombok.Builder
@lombok.Getter
public class DeletePostResponse implements Serializable {

    @Schema(description = "게시글 ID", required = true)
    private String postId;
}
