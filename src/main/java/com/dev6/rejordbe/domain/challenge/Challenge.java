package com.dev6.rejordbe.domain.challenge;

import com.dev6.rejordbe.domain.badge.BadgeCode;

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

    @Column(name = "badge_code")
    @Enumerated(value = EnumType.STRING)
    private BadgeCode badgeCode;

    @Column(name = "img_front")
    private String imgFront;

    @Column(name = "img_back")
    private String imgBack;

    @Column(name = "text_color")
    private String textColor;

    @Column(name = "flag")
    @Enumerated(value = EnumType.STRING)
    private ChallengeFlagType flag;

    public Challenge updateFlagToTheOtherDay() {
        this.flag = ChallengeFlagType.NOT_TODAY;
        return null;
    }

    public void updateFlagToToday() {
        this.flag = ChallengeFlagType.TODAY;
    }
}
