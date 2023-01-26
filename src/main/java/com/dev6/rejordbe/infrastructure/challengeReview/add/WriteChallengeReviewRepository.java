package com.dev6.rejordbe.infrastructure.challengeReview.add;

import com.dev6.rejordbe.domain.challengeReview.ChallengeReview;
import com.dev6.rejordbe.infrastructure.challengeReview.read.ReadChallengeReviewRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * AddChallengeReviewRepository
 */
public interface WriteChallengeReviewRepository extends JpaRepository<ChallengeReview, String>, ReadChallengeReviewRepositoryCustom {
}
