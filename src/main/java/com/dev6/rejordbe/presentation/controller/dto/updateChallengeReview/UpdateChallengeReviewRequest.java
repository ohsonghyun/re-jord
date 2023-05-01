package com.dev6.rejordbe.presentation.controller.dto.updateChallengeReview;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * UpdateChallengeReviewRequest
 */
@lombok.Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Getter
public class UpdateChallengeReviewRequest implements Serializable {
    @Schema(description = "챌린지 리뷰 게시물 아이디")
    private String challengeReviewId;
    @Schema(description = "챌린지 리뷰 게시물 내용")
    private String contents;
}
