package com.dev6.rejordbe.infrastructure.user;

import com.dev6.rejordbe.domain.user.Users;
import com.dev6.rejordbe.domain.user.dto.UserInfoForMyPage;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.Optional;

import static com.dev6.rejordbe.domain.user.QUsers.users;

/**
 * UserInfoRepositoryCustomImpl
 */
@lombok.RequiredArgsConstructor
public class UserInfoRepositoryCustomImpl implements UserInfoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<UserInfoForMyPage> searchUserInfoByUid(String uid) {
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
