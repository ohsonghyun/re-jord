package com.dev6.rejordbe.application.challengeReview.delete;

import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.exception.ChallengeNotFoundException;
import com.dev6.rejordbe.exception.UnauthorizedUserException;

/**
 * DeleteChallengeReviewService
 */
public interface DeleteChallengeReviewService {

    /**
     * challengeReviewId가 일치하는 게시글 삭제
     *
     * @param challengeReviewId {@code String} 삭제할 챌린지 리뷰 게시글 id
     * @param uid{@code String} 삭제할 게시글 작성한 유저 uid
     * @return {@code String} 삭제할 챌린지 리뷰 게시글 id
     * @throws ChallengeNotFoundException {@code challengeReviewId}가 존재하지 않는 게시글일 경우
     * @throws IllegalParameterException {@code challengeReviewId}가 정책에 어긋나는 경우
     * @throws UnauthorizedUserException {@code uid} 권한이 없는 유저가 접근한 경우
     */
    String deleteChallengeReview(final String challengeReviewId, final String uid);
}
