package com.dev6.rejordbe.domain.user.dto;

import com.dev6.rejordbe.domain.user.UserType;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * UserResult
 * <p>DTO</p>
 */
@lombok.Getter
@lombok.RequiredArgsConstructor
@lombok.Builder
public class UserResult {
    private final String uid;
    private final String userId;
    private final String nickname;
    @Nullable
    private final String password;
    private final UserType userType;
    @Nullable
    private final List<RuntimeException> errors;
}
