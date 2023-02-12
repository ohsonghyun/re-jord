package com.dev6.rejordbe.infrastructure.challengeReview.read;

import com.dev6.rejordbe.domain.challengeReview.dto.ChallengeReviewResult;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static com.dev6.rejordbe.domain.challengeReview.QChallengeReview.challengeReview;

/**
 * ReadChallengeReviewRepositoryCustomImpl
 */
@lombok.RequiredArgsConstructor
public class ReadChallengeReviewRepositoryCustomImpl implements ReadChallengeReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<ChallengeReviewResult> searchAll(LocalDateTime offsetTime, Pageable pageable) {
        List<ChallengeReviewResult> content = queryFactory.select(
                        Projections.constructor(
                                ChallengeReviewResult.class,
                                challengeReview.id,
                                challengeReview.contents,
                                challengeReview.challengeReviewType,
                                challengeReview.user.uid,
                                challengeReview.user.nickname,
                                challengeReview.createdDate
                        )
                )
                .from(challengeReview)
                .where(challengeReview.createdDate.loe(offsetTime))
                .orderBy(challengeReview.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(challengeReview.id.count())
                .from(challengeReview)
                .where(challengeReview.createdDate.loe(offsetTime))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
