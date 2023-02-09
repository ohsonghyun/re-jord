package com.dev6.rejordbe.domain.challenge.dto;

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
    private final String badgeId;
    private final String badgeName;
    private final Integer footprintAmount;
    private final String imgFront;
    private final String imgBack;
    private final String textColor;
}
