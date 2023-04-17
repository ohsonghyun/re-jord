package com.dev6.rejordbe.infrastructure.footprint.read;

import com.dev6.rejordbe.domain.footprint.dto.FootprintResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * ReadFootPrintRepositoryCustom
 */
public interface ReadFootPrintRepositoryCustom {
    /**
     * uid가 소유한 모든 발자국 정보를 반환 (페이지네이션)
     *
     * @param uid {@code String}
     * @param pageable  {@code Pageable}
     * @return {@code Page<FootPringResult>}
     */
    Page<FootprintResult> searchAllByUid(final String uid, final Pageable pageable);
}
