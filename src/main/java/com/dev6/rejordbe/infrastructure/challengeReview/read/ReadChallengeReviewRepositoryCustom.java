package com.dev6.rejordbe.infrastructure.challengeReview.read;

import com.dev6.rejordbe.domain.challengeReview.dto.ChallengeReviewResult;
import com.dev6.rejordbe.domain.user.dto.UserInfoForMyPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

/**
 * ReadChallengeReviewRepositoryCustom
 */
public interface ReadChallengeReviewRepositoryCustom {

    /**
     * 조건에 맞는 모든 일반 챌린지 게시글을 반환 (페이지네이션)
     *
     * @param offsetTime {@code LocalDateTime} 최신 챌린지 게시글 판단기준 시각
     * @param pageable  {@code Pageable}
     * @return {@code Page<ChallengeReviewResult>}
     */
    Page<ChallengeReviewResult> searchAll(final LocalDateTime offsetTime, final Pageable pageable);

    /**
     * uid가 동일한 모든 챌린지 리뷰 게시글을 반환 (페이지네이션)
     *
     * @param uid {@code String} 유저 UID
     * @param pageable {@code Pageable}
     * @return {@code Page<ChallengeReviewResult>}
     */
    Page<ChallengeReviewResult> searchChallengeReviewByUid(final String uid, final Pageable pageable);

    /**
     * uid가 일치하는 마이페이지 관련 정보 반환
     *
     * @param uid {@code String} 유저 UID
     * @return {@code Optional<UserInfoForMyPage>}
     */
    UserInfoForMyPage searchChallengeInfoByUid(final String uid);

}
