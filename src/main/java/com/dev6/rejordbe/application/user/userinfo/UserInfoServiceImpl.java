package com.dev6.rejordbe.application.user.userinfo;

import com.dev6.rejordbe.application.user.validate.UserInfoValidateService;
import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.domain.user.Users;
import com.dev6.rejordbe.domain.user.dto.UserResult;
import com.dev6.rejordbe.exception.DuplicatedNicknameException;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.exception.UserNotFoundException;
import com.dev6.rejordbe.infrastructure.user.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

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
    public UserResult updateUserInfo(@NonNull final Users newUserInfo) {
        if (!userInfoValidateService.validateNickname(newUserInfo.getNickname(), new ArrayList<>())) {
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_NICKNAME.name());
        }
        userInfoRepository.findUserByNickname(newUserInfo.getNickname())
                .ifPresent(existingUser -> {
                    log.info("UserInfoServiceImpl.updateNickname: DUPLICATED_NICKNAME: {}", newUserInfo.getNickname());
                    throw new DuplicatedNicknameException(ExceptionCode.DUPLICATED_NICKNAME.name());
                });

        Users targetUser = userInfoRepository.findById(newUserInfo.getUid())
                .orElseThrow(() -> new UserNotFoundException(ExceptionCode.USER_NOT_FOUND.name()));

        targetUser.update(Users.builder().nickname(newUserInfo.getNickname()).build());
        return UserResult.builder()
                .uid(targetUser.getUid())
                .userId(targetUser.getUserId())
                .nickname(targetUser.getNickname())
                // FIXME by flowertaekk
                //.userType(targetUser.getUserType())
                .build();
    }
}
