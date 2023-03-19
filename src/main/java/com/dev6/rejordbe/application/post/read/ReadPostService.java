package com.dev6.rejordbe.application.post.read;

import com.dev6.rejordbe.domain.post.dto.PostResult;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    Page<PostResult> allPosts(final LocalDateTime offsetTime, final Pageable pageable);

    /**
     * uid가 작성한 게시글 정보 획득
     *
     * @param uid {@code String} 유저 UID
     * @param pageable {@code Pageable} 페이지 정보
     * @return {@code Page<PostResult>}
     * @throws IllegalParameterException {@code offsetTime} 이 존재하지 않은 경우
     * @throws UserNotFoundException{@code uid} 존재하지 않는 유저인 경우
     */
    Page<PostResult> postsWrittenByUid(final String uid, final Pageable pageable);
}
