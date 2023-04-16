package com.dev6.rejordbe.infrastructure.badge.read;

import com.dev6.rejordbe.domain.badge.dto.BadgeByUidResult;
import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.dev6.rejordbe.domain.badge.QBadge.badge;
import static com.dev6.rejordbe.domain.badge.QBadgeImage.badgeImage;
import static com.dev6.rejordbe.domain.challengeReview.QChallengeReview.challengeReview;

/**
 * ReadBadgeRepositoryCustomImpl
 */
@Slf4j
@lombok.RequiredArgsConstructor
public class ReadBadgeRepositoryCustomImpl implements ReadBadgeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BadgeByUidResult> searchBadgeByUid(String uid) {
        if(StringUtils.isBlank(uid)) {
            log.error("ReadBadgeRepositoryCustomImpl.searchBadgeByUid: ILLEGAL_UID: {}", uid);
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_UID);
        }

        return queryFactory
                .select(
                        Projections.constructor(
                                BadgeByUidResult.class,
                                badge.badgeCode,
                                badgeImage.imageUrl
                        )
                ).distinct()
                .from(badge)
                .join(challengeReview).on(challengeReview.challengeReviewId.eq(badge.parentId)).fetchJoin()
                .join(badgeImage).on(badgeImage.badgeCode.eq(badge.badgeCode)).fetchJoin()
                .where(challengeReview.user.uid.eq(uid))
                .fetch();
    }
}
