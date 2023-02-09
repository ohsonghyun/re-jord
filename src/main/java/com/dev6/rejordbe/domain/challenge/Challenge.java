package com.dev6.rejordbe.domain.challenge;

import javax.persistence.*;
import java.awt.*;

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
    private Image imgFront;

    @Column(name = "img_back")
    private Image imgBack;

    @Column(name = "text_color")
    private String textColor;
}
