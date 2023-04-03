package com.dev6.rejordbe.infrastructure.challengeReview.read;

import com.dev6.rejordbe.domain.challengeReview.dto.ChallengeReviewResult;
import com.dev6.rejordbe.domain.user.dto.UserInfoForMyPage;
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

import static com.dev6.rejordbe.domain.badge.QBadge.*;
import static com.dev6.rejordbe.domain.challengeReview.QChallengeReview.challengeReview;
import static com.dev6.rejordbe.domain.challenge.QChallenge.challenge;
import static com.dev6.rejordbe.domain.footprint.QFootprint.footprint;

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
                                challengeReview.challengeReviewId,
                                challenge.title,
                                challengeReview.contents,
                                challengeReview.challengeReviewType,
                                challengeReview.user.uid,
                                challengeReview.user.nickname,
                                challengeReview.createdDate
                        )
                )
                .from(challengeReview)
                .join(challenge).on(challenge.challengeId.eq(challengeReview.challenge.challengeId)).fetchJoin()
                .where(challengeReview.createdDate.loe(offsetTime))
                .orderBy(challengeReview.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(challengeReview.challengeReviewId.count())
                .from(challengeReview)
                .where(challengeReview.createdDate.loe(offsetTime))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<ChallengeReviewResult> searchChallengeReviewByUid(String uid, Pageable pageable) {
        List<ChallengeReviewResult> content = queryFactory.select(
                        Projections.constructor(
                                ChallengeReviewResult.class,
                                challengeReview.challengeReviewId,
                                challenge.title,
                                challengeReview.contents,
                                challengeReview.challengeReviewType,
                                challengeReview.user.uid,
                                challengeReview.user.nickname,
                                challengeReview.createdDate
                        )
                )
                .from(challengeReview)
                .join(challenge).on(challenge.challengeId.eq(challengeReview.challenge.challengeId)).fetchJoin()
                .where(eqUidWith(uid))
                .orderBy(challengeReview.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(challengeReview.challengeReviewId.count())
                .from(challengeReview)
                .where(eqUidWith(uid))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public UserInfoForMyPage searchChallengeInfoByUid(String uid) {
        Long badgeAmount = queryFactory
                .select(badge.badgeCode.countDistinct())
                .from(badge)
                .join(challengeReview).on(challengeReview.challengeReviewId.eq(badge.parentId)).fetchJoin()
                .where(challengeReview.user.uid.eq(uid))
                .fetchOne();

        Integer totalFootprintAmount = queryFactory
                .select(footprint.footprintAmount.sum().coalesce(0))
                .from(footprint)
                .join(challengeReview).on(challengeReview.challengeReviewId.eq(footprint.parentId)).fetchJoin()
                .where(challengeReview.user.uid.eq(uid))
                .fetchOne();

        return UserInfoForMyPage.builder()
                .badgeAmount(Long.valueOf(badgeAmount).intValue())
                .totalFootprintAmount(totalFootprintAmount)
                .build();
    }

    /**
     * 특정 uid 조건
     *
     * @param uid {@code String}
     * @return {@code BooleanExpression}
     */
    private BooleanExpression eqUidWith(@Nullable final String uid) {
        return StringUtils.isBlank(uid) ? null :challengeReview.user.uid.eq(uid);
    }
}
