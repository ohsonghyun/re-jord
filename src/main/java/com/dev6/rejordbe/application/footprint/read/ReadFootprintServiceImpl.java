package com.dev6.rejordbe.application.footprint.read;

import com.dev6.rejordbe.domain.footprint.dto.FootprintResult;
import com.dev6.rejordbe.infrastructure.footprint.read.ReadFootPrintRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return readFootPrintRepository.searchAllByUid(uid, pageable);
    }
}
