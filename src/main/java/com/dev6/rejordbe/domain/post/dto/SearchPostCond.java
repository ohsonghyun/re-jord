package com.dev6.rejordbe.domain.post.dto;

import com.dev6.rejordbe.domain.post.PostType;

/**
 * SearchPostCond
 */
@lombok.Getter
@lombok.Builder
@lombok.NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@lombok.AllArgsConstructor
public class SearchPostCond {
    private PostType postType;
}
