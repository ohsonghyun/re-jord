package com.dev6.rejordbe.application.challengeReview.read;

import com.dev6.rejordbe.domain.challengeReview.ChallengeReview;
import com.dev6.rejordbe.domain.challengeReview.dto.ChallengeReviewResult;
import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.exception.ChallengeReviewNotFoundException;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.infrastructure.challengeReview.read.ReadChallengeReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * ReadChallengeReviewService
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@lombok.RequiredArgsConstructor
public class ReadChallengeReviewServiceImpl implements ReadChallengeReviewService {

    private final ReadChallengeReviewRepository readChallengeReviewRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<ChallengeReviewResult> allChallengeReviews(
            @NonNull final LocalDateTime offsetTime,
            @NonNull final Pageable pageable
    ) {
        if (Objects.isNull(offsetTime)) {
            log.warn("ReadChallengeReviewServiceImpl.allChallengeReviews: ILLEGAL_DATE_TIME: {}", offsetTime);
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_DATE_TIME);
        }
        return readChallengeReviewRepository.searchAll(offsetTime, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<ChallengeReviewResult> challengeReviewsWrittenByUid(
            @NonNull final String uid,
            @NonNull final Pageable pageable
    ) {
        if (StringUtils.isBlank(uid)) {
            log.warn("ReadChallengeReviewServiceImpl.ChallengeReviewWrittenByUid: ILLEGAL_UID: {}", uid);
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_UID);
        }
        return readChallengeReviewRepository.searchChallengeReviewByUid(uid, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public ChallengeReviewResult updateChallengeReviewInfo(ChallengeReview newChallengeReview) {
        if (!org.springframework.util.StringUtils.hasText(newChallengeReview.getContents())) {
            log.info("ReadChallengeReviewServiceImpl.updateChallengeReviewInfo: ILLEGAL_CONTENTS: {}", newChallengeReview.getContents());
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_CONTENTS);
        }

        ChallengeReview targetChallengeReview = readChallengeReviewRepository.findById(newChallengeReview.getChallengeReviewId())
                .orElseThrow(() -> {
                    log.info("ReadPostService.updatePostInfo: POST_NOT_FOUND: {}", newChallengeReview.getChallengeReviewId());
                    return new ChallengeReviewNotFoundException(ExceptionCode.CHALLENGE_NOT_FOUND);
                });

        targetChallengeReview.update(ChallengeReview.builder().contents(newChallengeReview.getContents()).build());
        return ChallengeReviewResult.builder()
                .challengeReviewId(targetChallengeReview.getChallengeReviewId())
                .title(targetChallengeReview.getChallenge().getTitle())
                .contents(targetChallengeReview.getContents())
                .challengeReviewType(targetChallengeReview.getChallengeReviewType())
                .uid(targetChallengeReview.getUser().getUid())
                .nickname(targetChallengeReview.getUser().getNickname())
                .createdDate(targetChallengeReview.getCreatedDate())
                .build();
    }
}
