package com.dev6.rejordbe.application.badge.add

import com.dev6.rejordbe.application.id.IdGenerator
import com.dev6.rejordbe.domain.badge.Badge
import com.dev6.rejordbe.domain.badge.BadgeCode
import com.dev6.rejordbe.domain.challengeReview.ChallengeReview
import com.dev6.rejordbe.domain.badge.AcquirementType
import com.dev6.rejordbe.exception.ChallengeReviewNotFoundException
import com.dev6.rejordbe.infrastructure.badge.add.AddBadgeRepository
import com.dev6.rejordbe.infrastructure.challengeReview.add.WriteChallengeReviewRepository
import spock.lang.Specification

/**
 * AddBadgeServiceImplSpec
 */
class AddBadgeServiceImplSpec extends Specification {

    AddBadgeService addBadgeService
    AddBadgeRepository addBadgeRepository
    WriteChallengeReviewRepository writeChallengeReviewRepository
    IdGenerator idGenerator

    def setup() {
        addBadgeRepository = Mock(AddBadgeRepository.class)
        writeChallengeReviewRepository = Mock(WriteChallengeReviewRepository.class)
        idGenerator = Mock(IdGenerator.class)
        addBadgeService = new AddBadgeServiceImpl(addBadgeRepository, idGenerator, writeChallengeReviewRepository)
    }

    def "에러가 없는 경우 배지를 추가할 수 있다"() {
        def anChallengeReview = ChallengeReview.builder()
                .challengeReviewId(challengeReviewId)
                .build()

        writeChallengeReviewRepository.findById(challengeReviewId) >> Optional.of(anChallengeReview)

        addBadgeRepository.save(_ as Badge) >> Badge.builder()
                    .badgeId(badgeId)
                    .badgeCode(badgeCode)
                    .challengeReview(anChallengeReview)
                    .acquirementType(acquirementType)
                    .build()

        when:
        def saveResult = addBadgeService.addBadge(challengeReviewId)

        then:
        saveResult.getBadgeId() == badgeId
        saveResult.getBadgeCode() == badgeCode
        saveResult.getParentId() == challengeReviewId
        saveResult.getAcquirementType() == acquirementType

        where:
        badgeId   | badgeCode            | challengeReviewId   | acquirementType
        'badgeId' | BadgeCode.FIRST_WEEK | 'challengeReviewId' | AcquirementType.BASIC
    }

    def "존재하지 않는 챌린지 리뷰이면 에러"() {
        def anChallengeReview = ChallengeReview.builder()
                .challengeReviewId(challengeReviewId)
                .build()

        writeChallengeReviewRepository.findById(challengeReviewId) >> Optional.empty()

        addBadgeRepository.save(_ as Badge) >> Badge.builder()
                .badgeId(badgeId)
                .badgeCode(badgeCode)
                .challengeReview(anChallengeReview)
                .acquirementType(acquirementType)
                .build()

        when:
        addBadgeService.addBadge(challengeReviewId)

        then:
        thrown(ChallengeReviewNotFoundException)

        where:
        badgeId   | badgeCode            | challengeReviewId   | acquirementType
        'badgeId' | BadgeCode.FIRST_WEEK | 'challengeReviewId' | AcquirementType.BASIC
    }
}
