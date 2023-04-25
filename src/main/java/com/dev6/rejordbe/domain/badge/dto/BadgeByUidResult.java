package com.dev6.rejordbe.domain.badge.dto;

import com.dev6.rejordbe.domain.badge.BadgeCode;

/**
 * BadgeByUidResult
 * <p>DTO</p>
 */
@lombok.Getter
@lombok.RequiredArgsConstructor
@lombok.Builder
public class BadgeByUidResult {
    private final BadgeCode badgeCode;
    private final String badgeName;
    private final String imageUrl;

    @Override
    public boolean equals(Object obj) {
        BadgeByUidResult badge = (BadgeByUidResult) obj;

        if(this.badgeCode.equals(badge.badgeCode)) {
            return true;
        } else {
            return false;
        }
    }
}
