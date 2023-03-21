package com.dev6.rejordbe.domain.user.dto;

import java.time.LocalDateTime;

/**
 * UserInfoForMyPage
 * <p>DTO</p>
 */
@lombok.Getter
@lombok.RequiredArgsConstructor
@lombok.Builder
public class UserInfoForMyPage {
    private final String nickname;
    private final Integer dDay;
    private final Integer badgeAmount;
    private final Integer totalFootprintAmount;
}
