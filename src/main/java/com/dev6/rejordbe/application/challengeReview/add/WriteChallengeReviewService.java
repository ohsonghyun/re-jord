package com.dev6.rejordbe.application.challengeReview.add;

import com.dev6.rejordbe.domain.challengeReview.ChallengeReview;
import com.dev6.rejordbe.domain.challengeReview.dto.ChallengeReviewResult;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.exception.UserNotFoundException;

/**
 * ChallengeReviewService
 */
public interface WriteChallengeReviewService {

    /**
     * 새로운 챌린지 리뷰 추가
     *
     * @param challengeReview {@code ChallengeReview} 새로운 챌린지 지류 정보
     * @param uid {@code String} 새로운 챌린지 리뷰 작성자 uid
     * @return {@code ChallengeReviewResult} 추가한 챌린지 리뷰 정보
     * @throws IllegalParameterException {@code contents} 가 정책에 어긋나는 경우
     * @throws UserNotFoundException {@code uid} 가 존재하지 않는 유저일 경우
     */
    ChallengeReviewResult writeChallengeReview(final ChallengeReview challengeReview, final String uid);
}
