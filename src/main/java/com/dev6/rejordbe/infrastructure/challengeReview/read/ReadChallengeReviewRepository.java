package com.dev6.rejordbe.infrastructure.challengeReview.read;

import com.dev6.rejordbe.domain.challengeReview.ChallengeReview;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ReadChallengeReviewRepository
 */
public interface ReadChallengeReviewRepository extends JpaRepository<ChallengeReview, String>, ReadChallengeReviewRepositoryCustom {
}
