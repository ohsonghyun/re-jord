package com.dev6.rejordbe.presentation.controller.dto.addPost;

import com.dev6.rejordbe.domain.post.Post;
import com.dev6.rejordbe.domain.post.PostType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * AddPostRequestDto
 */
@lombok.Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Getter
public class AddPostRequest implements Serializable {

    @Schema(description = "게시글 ID", required = true)
    private String postId;
    @Schema(description = "게시글 내용", required = true)
    private String contents;
    @Schema(description = "게시글 카테고리", required = true)
    private PostType postType;

    /**
     * AddPostRequest를 Post객체로 변환
     */
    public Post toPost() {
        return Post.builder()
                .postId(this.postId)
                .contents(this.contents)
                .postType(this.postType)
                .build();
    }
}
