package com.dev6.rejordbe.infrastructure.badge.read;

import com.dev6.rejordbe.domain.badge.dto.BadgeByUidResult;

import java.util.List;

/**
 * ReadBadgeRepositoryCustom
 */
public interface ReadBadgeRepositoryCustom {

    List<BadgeByUidResult> searchBadgeByUid(String uid);
}
