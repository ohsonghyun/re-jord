package com.dev6.rejordbe.domain.challengeReview;

import com.dev6.rejordbe.domain.BaseTime;
import com.dev6.rejordbe.domain.badge.Badge;
import com.dev6.rejordbe.domain.challenge.Challenge;
import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.domain.footprint.Footprint;
import com.dev6.rejordbe.domain.user.Users;
import com.dev6.rejordbe.exception.IllegalParameterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import javax.persistence.*;

/**
 * ChallengeReview
 */
@Entity
@Slf4j
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "footprint_id")
    private Footprint footprint;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "badge_id")
    private Badge badge;

    /**
     * 챌린지 리뷰 게시글 내용 수정
     *
     * @param challengeReview {@code ChallengeReview} 수정 정보
     */
    public void update(@NonNull final ChallengeReview challengeReview) {
        if(!org.springframework.util.StringUtils.hasText(challengeReview.getContents())) {
            log.info("ChallengeReview.update: ILLEGAL_CONTENTS: 챌린지 리뷰 게시글 내용이 null 또는 EmptyString");
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_CONTENTS);
        }
        this.contents = challengeReview.getContents();
    }
}
