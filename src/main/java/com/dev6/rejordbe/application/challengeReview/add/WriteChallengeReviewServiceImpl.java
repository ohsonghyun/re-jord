package com.dev6.rejordbe.application.challengeReview.add;


import com.dev6.rejordbe.application.badge.add.AddBadgeService;
import com.dev6.rejordbe.application.footprint.add.AddFootprintService;
import com.dev6.rejordbe.application.id.IdGenerator;
import com.dev6.rejordbe.domain.challengeReview.ChallengeReview;
import com.dev6.rejordbe.domain.challengeReview.ChallengeReviewType;
import com.dev6.rejordbe.domain.challengeReview.dto.ChallengeReviewResult;
import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.domain.user.Users;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.exception.UserNotFoundException;
import com.dev6.rejordbe.infrastructure.challengeReview.add.WriteChallengeReviewRepository;
import com.dev6.rejordbe.infrastructure.user.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * ChallengeReviewServiceImpl
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@lombok.RequiredArgsConstructor
public class WriteChallengeReviewServiceImpl implements WriteChallengeReviewService {

    private final WriteChallengeReviewRepository writeChallengeReviewRepository;
    private final IdGenerator idGenerator;
    private final UserInfoRepository userInfoRepository;
    private final AddFootprintService addFootprintService;
    private final AddBadgeService addBadgeService;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public ChallengeReviewResult writeChallengeReview(@NonNull ChallengeReview newChallengeReview, @NonNull String uid) {
        Users user = userInfoRepository.findById(uid).orElseThrow(() -> new UserNotFoundException(ExceptionCode.USER_NOT_FOUND.name()));

        if (!StringUtils.hasText(newChallengeReview.getContents())) {
            log.info("ChallengeReviewServiceImpl.writeChallengeReview: ILLEGAL_CONTENTS: {}", newChallengeReview.getContents());
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_CONTENTS.name());
        }

        ChallengeReview challengeReviewResult = writeChallengeReviewRepository.save(
                ChallengeReview.builder()
                        .challengeReviewId(idGenerator.generate("CR"))
                        .contents(newChallengeReview.getContents())
                        .challengeReviewType(
                                checkChallengeReviewType(newChallengeReview.getChallengeReviewType())
                        )
                        .user(user)
                        .build()
        );

        if(!challengeReviewResult.getChallengeReviewId().isEmpty()){
            addBadgeService.addBadge(challengeReviewResult.getChallengeReviewId());
            addFootprintService.addFootprint(challengeReviewResult.getChallengeReviewId());
        }

        return ChallengeReviewResult.builder()
                .challengeReviewId(challengeReviewResult.getChallengeReviewId())
                .contents(challengeReviewResult.getContents())
                .challengeReviewType(challengeReviewResult.getChallengeReviewType())
                .uid(challengeReviewResult.getUser().getUid())
                .build();
    }

    public ChallengeReviewType checkChallengeReviewType(ChallengeReviewType challengeReviewType) {

        if(challengeReviewType == null) {
            challengeReviewType = ChallengeReviewType.FEELING;
        }

        return challengeReviewType;
    }
}
