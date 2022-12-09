package com.dev6.rejordbe.presentation.controller.dto.signup;

import com.dev6.rejordbe.domain.user.UserType;
import com.dev6.rejordbe.domain.user.Users;
import lombok.Builder;

import java.io.Serializable;
import java.util.List;

/**
 * SignUpUserRequestDto
 */
@Builder
public class SignUpResponse implements Serializable {
    private String uid;
    private String userId;
    private String nickname;
    private UserType userType;
    private List<String> errors;
}
