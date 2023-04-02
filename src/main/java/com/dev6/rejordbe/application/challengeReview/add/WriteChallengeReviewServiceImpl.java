package com.dev6.rejordbe.application.challengeReview.add;

import com.dev6.rejordbe.application.id.IdGenerator;
import com.dev6.rejordbe.domain.badge.Badge;
import com.dev6.rejordbe.domain.badge.BadgeAcquirementType;
import com.dev6.rejordbe.domain.challengeReview.ChallengeReview;
import com.dev6.rejordbe.domain.challengeReview.dto.ChallengeReviewResult;
import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.domain.footprint.Footprint;
import com.dev6.rejordbe.domain.footprint.FootprintAcquirementType;
import com.dev6.rejordbe.domain.user.Users;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.exception.UserNotFoundException;
import com.dev6.rejordbe.infrastructure.badge.add.AddBadgeRepository;
import com.dev6.rejordbe.infrastructure.challengeReview.add.WriteChallengeReviewRepository;
import com.dev6.rejordbe.infrastructure.footprint.add.AddFootprintRepository;
import com.dev6.rejordbe.infrastructure.user.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * ChallengeReviewServiceImpl
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@lombok.RequiredArgsConstructor
public class WriteChallengeReviewServiceImpl implements WriteChallengeReviewService {

    private final WriteChallengeReviewRepository writeChallengeReviewRepository;
    private final AddBadgeRepository addBadgeRepository;
    private final AddFootprintRepository addFootprintRepository;
    private final UserInfoRepository userInfoRepository;
    private final IdGenerator idGenerator;
    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public ChallengeReviewResult writeChallengeReview(@NonNull ChallengeReview newChallengeReview, @NonNull String uid) {
        Users user = userInfoRepository.findById(uid).orElseThrow(() -> new UserNotFoundException(ExceptionCode.USER_NOT_FOUND));

        if (!StringUtils.hasText(newChallengeReview.getContents())) {
            log.warn("ChallengeReviewServiceImpl.writeChallengeReview: ILLEGAL_CONTENTS: {}", newChallengeReview.getContents());
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_CONTENTS);
        }

        if (Objects.isNull(newChallengeReview.getChallengeReviewType())) {
            log.warn("ChallengeReviewServiceImpl.writeChallengeReview: ILLEGAL_PARAM: {}", newChallengeReview.getChallengeReviewType());
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_PARAM);
        }

        // TODO flowertaekk refactor
//        if (Objects.isNull(newChallengeReview.getBadgeCode())) {
//            log.warn("ChallengeReviewServiceImpl.writeChallengeReview: ILLEGAL_PARAM: {}", newChallengeReview.getBadgeCode());
//            throw new IllegalParameterException(ExceptionCode.ILLEGAL_PARAM);
//        }
//
//        if (Objects.isNull(newChallengeReview.getFootprintAmount())) {
//            log.warn("ChallengeReviewServiceImpl.writeChallengeReview: ILLEGAL_PARAM: {}", newChallengeReview.getFootprintAmount());
//            throw new IllegalParameterException(ExceptionCode.ILLEGAL_PARAM);
//        }

        ChallengeReview challengeReviewResult = writeChallengeReviewRepository.save(
                ChallengeReview.builder()
                        .challengeReviewId(idGenerator.generate("CR"))
                        .contents(newChallengeReview.getContents())
                        .challengeReviewType(newChallengeReview.getChallengeReviewType())
                        // TODO flowertaekk refactor
//                        .footprintAmount(newChallengeReview.getFootprintAmount())
//                        .badgeCode(newChallengeReview.getBadgeCode())
                        .user(user)
                        .build()
        );

        addBadgeRepository.save(Badge.builder()
                .badgeId(idGenerator.generate("BG"))
                // TODO flowertaekk refactor
//                .badgeCode(newChallengeReview.getBadgeCode())
                .parentId(challengeReviewResult.getChallengeReviewId())
                .badgeAcquirementType(BadgeAcquirementType.CHALLENGE_REVIEW)
                .build());

        addFootprintRepository.save(Footprint.builder()
                .footprintId(idGenerator.generate("FP"))
                // TODO flowertaekk refactor
//                .footprintAmount(newChallengeReview.getFootprintAmount())
                .parentId(challengeReviewResult.getChallengeReviewId())
                .footprintAcquirementType(FootprintAcquirementType.CHALLENGE_REVIEW)
                .build());

        return ChallengeReviewResult.builder()
                .challengeReviewId(challengeReviewResult.getChallengeReviewId())
                .contents(challengeReviewResult.getContents())
                .challengeReviewType(challengeReviewResult.getChallengeReviewType())
                .uid(challengeReviewResult.getUser().getUid())
                .build();
    }
}
