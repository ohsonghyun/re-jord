package com.dev6.rejordbe.application.footprint.add;

import com.dev6.rejordbe.domain.footprint.dto.FootprintResult;
import com.dev6.rejordbe.exception.ParentIdNotFoundException;

/**
 * AddFootprintService
 */
public interface AddFootprintService {

    /**
     * 새로운 발자국 추가
     *
     * @param parentId {@code String} 획득 유형에 따른 부모 아이디 정보
     * @return {@code FootprintResult} 추가한 발자국 정보
     * @throws ParentIdNotFoundException {@code parentId} 가 존재하지 않는 부모 아이디일 경우
     */
    FootprintResult addFootprint(final String parentId);
}
