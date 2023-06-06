package com.dev6.rejordbe.infrastructure.post.read;

import com.dev6.rejordbe.domain.post.PostType;
import com.dev6.rejordbe.domain.post.dto.PostResult;
import com.dev6.rejordbe.domain.post.dto.SearchPostCond;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
    public Page<PostResult> searchPostAll(
            @NonNull final LocalDateTime offsetTime,
            @Nullable final SearchPostCond cond,
            @NonNull final Pageable pageable) {
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
                .where(
                        post.createdDate.loe(offsetTime),
                        eqPostType(cond.getPostType())
                )
                .orderBy(post.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(post.postId.count())
                .from(post)
                .where(
                        post.createdDate.loe(offsetTime),
                        eqPostType(cond.getPostType())
                )
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
     * 특정 uid 조건
     *
     * @param uid {@code String}
     * @return {@code BooleanExpression}
     */
    private BooleanExpression eqUidWith(@Nullable final String uid) {
        return StringUtils.isBlank(uid) ? null : post.user.uid.eq(uid);
    }

    /**
     * 게시글 검색 타입
     *
     * @param postType {@code PostType} 게시글 타입 조건
     * @return {@code BooleanExpression} 게시글 타입 검색 조건
     */
    private BooleanExpression eqPostType(@Nullable final PostType postType) {
        return Objects.isNull(postType) ? null : post.postType.eq(postType);
    }
}
