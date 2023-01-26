package com.dev6.rejordbe.application.challengeReview.read;

import com.dev6.rejordbe.domain.challengeReview.dto.ChallengeReviewResult;
import com.dev6.rejordbe.exception.IllegalParameterException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

/**
 * ReadChallengeReviewService
 */
public interface ReadChallengeReviewService {

    /**
     * 모든 챌린지 후기 획득
     *
     * @param offsetTime {@code LocalDateTime} 최신 데이터 판단기준 시각
     * @param pageable {@code Pageable} 페이지 정보
     * @return {@code Page<ChallengeReviewResult>}
     * @throws IllegalParameterException {@code offsetTime} 이 존재하지 안흔 경우
     */
    Page<ChallengeReviewResult> allChallengeReviews(final LocalDateTime offsetTime, final Pageable pageable);
}
