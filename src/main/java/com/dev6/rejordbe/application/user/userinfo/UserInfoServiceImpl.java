package com.dev6.rejordbe.application.user.userinfo;

import com.dev6.rejordbe.application.user.validate.UserInfoValidateService;
import com.dev6.rejordbe.domain.user.Users;
import com.dev6.rejordbe.exception.DuplicatedNicknameException;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.exception.UserNotFoundException;
import com.dev6.rejordbe.infrastructure.user.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * UserInfoServiceImpl
 */
@Service
@Transactional(readOnly = true)
@lombok.RequiredArgsConstructor
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final UserInfoValidateService userInfoValidateService;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void updateNickname(@NonNull final Users userInfo) {
        if (!userInfoValidateService.validateNickname(userInfo.getNickname(), new ArrayList<>())) {
            throw new IllegalParameterException("ILLEGAL_NICKNAME");
        }
        userInfoRepository.findUserByNickname(userInfo.getNickname())
                .ifPresent(existingUser -> {
                    log.info("UserInfoServiceImpl.updateNickname: DUPLICATED_NICKNAME: {}", userInfo.getNickname());
                    throw new DuplicatedNicknameException("DUPLICATED_NICKNAME");
                });

        Users targetUser = userInfoRepository.findById(userInfo.getUid())
                .orElseThrow(() -> new UserNotFoundException("USER_NOT_FOUND"));

        targetUser.update(Users.builder().nickname(userInfo.getNickname()).build());
    }
}
