package com.dev6.rejordbe.application.challengeReview.delete;

import com.dev6.rejordbe.domain.challengeReview.ChallengeReview;
import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.exception.ChallengeReviewNotFoundException;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.exception.UnauthorizedUserException;
import com.dev6.rejordbe.infrastructure.challengeReview.delete.DeleteChallengeReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * DeleteChallengeReviewServiceImpl
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@lombok.RequiredArgsConstructor
public class DeleteChallengeReviewServiceImpl implements DeleteChallengeReviewService {

    private final DeleteChallengeReviewRepository deleteChallengeReviewRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public String deleteChallengeReview(
            @NonNull final String challengeReviewId,
            @NonNull final String uid
    ) {
        if(StringUtils.isBlank(challengeReviewId)) {
            log.warn("DeleteChallengeReviewServiceImpl.deleteChallengeReview: {}: challengeReviewId is blank", ExceptionCode.ILLEGAL_PARAM);
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_PARAM);
        }

        ChallengeReview targetChallengeReview = deleteChallengeReviewRepository.findById(challengeReviewId).orElseThrow(() -> {
            log.warn("DeleteChallengeReviewServiceImpl.deleteChallengeReview: CHALLENGE_NOT_FOUND: challengeReviewId: {}", challengeReviewId);
            return new ChallengeReviewNotFoundException(ExceptionCode.CHALLENGE_NOT_FOUND);
        });

        if(ObjectUtils.notEqual(uid, targetChallengeReview.getUser().getUid())) {
            log.warn("DeleteChallengeReviewServiceImpl.deleteChallengeReview: UNAUTHORIZED_USER: uid: {}", uid);
            throw new UnauthorizedUserException(ExceptionCode.UNAUTHORIZED_USER);
        }

        deleteChallengeReviewRepository.deleteById(challengeReviewId);
        return challengeReviewId;
    }
}
