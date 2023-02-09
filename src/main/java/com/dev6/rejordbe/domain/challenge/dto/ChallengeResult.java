package com.dev6.rejordbe.domain.challenge.dto;

import java.awt.*;

/**
 * ChallengeResult
 * <p>DTO</p>
 */
@lombok.Getter
@lombok.RequiredArgsConstructor
@lombok.Builder
public class ChallengeResult {

    private final String challengeId;
    private final String title;
    private final String contents;
    private final Integer footprintAmount;
    private final String badgeId;
    private final Image iconImage;
    private final String imgColor;
}
