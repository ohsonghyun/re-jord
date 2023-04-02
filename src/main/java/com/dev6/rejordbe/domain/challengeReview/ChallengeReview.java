package com.dev6.rejordbe.domain.challengeReview;

import com.dev6.rejordbe.domain.BaseTime;
import com.dev6.rejordbe.domain.badge.Badge;
import com.dev6.rejordbe.domain.challenge.Challenge;
import com.dev6.rejordbe.domain.footprint.Footprint;
import com.dev6.rejordbe.domain.user.Users;

import javax.persistence.*;

/**
 * ChallengeReview
 */
@Entity
@lombok.Getter
@lombok.Builder
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class ChallengeReview extends BaseTime {

    @Id
    @Column(name = "challenge_review_id")
    private String challengeReviewId;

    @Column(name = "contents", length = 3000)
    private String contents;

    @Column(name = "challenge_review_type")
    @Enumerated(value = EnumType.STRING)
    private ChallengeReviewType challengeReviewType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

}
