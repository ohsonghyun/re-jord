package com.dev6.rejordbe.domain.challengeReview;

import com.dev6.rejordbe.domain.BaseTime;
import com.dev6.rejordbe.domain.reward.BadgeReward;
import com.dev6.rejordbe.domain.user.Users;

import javax.persistence.*;

/**
 * ChallengeReview
 */
@Entity
@lombok.Getter
@lombok.NoArgsConstructor
public class ChallengeReview extends BadgeReward {

    @lombok.Builder
    public ChallengeReview(String id, String contents, ChallengeReviewType challengeReviewType, Users user) {
        super(id);
        this.contents = contents;
        this.challengeReviewType = challengeReviewType;
        this.user = user;
    }

    @Column(name = "contents", length = 3000)
    private String contents;

    @Column(name = "challenge_review_type")
    @Enumerated(value = EnumType.STRING)
    private ChallengeReviewType challengeReviewType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private Users user;
}
