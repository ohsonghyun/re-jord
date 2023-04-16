package com.dev6.rejordbe.application.badge.read;

import com.dev6.rejordbe.domain.badge.dto.BadgeByUidResult;

import java.util.List;

/**
 * BadgeInfoService
 */
public interface BadgeInfoService {

    /**
     * UID가 갖고있는 배지 획득
     * @param uid {@code String} 유저 UID
     * @return {@code List<BadgeByUidResult>}
     * @throws
     */
    List<BadgeByUidResult> findMyBadge(final String uid);
}
