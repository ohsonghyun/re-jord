package com.dev6.rejordbe.application.challenge.read;

import com.dev6.rejordbe.domain.challenge.dto.ChallengeResult;

import java.util.Optional;

/**
 * ChallengeInfoService
 */
public interface ChallengeInfoService {

    /**
     * flag 가 Today 인 챌린지 정보 획득
     * Today 인 flag가 없을 시 DEFAULT로 출력
     *
     * @return {@code Optional<Challenge>}
     */
    Optional<ChallengeResult> findTodayChallengeInFlag();
}
