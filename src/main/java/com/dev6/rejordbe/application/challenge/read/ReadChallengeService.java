package com.dev6.rejordbe.application.challenge.read;

import com.dev6.rejordbe.domain.challenge.dto.ChallengeResult;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * ReadChallengeService
 */
public interface ReadChallengeService {

    /**
     * flag 가 true 인 챌린지 정보 획득
     *
     * @param flag {@code Boolean}
     * @return {@code Optional<Challenge>}
     */
    Optional<ChallengeResult> findChallengeByFlag(@NonNull final Boolean flag);
}
