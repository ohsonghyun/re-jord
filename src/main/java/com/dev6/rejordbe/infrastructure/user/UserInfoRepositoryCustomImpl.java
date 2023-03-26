package com.dev6.rejordbe.infrastructure.user;

import com.dev6.rejordbe.domain.user.Users;
import com.dev6.rejordbe.domain.user.dto.UserInfoForMyPage;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

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
                .where(eqUidWith(uid))
                .fetchOne();

        return Optional.ofNullable(UserInfoForMyPage.builder()
                .nickname(user.getNickname())
                .createdDate(user.getCreatedDate())
                .build());
    }

    private BooleanExpression eqUidWith(@Nullable final String uid) {
        return StringUtils.isBlank(uid) ? null : users.uid.eq(uid);
    }
}
