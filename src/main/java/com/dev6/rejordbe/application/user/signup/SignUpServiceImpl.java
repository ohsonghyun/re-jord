package com.dev6.rejordbe.application.user.signup;

import com.dev6.rejordbe.application.id.IdGenerator;
import com.dev6.rejordbe.application.user.validate.UserInfoValidateService;
import com.dev6.rejordbe.domain.user.Users;
import com.dev6.rejordbe.domain.user.dto.UserResult;
import com.dev6.rejordbe.exception.DuplicatedNicknameException;
import com.dev6.rejordbe.exception.DuplicatedUserIdException;
import com.dev6.rejordbe.infrastructure.user.SignUpRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * SignUpServiceImpl
 */
@Service
@Transactional
@lombok.RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {

    private final SignUpRepository signUpRepository;
    private final UserInfoValidateService userInfoValidateService;
    private final IdGenerator idGenerator;

    /**
     * {@inheritDoc}
     */
    @Override
    public UserResult signUp(Users newUser) {
        final List<RuntimeException> errors = new ArrayList<>();
        boolean validationResult = validateParam(newUser, errors);
        if (!validationResult) {
            return UserResult.builder()
                    .errors(errors)
                    .build();
        }

        signUpRepository.findUserByUserId(newUser.getUserId()).ifPresent(user -> errors.add(new DuplicatedUserIdException("DUPLICATED_USERID")));
        signUpRepository.findUserByNickname(newUser.getNickname()).ifPresent(user -> errors.add(new DuplicatedNicknameException("DUPLICATED_NICKNAME")));
        if (!errors.isEmpty()) {
            return UserResult.builder()
                    .errors(errors)
                    .build();
        }

        Users saveResult = signUpRepository.save(
                Users.builder()
                        .uid(idGenerator.generate("US"))
                        .userId(newUser.getUserId())
                        // 20230224 릴리즈에는 nickname이 userId와 동일한 사양
                        .nickname(newUser.getUserId())
                        .password(newUser.getPassword())
                        .userType(newUser.getUserType())
                        .build());

        return UserResult.builder()
                .uid(saveResult.getUid())
                .userId(saveResult.getUserId())
                .nickname(saveResult.getNickname())
                .userType(saveResult.getUserType())
                .build();
    }

    /**
     * 회원가입시 필요한 유저정보 체크
     *
     * @param anUser {@code Users}
     * @param errors {@code List<RuntimeException>} validation 실패 정보를 저장할 리스트
     */
    private boolean validateParam(Users anUser, List<RuntimeException> errors) {
        boolean userIdResult = userInfoValidateService.validateUserId(anUser.getUserId(), errors);
        boolean passwordResult = userInfoValidateService.validatePassword(anUser.getPassword(), errors);
        return userIdResult && passwordResult && Objects.nonNull(anUser.getUserType());
    }

}
