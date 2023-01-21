package com.dev6.rejordbe.infrastructure.challengeReview;

import com.dev6.rejordbe.domain.challengeReview.ChallengeReview;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ChallengeReviewRepository
 */
public interface ChallengeReviewRepository extends JpaRepository<ChallengeReview, String> {
}
