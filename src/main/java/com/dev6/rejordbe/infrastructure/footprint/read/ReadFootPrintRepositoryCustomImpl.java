package com.dev6.rejordbe.infrastructure.footprint.read;

import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.domain.footprint.dto.FootprintResult;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import java.util.List;

import static com.dev6.rejordbe.domain.challengeReview.QChallengeReview.challengeReview;
import static com.dev6.rejordbe.domain.footprint.QFootprint.footprint;

/**
 * ReadFootPrintRepositoryCustomImpl
 */
@Slf4j
@lombok.RequiredArgsConstructor
public class ReadFootPrintRepositoryCustomImpl implements ReadFootPrintRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<FootprintResult> searchAllByUid(
            @NonNull String uid,
            @NonNull Pageable pageable
    ) {
        if (StringUtils.isBlank(uid)) {
            log.error("ReadFootPrintRepositoryCustomImpl.searchAllByUid: {}: uid is blank", ExceptionCode.ILLEGAL_PARAM);
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_PARAM);
        }

        List<FootprintResult> content = queryFactory.select(
                        Projections.constructor(
                                FootprintResult.class,
                                footprint.footprintId,
                                footprint.footprintAmount,
                                footprint.parentId,
                                footprint.footprintAcquirementType,
                                challengeReview.challenge.title,
                                challengeReview.challenge.badgeCode,
                                footprint.createdDate
                        )
                ).from(footprint)
                .join(challengeReview)
                .on(challengeReview.challengeReviewId.eq(footprint.parentId)).fetchJoin()
                .where(eqUid(uid))
                .orderBy(footprint.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory.select(footprint.footprintId.count())
                .from(footprint)
                .join(challengeReview)
                .on(challengeReview.challengeReviewId.eq(footprint.parentId)).fetchJoin()
                .where(eqUid(uid))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * Uid 비교 조건문
     *
     * @param uid {@String} 비교 대상 UID
     * @return {@code BooleanExpression}
     */
    private BooleanExpression eqUid(final String uid) {
        return StringUtils.isBlank(uid) ? null : challengeReview.user.uid.eq(uid);

    }
}
