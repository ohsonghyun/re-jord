package com.dev6.rejordbe.domain.footprint.dto;

import com.dev6.rejordbe.domain.footprint.FootprintAcquirementType;

/**
 * FootprintResult
 * <p>DTO</p>
 */
@lombok.Getter
@lombok.RequiredArgsConstructor
@lombok.Builder
public class FootprintResult {
    private final String footprintId;
    private final Integer footprintAmount;
    private final String parentId;
    private final FootprintAcquirementType footprintAcquirementType;
}
