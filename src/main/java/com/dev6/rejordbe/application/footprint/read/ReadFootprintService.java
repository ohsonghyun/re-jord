package com.dev6.rejordbe.application.footprint.read;

import com.dev6.rejordbe.domain.footprint.dto.FootprintResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

/**
 * ReadFootPrintService
 */
public interface ReadFootprintService {

    /**
     * UID로 모든 발자국을 취득
     *
     * @param uid {@code String} uid
     * @param pageable {@code Pageable} page정보
     * @return {@code Page<FootprintResult>}
     */
    Page<FootprintResult> searchAllByUid(
            @NonNull String uid,
            @NonNull Pageable pageable
    );
}
