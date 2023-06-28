package com.dev6.rejordbe.application.post.read;

import com.dev6.rejordbe.domain.post.Post;
import com.dev6.rejordbe.domain.post.dto.PostResult;
import com.dev6.rejordbe.domain.post.dto.SearchPostCond;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.exception.UserNotFoundException;
import com.dev6.rejordbe.exception.PostNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

/**
 * ReadPostService
 */
public interface ReadPostService {

    /**
     * 모든 게시글 정보 획득
     *
     * @param offsetTime {@code LocalDateTime} 최신 데이터 판단기준 시각
     * @param pageable {@code Pageable} 페이지 정보
     * @return {@code Page<PostResult>}
     * @throws IllegalParameterException {@code offsetTime} 이 존재하지 않은 경우
     */
    Page<PostResult> allPosts(final LocalDateTime offsetTime, final SearchPostCond cond, final Pageable pageable);

    /**
     * uid가 작성한 게시글 정보 획득
     *
     * @param uid {@code String} 유저 UID
     * @param cond {@code SearchPostCond} 게시글 조회 조건
     * @param pageable {@code Pageable} 페이지 정보
     * @return {@code Page<PostResult>}
     * @throws IllegalParameterException {@code offsetTime} 이 존재하지 않은 경우
     * @throws UserNotFoundException{@code uid} 존재하지 않는 유저인 경우
     */
    Page<PostResult> postsWrittenByUid(final String uid, final SearchPostCond cond, final Pageable pageable);

    /**
     * 게시글 내용 변경
     *
     * @param newPostInfo {@code Post} 변경할 게시글 정보
     * @return {@code PostResult} 변경된 게시글 정보
     * @throws PostNotFoundException {@code postId} 가 존재하지 않는 게시글일 경우
     * @throws IllegalParameterException {@code contents} 가 정책에 어긋나는 경우
     */
    PostResult updatePostInfo(@NonNull final Post newPostInfo);
}
