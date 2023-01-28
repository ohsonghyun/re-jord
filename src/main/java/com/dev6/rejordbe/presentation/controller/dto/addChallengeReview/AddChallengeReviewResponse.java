package com.dev6.rejordbe.presentation.controller.dto.addChallengeReview;

import com.dev6.rejordbe.domain.challengeReview.ChallengeReviewType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * AddChallengeReviewResponseDto
 */
@lombok.Builder
@lombok.Getter
public class AddChallengeReviewResponse implements Serializable {
    @Schema(description = "챌린지 리뷰 ID")
    private final String challengeReviewId;
    @Schema(description = "챌린지 리뷰 내용")
    private final String contents;
    @Schema(description = "챌린지 리뷰 카테고리")
    private final ChallengeReviewType challengeReviewType;
    @Schema(description = "유저 UID")
    private final String uid;
}
