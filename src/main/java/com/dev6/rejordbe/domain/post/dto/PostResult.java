package com.dev6.rejordbe.domain.post.dto;

import com.dev6.rejordbe.domain.post.PostType;

/**
 * PostResult
 * <p>DTO</p>
 */
@lombok.Getter
@lombok.RequiredArgsConstructor
@lombok.Builder
public class PostResult {
    private final String postId;
    private final String contents;
    private final PostType postType;
    private final String uid;
}
