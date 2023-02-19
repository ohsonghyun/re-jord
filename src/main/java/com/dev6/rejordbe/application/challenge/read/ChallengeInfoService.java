package com.dev6.rejordbe.application.challenge.read;

import com.dev6.rejordbe.domain.challenge.dto.ChallengeResult;

import java.util.Optional;

/**
 * ChallengeInfoService
 */
public interface ChallengeInfoService {

    /**
     * flag 가 true 인 챌린지 정보 획득
     *
     * @return {@code Optional<Challenge>}
     */
    Optional<ChallengeResult> findChallengeByFlag();
}
