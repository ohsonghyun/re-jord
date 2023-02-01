package com.dev6.rejordbe.application.user.login;

import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.domain.user.dto.UserResult;
import com.dev6.rejordbe.exception.UserNotFoundException;
import com.dev6.rejordbe.infrastructure.user.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public UserResult logIn(final String userId, final String password) {
        return userInfoRepository.findUserByUserIdAndPassword(userId, password)
                .map(anUser -> UserResult.builder()
                        .uid(anUser.getUid())
                        .userId(anUser.getUserId())
                        .nickname(anUser.getNickname())
                        // FIXME by flowertaekk
                        //.userType(anUser.getUserType())
                        .build())
                .orElseThrow(() -> {
                    log.info("LoginServiceImpl.logIn: USER_NOT_FOUND: {}", userId);
                    return new UserNotFoundException(ExceptionCode.USER_NOT_FOUND.name());
                });
    }
}
