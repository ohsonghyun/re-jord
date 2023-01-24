package com.dev6.rejordbe.application.post.add;

import com.dev6.rejordbe.domain.post.Post;
import com.dev6.rejordbe.domain.post.dto.PostResult;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.exception.UserNotFoundException;

/**
 * WritePostService
 */
public interface WritePostService {

    /**
     * 새로운 게시글을 추가
     *
     * @param newPost {@code Post} 새로운 게시글 정보
     * @param uid {@code String} 새로운 게시글 작성자 uid
     * @return {@code PostResult} 추가한 게시글 정보
     * @throws IllegalParameterException {@code contents} 가 정책에 어긋나는 경우
     * @throws UserNotFoundException {@code uid} 가 존재하지 않는 유저일 경우
     */
    PostResult writePost(final Post newPost, final String uid);
}
