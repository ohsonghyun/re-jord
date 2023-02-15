package com.dev6.rejordbe.infrastructure.challenge.read;

import com.dev6.rejordbe.domain.challenge.Challenge;

import java.util.Optional;

/**
 * ReadChallengeRepositoryCustom
 */
public interface ReadChallengeRepositoryCustom {

    Optional<Challenge> randomChallenge();
}
