package com.dev6.rejordbe.application.badge.add;

import com.dev6.rejordbe.domain.badge.dto.BadgeResult;
import com.dev6.rejordbe.exception.ParentIdNotFoundException;

/**
 * AddBadgeService
 */
public interface AddBadgeService {

    /**
     * 새로운 배지 추가
     *
     * @param parentId {@code String} 획득 유형에 따른 부모 아이디 정보
     * @return {@code BadgeResult} 추가한 배지 정보
     * @throws ParentIdNotFoundException {@code parentId} 가 존재하지 않는 부모 아이디일 경우
     */
    BadgeResult addBadge(final String parentId);
}
