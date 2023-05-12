package com.dev6.rejordbe.application.challengeReview.read;

import com.dev6.rejordbe.domain.challengeReview.ChallengeReview;
import com.dev6.rejordbe.domain.challengeReview.dto.ChallengeReviewResult;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.exception.ChallengeNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

/**
 * ReadChallengeReviewService
 */
public interface ReadChallengeReviewService {

    /**
     * 모든 챌린지 후기 획득
     *
     * @param offsetTime {@code LocalDateTime} 최신 데이터 판단기준 시각
     * @param pageable {@code Pageable} 페이지 정보
     * @return {@code Page<ChallengeReviewResult>}
     * @throws IllegalParameterException {@code offsetTime} 이 존재하지 않은 경우
     */
    Page<ChallengeReviewResult> allChallengeReviews(final LocalDateTime offsetTime, final Pageable pageable);

    /**
     * UID 가 작성한 모든 챌린지 후기 획득
     *
     * @param uid {@code String} 유저 UID
     * @param pageable {@code Pageable} 페이지 정보
     * @return {@code Page<ChallengeReviewResult>}
     * @throws IllegalParameterException {@code uid} 이 존재하지 않은 경우
     */
    Page<ChallengeReviewResult> challengeReviewsWrittenByUid(final String uid, final Pageable pageable);

    /**
     * 챌린지 리뷰 게시글 내용 변경
     *
     * @param newChallengeReview {@code ChallengeReview} 변경할 챌린지 리뷰 게시글 정보
     * @return {@code ChallengeReviewResult} 변경된 챌린지 리뷰 게시글 정보
     * @throws ChallengeNotFoundException {@code challengeReviewId} 가 존재하지 않는 챌린지 리뷰 게시글일 경우
     * @throws IllegalParameterException {@code contents} 가 정책에 어긋나는 경우
     */
    ChallengeReviewResult updateChallengeReviewInfo(@NonNull final ChallengeReview newChallengeReview);
}
