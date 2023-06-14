package com.dev6.rejordbe.application.footprint.read;

import com.dev6.rejordbe.domain.footprint.dto.FootprintResult;
import com.dev6.rejordbe.infrastructure.footprint.read.ReadFootPrintRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * ReadFootprintServiceImpl
 */
@Service
@Transactional
@lombok.RequiredArgsConstructor
public class ReadFootprintServiceImpl implements ReadFootprintService {

    private final ReadFootPrintRepository readFootPrintRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<FootprintResult> searchAllByUid(
            @NonNull String uid,
            @NonNull Pageable pageable
    ) {
        Page<FootprintResult> footprints = readFootPrintRepository.searchAllByUid(uid, pageable);

        List<FootprintResult> content = new ArrayList<>();

        for(FootprintResult footprint : footprints.getContent()) {
            content.add(FootprintResult.builder()
                    .footprintId(footprint.getFootprintId())
                    .footprintAmount(footprint.getFootprintAmount())
                    .parentId(footprint.getParentId())
                    .footprintAcquirementType(footprint.getFootprintAcquirementType())
                    .title(footprint.getTitle())
                    .badgeCode(footprint.getBadgeCode())
                    .badgeName(footprint.getBadgeCode().getBadgeName())
                    .createdDate(footprint.getCreatedDate())
                    .build());
        }

        return new PageImpl<>(content, pageable, footprints.getTotalElements());
    }
}
