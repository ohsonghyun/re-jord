package com.dev6.rejordbe.application.user.login;

import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.domain.role.Role;
import com.dev6.rejordbe.domain.user.dto.UserResult;
import com.dev6.rejordbe.exception.UserNotFoundException;
import com.dev6.rejordbe.infrastructure.user.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * LoginServiceImpl
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@lombok.RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserInfoRepository userInfoRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public UserResult findUserByUserId(final String userId) {
        return userInfoRepository.findUserByUserId(userId)
                .map(anUser -> UserResult.builder()
                        .uid(anUser.getUid())
                        .userId(anUser.getUserId())
                        .nickname(anUser.getNickname())
                        .roles(
                                ObjectUtils.defaultIfNull(anUser.getRoles(), new ArrayList<Role>()).stream()
                                        .map(Role::getName)
                                        .collect(Collectors.toList())
                        )
                        .build())
                .orElseThrow(() -> {
                    log.info("LoginServiceImpl.findUserByUserId: USER_NOT_FOUND: {}", userId);
                    return new UserNotFoundException(ExceptionCode.USER_NOT_FOUND);
                });
    }
}
