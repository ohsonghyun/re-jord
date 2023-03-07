package com.dev6.rejordbe.infrastructure.post.read;

import com.dev6.rejordbe.domain.post.dto.PostResult;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;

import static com.dev6.rejordbe.domain.post.QPost.post;

/**
 * ReadPostRepositoryCustomImpl
 */
@lombok.RequiredArgsConstructor
public class ReadPostRepositoryCustomImpl implements ReadPostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<PostResult> searchAll(final LocalDateTime offsetTime, final Pageable pageable) {
        List<PostResult> content = queryFactory.select(
                        Projections.constructor(
                                PostResult.class,
                                post.postId,
                                post.contents,
                                post.postType,
                                post.user.uid,
                                post.user.nickname,
                                post.createdDate
                        )
                )
                .from(post)
                .where(post.createdDate.loe(offsetTime))
                .orderBy(post.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(post.postId.count())
                .from(post)
                .where(post.createdDate.loe(offsetTime))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<PostResult> searchPostByUid(String uid, Pageable pageable) {
        List<PostResult> content = queryFactory.select(
                        Projections.constructor(
                                PostResult.class,
                                post.postId,
                                post.contents,
                                post.postType,
                                post.user.uid,
                                post.user.nickname,
                                post.createdDate
                        )
                )
                .from(post)
                .where(eqUidWith(uid))
                .orderBy(post.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(post.postId.count())
                .from(post)
                .where(eqUidWith(uid))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    /**
     *특정uid조건
     *
     *@paramuid{@codeString}
     *@return{@codeBooleanExpression}
     */
    private BooleanExpression eqUidWith(@Nullable final String uid) {
        return StringUtils.isBlank(uid) ? null :post.user.uid.eq(uid);
    }
}
