package com.dev6.rejordbe.application.challengeReview.add;

import com.dev6.rejordbe.application.id.IdGenerator;
import com.dev6.rejordbe.domain.badge.Badge;
import com.dev6.rejordbe.domain.badge.BadgeAcquirementType;
import com.dev6.rejordbe.domain.challenge.Challenge;
import com.dev6.rejordbe.domain.challengeReview.ChallengeReview;
import com.dev6.rejordbe.domain.challengeReview.ChallengeReviewType;
import com.dev6.rejordbe.domain.challengeReview.dto.ChallengeReviewResult;
import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.domain.footprint.Footprint;
import com.dev6.rejordbe.domain.footprint.FootprintAcquirementType;
import com.dev6.rejordbe.domain.user.Users;
import com.dev6.rejordbe.exception.ChallengeNotFoundException;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.exception.UserNotFoundException;
import com.dev6.rejordbe.infrastructure.badge.add.AddBadgeRepository;
import com.dev6.rejordbe.infrastructure.challenge.read.ReadChallengeRepository;
import com.dev6.rejordbe.infrastructure.challengeReview.add.WriteChallengeReviewRepository;
import com.dev6.rejordbe.infrastructure.footprint.add.AddFootprintRepository;
import com.dev6.rejordbe.infrastructure.user.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
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
    private final ReadChallengeRepository readChallengeRepository;
    private final AddBadgeRepository addBadgeRepository;
    private final AddFootprintRepository addFootprintRepository;
    private final UserInfoRepository userInfoRepository;
    private final IdGenerator idGenerator;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public ChallengeReviewResult writeChallengeReview(
            final String challengeId,
            final String contents,
            final ChallengeReviewType challengeReviewType,
            final String uid
    ) {
        // null || empty check
        if (!StringUtils.hasText(contents)) {
            log.warn("ChallengeReviewServiceImpl.writeChallengeReview: {}: {}", ExceptionCode.ILLEGAL_CONTENTS, contents);
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_CONTENTS);
        }
        if (Objects.isNull(challengeReviewType)) {
            log.warn("ChallengeReviewServiceImpl.writeChallengeReview: {}: challengeReviewType", ExceptionCode.ILLEGAL_PARAM);
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_PARAM);
        }
        if (!StringUtils.hasText(challengeId)) {
            log.warn("ChallengeReviewServiceImpl.writeChallengeReview: {}: {}", ExceptionCode.ILLEGAL_PARAM, challengeId);
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_PARAM);
        }

        Users user = userInfoRepository.findById(uid).orElseThrow(() -> {
            log.error("ChallengeReviewServiceImpl.writeChallengeReview: {}: {}", ExceptionCode.USER_NOT_FOUND, uid);
            return new UserNotFoundException(ExceptionCode.USER_NOT_FOUND);
        });

        Challenge challenge = readChallengeRepository.findById(challengeId).orElseThrow(() -> {
            log.error("ChallengeReviewServiceImpl.writeChallengeReview: {}: {}", ExceptionCode.CHALLENGE_NOT_FOUND, challengeId);
            return new ChallengeNotFoundException(ExceptionCode.CHALLENGE_NOT_FOUND);
        });

        ChallengeReview challengeReviewResult = writeChallengeReviewRepository.save(
                ChallengeReview.builder()
                        .challengeReviewId(idGenerator.generate("CR"))
                        .contents(contents)
                        .challengeReviewType(challengeReviewType)
                        .challenge(challenge)
                        .user(user)
                        .build()
        );

        addBadgeRepository.save(Badge.builder()
                .badgeId(idGenerator.generate("BG"))
                .badgeCode(challenge.getBadgeCode())
                .parentId(challengeReviewResult.getChallengeReviewId())
                .badgeAcquirementType(BadgeAcquirementType.CHALLENGE_REVIEW)
                .build());

        addFootprintRepository.save(Footprint.builder()
                .footprintId(idGenerator.generate("FP"))
                .footprintAmount(challenge.getFootprintAmount())
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
