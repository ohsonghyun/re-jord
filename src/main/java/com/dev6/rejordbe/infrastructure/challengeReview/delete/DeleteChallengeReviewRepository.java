package com.dev6.rejordbe.infrastructure.challengeReview.delete;

import com.dev6.rejordbe.domain.challengeReview.ChallengeReview;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DeleteChallengeReviewRepository
 */
public interface DeleteChallengeReviewRepository  extends JpaRepository<ChallengeReview, String> {
}
