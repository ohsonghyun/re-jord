package com.dev6.rejordbe.application.badge.add;

import com.dev6.rejordbe.application.id.IdGenerator;
import com.dev6.rejordbe.domain.badge.BadgeAcquirementType;
import com.dev6.rejordbe.domain.badge.Badge;
import com.dev6.rejordbe.domain.badge.BadgeCode;
import com.dev6.rejordbe.domain.badge.dto.BadgeResult;
import com.dev6.rejordbe.domain.challengeReview.ChallengeReview;
import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.exception.ParentIdNotFoundException;
import com.dev6.rejordbe.infrastructure.badge.add.AddBadgeRepository;
import com.dev6.rejordbe.infrastructure.challengeReview.add.WriteChallengeReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AddBadgeServiceImpl
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@lombok.RequiredArgsConstructor
public class AddBadgeServiceImpl implements AddBadgeService {

    private final AddBadgeRepository addBadgeRepository;
    private final IdGenerator idGenerator;
    private final WriteChallengeReviewRepository writeChallengeReviewRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public BadgeResult addBadge(@NonNull String parentId) {
        ChallengeReview challengeReview = writeChallengeReviewRepository.findById(parentId).orElseThrow(()-> new ParentIdNotFoundException(ExceptionCode.CHALLENGE_REVIEW_NOT_FOUND.name()));

        Badge badgeResult = addBadgeRepository.save(
                Badge.builder()
                        .badgeId(idGenerator.generate("BG"))
                        .badgeCode(BadgeCode.CHALLENGE_POST)
                        .parent(challengeReview)
                        .badgeAcquirementType(BadgeAcquirementType.CHALLENGE_REVIEW )
                        .build()
        );

        return BadgeResult.builder()
                .badgeId(badgeResult.getBadgeId())
                .badgeCode(badgeResult.getBadgeCode())
                .parentId(badgeResult.getParent().getId())
                .badgeAcquirementType(badgeResult.getBadgeAcquirementType())
                .build();
    }
}
