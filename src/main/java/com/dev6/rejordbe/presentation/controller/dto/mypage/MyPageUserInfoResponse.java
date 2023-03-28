package com.dev6.rejordbe.presentation.controller.dto.mypage;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * MyPageUserInfoResponse
 */
@lombok.Builder
@lombok.Getter
public class MyPageUserInfoResponse implements Serializable {

    @Schema(description = "유저 닉네임")
    private String nickname;
    @Schema(description = "리욜드 가입한 D-day")
    private Integer dDay;
    @Schema(description = "배지 개수")
    private Integer badgeAmount;
    @Schema(description = "총 발자국 수")
    private Integer totalFootprintAmount;
}
