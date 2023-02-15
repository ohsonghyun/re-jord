package com.dev6.rejordbe.domain.challenge;

import org.springframework.lang.NonNull;

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

    @Column(name = "badge_name")
    private String badgeName;

    @Column(name = "badge_id")
    private String badgeId;

    @Column(name = "img_front")
    private String imgFront;

    @Column(name = "img_back")
    private String imgBack;

    @Column(name = "text_color")
    private String textColor;

    @Column(name = "flag")
    private Boolean flag;

    public Challenge update(@NonNull final Challenge challenge) {
        this.flag = !challenge.getFlag();
        return challenge;
    }
}
