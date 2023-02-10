package com.dev6.rejordbe.application.user.signup;

import com.dev6.rejordbe.application.id.IdGenerator;
import com.dev6.rejordbe.application.user.validate.UserInfoValidateService;
import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.domain.role.Role;
import com.dev6.rejordbe.domain.role.RoleType;
import com.dev6.rejordbe.domain.user.Users;
import com.dev6.rejordbe.domain.user.dto.UserResult;
import com.dev6.rejordbe.exception.DuplicatedNicknameException;
import com.dev6.rejordbe.exception.DuplicatedUserIdException;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.infrastructure.role.RoleInfoRepository;
import com.dev6.rejordbe.infrastructure.user.SignUpRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * SignUpServiceImpl
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@lombok.RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {

    private final SignUpRepository signUpRepository;
    private final RoleInfoRepository roleInfoRepository;
    private final UserInfoValidateService userInfoValidateService;
    private final IdGenerator idGenerator;
    private final PasswordEncoder passwordEncoder;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public UserResult signUp(Users newUser, List<String> roleNames) {
        final List<RuntimeException> errors = new ArrayList<>();
        boolean validationResult = validateParam(newUser, roleNames, errors);
        if (!validationResult) {
            return UserResult.builder()
                    .errors(errors)
                    .build();
        }

        signUpRepository.findUserByUserId(newUser.getUserId())
                .ifPresent(user -> errors.add(new DuplicatedUserIdException(ExceptionCode.DUPLICATED_USERID.name())));
        signUpRepository.findUserByNickname(newUser.getNickname())
                .ifPresent(user -> errors.add(new DuplicatedNicknameException(ExceptionCode.DUPLICATED_NICKNAME.name())));
        if (!errors.isEmpty()) {
            return UserResult.builder()
                    .errors(errors)
                    .build();
        }

        List<Role> roles = roleInfoRepository.findByNameIn(roleNames);

        Users saveResult = signUpRepository.save(
                Users.builder()
                        .uid(idGenerator.generate("US"))
                        .userId(newUser.getUserId())
                        // 20230224 릴리즈에는 nickname이 userId와 동일한 사양
                        .nickname(newUser.getUserId())
                        .password(passwordEncoder.encode(newUser.getPassword()))
                        .roles(roles)
                        .build());

        return UserResult.builder()
                .uid(saveResult.getUid())
                .userId(saveResult.getUserId())
                .nickname(saveResult.getNickname())
                .roles(
                        saveResult.getRoles().stream()
                                .map(Role::getName)
                                .collect(Collectors.toList()))
                .build();
    }

    /**
     * 회원가입시 필요한 유저정보 체크
     *
     * @param anUser    {@code Users}
     * @param roleNames {@code List<String>} role 리스트
     * @param errors    {@code List<RuntimeException>} validation 실패 정보를 저장할 리스트
     */
    private boolean validateParam(Users anUser, List<String> roleNames, List<RuntimeException> errors) {
        boolean userIdResult = userInfoValidateService.validateUserId(anUser.getUserId(), errors);
        boolean passwordResult = userInfoValidateService.validatePassword(anUser.getPassword(), errors);
        boolean roleResult = userInfoValidateService.validateRoleTypes(roleNames, errors);
        return userIdResult && passwordResult && roleResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String isNotDuplicatedUserId(String userId) {
        if (!userInfoValidateService.validateUserId(userId, new ArrayList<>())) {
            throw new IllegalParameterException((ExceptionCode.ILLEGAL_USERID.name()));
        }
        signUpRepository.findUserByUserId(userId).ifPresent(existingUserId -> {
            log.info("SignUpServiceImpl.checkDuplicatedUserId: DUPLICATED_USERID: {}", userId);
            throw new DuplicatedUserIdException(ExceptionCode.DUPLICATED_USERID.name());
        });
        return userId;
    }

}
