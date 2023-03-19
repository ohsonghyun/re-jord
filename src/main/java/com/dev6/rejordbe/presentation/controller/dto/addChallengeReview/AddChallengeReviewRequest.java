package com.dev6.rejordbe.presentation.controller.dto.addChallengeReview;

import com.dev6.rejordbe.domain.badge.BadgeCode;
import com.dev6.rejordbe.domain.challengeReview.ChallengeReview;
import com.dev6.rejordbe.domain.challengeReview.ChallengeReviewType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * AddChallengeReviewRequestDto
 */
@lombok.Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
@lombok.Getter
public class AddChallengeReviewRequest implements Serializable {

    @Schema(description = "챌린지 리뷰 내용", required = true)
    private String contents;
    @Schema(description = "챌린지 리뷰 카테고리", required = true)
    private ChallengeReviewType challengeReviewType;
    @Schema(description = "챌린지 배지 코드", required = true)
    private BadgeCode badgeCode;
    @Schema(description = "챌린지 발자국 수", required = true)
    private Integer footprintAmount;

    /**
     * AddChallengeRequest를 ChallengeReview객체로 변환
     */
    public ChallengeReview toChallengeReview() {
        return ChallengeReview.builder()
                .contents(this.contents)
                .challengeReviewType(this.challengeReviewType)
                .badgeCode(this.badgeCode)
                .footprintAmount(this.footprintAmount)
                .build();
    }
}
