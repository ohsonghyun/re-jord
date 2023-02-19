package com.dev6.rejordbe.infrastructure.challenge.read;

import com.dev6.rejordbe.domain.challenge.Challenge;
import com.dev6.rejordbe.domain.challenge.ChallengeFlagType;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

import static com.dev6.rejordbe.domain.challenge.QChallenge.challenge;

/**
 * ReadChallengeRepositoryCustomImpl
 */
@lombok.RequiredArgsConstructor
public class ReadChallengeRepositoryCustomImpl implements ReadChallengeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * {@inheritDoc}
     */
    @Override
    public Challenge randomChallenge() {
        List<Challenge> random = queryFactory.selectFrom(challenge)
                .where(challenge.flag.eq(ChallengeFlagType.NOT_TODAY))
                .orderBy(NumberExpression.random().asc())
                .limit(1)
                .fetch();

        return random.get(0);
    }



}
