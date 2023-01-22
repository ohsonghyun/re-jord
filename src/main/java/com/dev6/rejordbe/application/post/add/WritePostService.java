package com.dev6.rejordbe.application.post.add;

import com.dev6.rejordbe.domain.post.Post;
import com.dev6.rejordbe.domain.post.dto.PostResult;

/**
 * WritePostService
 */
public interface WritePostService {
    /**
     * 새로운 게시글을 추가
     *
     * @param newPost {@code Post} 새로운 게시글 정보
     * @return {@code PostResult} 추가한 게시글
     * TODO throw 정보 추가해주세요
     */
    PostResult writePost(final Post newPost, final String uid);
}
