package com.dev6.rejordbe.presentation.controller.dto.addPost;

import com.dev6.rejordbe.domain.post.PostType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * AddPostResponse
 */
@lombok.Builder
@lombok.Getter
public class AddPostResponse implements Serializable {
    @Schema(description = "게시글 ID")
    private final String postId;
    @Schema(description = "게시글 내용")
    private final String contents;
    @Schema(description = "게시글 카테고리")
    private final PostType postType;
    @Schema(description = "유저 UID")
    private final String uid;
}
