package com.dev6.rejordbe.presentation.controller.dto.signup;

import com.dev6.rejordbe.domain.user.UserType;
import com.dev6.rejordbe.domain.user.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.io.Serializable;
import java.util.List;

/**
 * SignUpResponse
 */
@Builder
@lombok.Getter
public class SignUpResponse implements Serializable {
    @Schema(description = "유저 UID")
    private String uid;
    @Schema(description = "유저 ID")
    private String userId;
    @Schema(description = "유저 닉네임")
    private String nickname;
    @Schema(description = "유저 타입")
    private UserType userType;
    @Schema(description = "에러(에러인 경우에만)")
    private List<String> errors;
}
