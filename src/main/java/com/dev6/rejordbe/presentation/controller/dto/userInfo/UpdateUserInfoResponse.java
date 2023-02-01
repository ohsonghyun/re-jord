package com.dev6.rejordbe.presentation.controller.dto.userInfo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;

/**
 * UpdateUserInfoResponse
 */
@lombok.Builder
@lombok.Getter
public class UpdateUserInfoResponse implements Serializable {

    @Schema(description = "유저 UID")
    private String uid;
    @Schema(description = "유저 ID")
    private String userId;
    @Schema(description = "유저 닉네임")
    private String nickname;
    @Schema(description = "유저 타입")
    private List<String> roles;

}
