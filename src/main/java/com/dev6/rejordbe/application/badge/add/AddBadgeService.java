package com.dev6.rejordbe.application.badge.add;

import com.dev6.rejordbe.domain.badge.dto.BadgeResult;
import com.dev6.rejordbe.exception.ChallengeReviewNotFoundException;

/**
 * AddBadgeService
 */
public interface AddBadgeService {

    /**
     * 새로운 배지 추가
     *
     * @param parentId {@code String} 새로운 챌린지 리뷰 아이디 정보
     * @return {@code BadgeResult} 추가한 배지 정보
     * @throws ChallengeReviewNotFoundException {@code parentId} 가 존재하지 않는 챌린지 리뷰 아이디일 경우
     */
    BadgeResult addBadge(final String parentId);
}
