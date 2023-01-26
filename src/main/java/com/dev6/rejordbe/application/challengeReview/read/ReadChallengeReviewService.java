package com.dev6.rejordbe.application.challengeReview.read;

import com.dev6.rejordbe.domain.post.dto.PostResult;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

/**
 * ReadChallengeReviewService
 */
public interface ReadChallengeReviewService {

    /**
     * 모든 챌린지 후기 획득
     *
     * @param offsetTime {@code LocalDateTime} 최신 데이터 판단기준 시각
     * @return {@code Page<PostResult>}
     */
    Page<PostResult> allChallengeReview(final LocalDateTime offsetTime);
}
