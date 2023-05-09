package com.dev6.rejordbe.application.post.delete;

import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.exception.PostNotFoundException;
import com.dev6.rejordbe.exception.UnauthorizedUserException;

/**
 * DeletePostService
 */
public interface DeletePostService {

    /**
     * postId가 일치하는 게시글 삭제
     *
     * @param postId {@code String} 삭제할 게시글 id
     * @param uid {@code String} 삭제할 게시글 작성한 유저 uid
     * @return {@code String} 삭제할 게시글 id
     * @throws PostNotFoundException {@code postId}가 존재하지 않는 게시글일 경우
     * @throws IllegalParameterException {@code postId}가 정책에 어긋나는 경우
     * @throws UnauthorizedUserException {@code uid}가 일치하지 않는 게시글일 경우
     */
    String deletePost(final String postId, final String uid);
}
