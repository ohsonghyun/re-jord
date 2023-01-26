package com.dev6.rejordbe.application.footprint.add;

import com.dev6.rejordbe.application.id.IdGenerator;
import com.dev6.rejordbe.domain.challengeReview.ChallengeReview;
import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.domain.footprint.AcquirementType;
import com.dev6.rejordbe.domain.footprint.Footprint;
import com.dev6.rejordbe.domain.footprint.dto.FootprintResult;
import com.dev6.rejordbe.exception.ChallengeReviewNotFoundException;
import com.dev6.rejordbe.infrastructure.challengeReview.add.WriteChallengeReviewRepository;
import com.dev6.rejordbe.infrastructure.footprint.add.AddFootprintRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AddFootprintServiceImpl
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@lombok.RequiredArgsConstructor
public class AddFootprintServiceImpl implements AddFootprintService {

    private final AddFootprintRepository addFootprintRepository;
    private final IdGenerator idGenerator;
    private final WriteChallengeReviewRepository writeChallengeReviewRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public FootprintResult addFootprint(@NonNull String parentId) {
        ChallengeReview challengeReview = writeChallengeReviewRepository.findById(parentId).orElseThrow(()-> new ChallengeReviewNotFoundException(ExceptionCode.CHALLENGE_REVIEW_NOT_FOUND.name()));

        Footprint footprintResult = addFootprintRepository.save(
                Footprint.builder()
                        .footprintId(idGenerator.generate("FP"))
                        .footprintNum(1)
                        .challengeReview(challengeReview)
                        .acquirementType(AcquirementType.BASIC)
                        .build()
        );

        return FootprintResult.builder()
                .footprintId(footprintResult.getFootprintId())
                .footprintNum(footprintResult.getFootprintNum())
                .parentId(footprintResult.getChallengeReview().getChallengeReviewId())
                .acquirementType(footprintResult.getAcquirementType())
                .build();
    }
}
