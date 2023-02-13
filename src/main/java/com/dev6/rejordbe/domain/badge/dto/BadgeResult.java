package com.dev6.rejordbe.domain.badge.dto;

import com.dev6.rejordbe.domain.badge.BadgeCode;
import com.dev6.rejordbe.domain.badge.BadgeAcquirementType;

/**
 * BadgeResult
 * <p>DTO</p>
 */
@lombok.Getter
@lombok.RequiredArgsConstructor
@lombok.Builder
public class BadgeResult {
    private final String badgeId;
    private final BadgeCode badgeCode;
    private final String parentId;
    private final BadgeAcquirementType badgeAcquirementType;
}
