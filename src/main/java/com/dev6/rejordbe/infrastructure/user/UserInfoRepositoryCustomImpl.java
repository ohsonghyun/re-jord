package com.dev6.rejordbe.infrastructure.user;

import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.domain.user.Users;
import com.dev6.rejordbe.domain.user.dto.UserInfoForMyPage;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

import static com.dev6.rejordbe.domain.user.QUsers.users;

/**
 * UserInfoRepositoryCustomImpl
 */
@Slf4j
@lombok.RequiredArgsConstructor
public class UserInfoRepositoryCustomImpl implements UserInfoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<UserInfoForMyPage> searchUserInfoByUid(String uid) {
        if(StringUtils.isBlank(uid)) {
            log.error("UserInfoRepositoryCustomImpl.searchUserInfoByUid: ILLEGAL_UID: {}", uid);
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_UID);
        }

        Users user = queryFactory
                .selectFrom(users)
                .where(users.uid.eq(uid))
                .fetchOne();

        return Optional.ofNullable(UserInfoForMyPage.builder()
                .nickname(user.getNickname())
                .createdDate(user.getCreatedDate())
                .build());
    }
}
