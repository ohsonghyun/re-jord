package com.dev6.rejordbe.application.challengeReview.read;

import com.dev6.rejordbe.domain.challengeReview.dto.ChallengeReviewResult;
import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.infrastructure.challengeReview.read.ReadChallengeReviewRepository;
import lombok.extern.slf4j.Slf4j;
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
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_DATE_TIME.name());
        }
        return readChallengeReviewRepository.searchAll(offsetTime, pageable);
    }
}