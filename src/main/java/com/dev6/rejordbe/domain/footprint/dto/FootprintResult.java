package com.dev6.rejordbe.domain.footprint.dto;

import com.dev6.rejordbe.domain.footprint.AcquirementType;

/**
 * FootprintResult
 * <p>DTO</p>
 */
@lombok.Getter
@lombok.RequiredArgsConstructor
@lombok.Builder
public class FootprintResult {
    private final String footprintId;
    private final Integer footprintNum;
    private final String parentId;
    private final AcquirementType acquirementType;
}
