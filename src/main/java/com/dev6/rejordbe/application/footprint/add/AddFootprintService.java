package com.dev6.rejordbe.application.footprint.add;

import com.dev6.rejordbe.domain.footprint.dto.FootprintResult;
import com.dev6.rejordbe.exception.ChallengeReviewNotFoundException;

/**
 * AddFootprintService
 */
public interface AddFootprintService {

    /**
     * 새로운 발자국 추가
     *
     * @param parentId {@code String} 새로운 챌린지 리뷰 아이디 정보
     * @return {@code FootprintResult} 추가한 발자국 정보
     * @throws ChallengeReviewNotFoundException {@code parentId} 가 존재하지 않는 챌린지 리뷰 아이디일 경우
     */
    FootprintResult addFootprint(final String parentId);
}
