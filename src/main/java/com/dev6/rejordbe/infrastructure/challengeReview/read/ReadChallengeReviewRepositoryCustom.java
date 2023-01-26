package com.dev6.rejordbe.infrastructure.challengeReview.read;

import com.dev6.rejordbe.domain.challengeReview.dto.ChallengeReviewResult;
import com.dev6.rejordbe.domain.post.dto.PostResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

/**
 * ReadChallengeReviewRepositoryCustom
 */
public interface ReadChallengeReviewRepositoryCustom {

    /**
     * 조건에 맞는 모든 일반 챌린지 게시글을 반환 (페이지네이션)
     *
     * @param offsetTime {@code LocalDateTime} 최신 챌린지 게시글 판단기준 시각
     * @param pageable  {@code Pageable}
     * @return {@code Page<ChallengeReviewResult>}
     */
    Page<ChallengeReviewResult> searchAll(final LocalDateTime offsetTime, final Pageable pageable);
}
