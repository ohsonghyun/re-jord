package com.dev6.rejordbe.domain.challengeReview.dto;

import com.dev6.rejordbe.domain.challengeReview.ChallengeReviewType;

import java.time.LocalDateTime;

/**
 * ChallengeReviewResult
 * <p>DTO</p>
 */
@lombok.Getter
@lombok.RequiredArgsConstructor
@lombok.Builder
public class ChallengeReviewResult {
    private final String challengeReviewId;
    private final String title;
    private final String contents;
    private final ChallengeReviewType challengeReviewType;
    private final String uid;
    private final String nickname;
    private final LocalDateTime createdDate;
}
