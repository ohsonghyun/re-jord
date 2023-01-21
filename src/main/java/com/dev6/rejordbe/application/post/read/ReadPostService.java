package com.dev6.rejordbe.application.post.read;

import com.dev6.rejordbe.domain.post.dto.PostResult;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

/**
 * ReadPostService
 */
public interface ReadPostService {

    /**
     * 모든 게시글 정보 획득
     *
     * @param offsetTime {@code LocalDateTime} 최신 데이터 판단기준 시각
     * @return {@code Page<PostResult>}
     */
    Page<PostResult> allPosts(final LocalDateTime offsetTime);
}
