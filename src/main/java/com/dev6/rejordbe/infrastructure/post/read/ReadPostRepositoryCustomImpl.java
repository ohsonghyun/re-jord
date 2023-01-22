package com.dev6.rejordbe.infrastructure.post.read;

import com.dev6.rejordbe.domain.post.dto.PostResult;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
                                post.user.uid
                        )
                )
                .from(post)
                .where(post.createdDate.loe(offsetTime))
                .orderBy(post.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(post.count())
                .from(post)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchFirst();

        return new PageImpl<>(content, pageable, total);
    }
}
