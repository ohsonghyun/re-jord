package com.dev6.rejordbe.application.user.userinfo;

import com.dev6.rejordbe.application.user.validate.UserInfoValidateService;
import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.domain.role.Role;
import com.dev6.rejordbe.domain.user.Users;
import com.dev6.rejordbe.domain.user.dto.UserInfoForMyPage;
import com.dev6.rejordbe.domain.user.dto.UserResult;
import com.dev6.rejordbe.exception.AlreadyUsingNicknameException;
import com.dev6.rejordbe.exception.DuplicatedNicknameException;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.exception.UserNotFoundException;
import com.dev6.rejordbe.exception.WrongPasswordException;
import com.dev6.rejordbe.infrastructure.challengeReview.read.ReadChallengeReviewRepository;
import com.dev6.rejordbe.infrastructure.user.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * UserInfoServiceImpl
 */
@Service
@Transactional(readOnly = true)
@lombok.RequiredArgsConstructor
@Slf4j
public class UserInfoServiceImpl implements UserInfoService, UserDetailsService {

    private final UserInfoRepository userInfoRepository;
    private final ReadChallengeReviewRepository readChallengeReviewRepository;
    private final UserInfoValidateService userInfoValidateService;
    private final PasswordEncoder passwordEncoder;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<UserResult> findUserByUid(@NonNull final String uid) {
        Optional<Users> userOptional = userInfoRepository.findUserByUid(uid);
        if (userOptional.isEmpty()) {
            log.info("UserInfoServiceImpl.findUserByUid: USER_NOT_FOUND: {}", uid);
            return Optional.empty();
        }
        return userOptional.map(anUser ->
                UserResult.builder()
                        .uid(anUser.getUid())
                        .userId(anUser.getUserId())
                        .nickname(anUser.getNickname())
                        .roles(
                                ObjectUtils.defaultIfNull(anUser.getRoles(), new ArrayList<Role>()).stream()
                                        .map(Role::getName)
                                        .collect(Collectors.toList())
                        )
                        .build());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public UserResult updateUserInfo(@NonNull final Users newUserInfo) {
        if (!userInfoValidateService.validateNickname(newUserInfo.getNickname(), new ArrayList<>())) {
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_NICKNAME);
        }

        Users targetUser = userInfoRepository.findById(newUserInfo.getUid())
                .orElseThrow(() -> {
                    log.info("UserInfoServiceImpl.updateUserInfo: USER_NOT_FOUND: {}", newUserInfo.getNickname());
                    return new UserNotFoundException(ExceptionCode.USER_NOT_FOUND);
                });

        if (targetUser.getNickname().equals(newUserInfo.getNickname())) {
            log.info("UserInfoServiceImpl.updateUserInfo: ALREADY_USING_NICKNAME: {}", newUserInfo.getNickname());
            throw new AlreadyUsingNicknameException(ExceptionCode.ALREADY_USING_NICKNAME);
        }

        userInfoRepository.findUserByNickname(newUserInfo.getNickname())
                .ifPresent(existingUser -> {
                    log.info("UserInfoServiceImpl.updateUserInfo: DUPLICATED_NICKNAME: {}", newUserInfo.getNickname());
                    throw new DuplicatedNicknameException(ExceptionCode.DUPLICATED_NICKNAME);
                });

        targetUser.update(Users.builder().nickname(newUserInfo.getNickname()).build());
        return UserResult.builder()
                .uid(targetUser.getUid())
                .userId(targetUser.getUserId())
                .nickname(targetUser.getNickname())
                .roles(
                        Optional.ofNullable(targetUser.getRoles())
                                .orElse(Collections.emptyList())
                                .stream()
                                .map(Role::getName)
                                .collect(Collectors.toList())
                )
                .build();
    }

    /**
     * JWT 토큰 생성을 위한 유저 정보 취득
     * <p>UsernamePasswordAuthenticationToken에 넣어준 username(uid) 값으로 유저 정보 취득</p>
     *
     * @param uid the uid identifying the user whose data is required.
     * @return {@code UserDetails}
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {
        Optional<Users> userOptional = userInfoRepository.findById(uid);
        Users anUser = userOptional.orElseThrow(() -> {
            log.error("UserInfoServiceImpl.loadUserByUsername: ILLEGAL_UID {}", uid);
            throw new UsernameNotFoundException("UserInfoServiceImpl.loadUserByUsername: ILLEGAL_UID");
        });
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        anUser.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new User(anUser.getUid(), anUser.getPassword(), authorities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserInfoForMyPage findUserInfoByUid(@NonNull final String uid) {
        UserInfoForMyPage user = userInfoRepository.searchUserInfoByUid(uid).orElseThrow(() -> {
            log.error("UserInfoServiceImpl.findUserInfoByUid: USER_NOT_FOUND: {}", uid);
            return new UserNotFoundException(ExceptionCode.USER_NOT_FOUND);
        });

        UserInfoForMyPage userInfo = readChallengeReviewRepository.searchChallengeInfoByUid(uid);

        Long dDay = ChronoUnit.DAYS.between(user.getCreatedDate(), LocalDateTime.now());

        return UserInfoForMyPage.builder()
                .totalFootprintAmount(userInfo.getTotalFootprintAmount())
                .badgeAmount(userInfo.getBadgeAmount())
                .nickname(user.getNickname())
                .dDay(Long.valueOf(dDay).intValue())
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public String deleteAccountByUid(@NonNull final String uid, @NonNull final String password) {
        Users userInfo = userInfoRepository.findUserByUid(uid).orElseThrow(() -> {
            log.error("MyPageServiceImpl.deleteAccountUserInfoByUid: USER_NOT_FOUND: {}", uid);
            return new UserNotFoundException(ExceptionCode.USER_NOT_FOUND);
        });

        if (!passwordEncoder.matches(password, userInfo.getPassword())) {
            log.error("UserController.deleteAccount: WRONG_PASSWORD: {}", password);
            throw new WrongPasswordException(ExceptionCode.WRONG_PASSWORD);
        }

        userInfoRepository.deleteById(uid);

        return userInfo.getUserId();
    }
}
