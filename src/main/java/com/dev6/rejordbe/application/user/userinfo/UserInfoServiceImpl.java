package com.dev6.rejordbe.application.user.userinfo;

import com.dev6.rejordbe.application.user.validate.UserInfoValidateService;
import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.domain.role.Role;
import com.dev6.rejordbe.domain.user.Users;
import com.dev6.rejordbe.domain.user.dto.UserResult;
import com.dev6.rejordbe.exception.DuplicatedNicknameException;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.exception.UserNotFoundException;
import com.dev6.rejordbe.infrastructure.user.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final UserInfoValidateService userInfoValidateService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<UserResult> findUserByUid(@NonNull final String uid) {
        Optional<Users> userOptional = userInfoRepository.findUserByUid(uid);
        if (userOptional.isEmpty()) {
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
}
