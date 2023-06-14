package com.dev6.rejordbe.presentation.controller.dto.deleteChallengeReview;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * DeleteChallengeReviewResponse
 */
@lombok.Builder
@lombok.Getter
public class DeleteChallengeReviewResponse  implements Serializable {

    @Schema(description = "챌린지 리뷰 게시글 ID", required = true)
    private String challengeReviewId;
}
