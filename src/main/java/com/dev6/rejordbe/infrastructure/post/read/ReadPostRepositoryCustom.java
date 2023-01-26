package com.dev6.rejordbe.infrastructure.post.read;

import com.dev6.rejordbe.domain.post.dto.PostResult;
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
     * @param pageable  {@code Pageable}
     * @return {@code Page<PostResult>}
     */
    Page<PostResult> searchAll(final LocalDateTime offsetTime, final Pageable pageable);
}