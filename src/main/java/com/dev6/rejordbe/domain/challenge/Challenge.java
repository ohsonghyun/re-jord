package com.dev6.rejordbe.domain.challenge;

import javax.persistence.*;

/**
 * Challenge
 */
@Entity
@lombok.Getter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder
public class Challenge {

    @Id
    @Column(name = "challenge_id")
    private String challengeId;

    @Column(name = "title")
    private String title;

    @Column(name = "contents")
    private String contents;

    @Column(name = "footprint_amount")
    private Integer footprintAmount;

    // TODO 챌린지 리뷰 작성 api 올라오면 badge 테이블이랑 연결하기
    @Column(name = "badge_id")
    private String badgeId;
}
