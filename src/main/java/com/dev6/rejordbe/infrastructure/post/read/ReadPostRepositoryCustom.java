package com.dev6.rejordbe.infrastructure.post.read;

import com.dev6.rejordbe.domain.post.dto.PostResult;
import com.dev6.rejordbe.domain.post.dto.SearchPostCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

/**
 * PostRepositoryReadCustom
 */
public interface ReadPostRepositoryCustom {

    /**
     * 조건에 맞는 모든 일반 피드를 반환 (페이지네이션)
     *
     * @param offsetTime {@code LocalDateTime} 최신 게시글 판단기준 시각
     * @param cond {@code SearchPostCond}
     * @param pageable  {@code Pageable}
     * @return {@code Page<PostResult>}
     */
    Page<PostResult> searchPostAll(final LocalDateTime offsetTime, final SearchPostCond cond, final Pageable pageable);

    /**
     * uid가 동일한 모든 일반 피드를 반환 (페이지네이션)
     *
     * @param uid {@code String} 유저 UID
     * @param cond {@code SearchPostCond} 게시글 조회 조건
     * @param pageable {@code Pageable}
     * @return {@code Page<PostResult>}
     */
    Page<PostResult> searchPostByUid(final String uid, final SearchPostCond cond, final Pageable pageable);
}
