package com.dev6.rejordbe.infrastructure.challenge.read;

import com.dev6.rejordbe.domain.challenge.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * ReadChallengeRepository
 */
public interface ReadChallengeRepository extends JpaRepository<Challenge, String>, ReadChallengeRepositoryCustom {

    /**
     * flag로 챌린지 찾기
     *
     * @param flag {@code Boolean} 찾고자 하는 챌린지의 flag
     * @return {@code Optional<Challenge>}
     */
    Optional<Challenge> findChallengeByFlag(final Boolean flag);
}
