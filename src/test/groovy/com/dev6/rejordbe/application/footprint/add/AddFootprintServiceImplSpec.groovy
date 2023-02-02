package com.dev6.rejordbe.application.footprint.add

import com.dev6.rejordbe.application.badge.add.AddBadgeService
import com.dev6.rejordbe.application.id.IdGenerator
import com.dev6.rejordbe.domain.challengeReview.ChallengeReview
import com.dev6.rejordbe.domain.footprint.AcquirementType
import com.dev6.rejordbe.domain.footprint.Footprint
import com.dev6.rejordbe.exception.ParentIdNotFoundException
import com.dev6.rejordbe.infrastructure.challengeReview.add.WriteChallengeReviewRepository
import com.dev6.rejordbe.infrastructure.footprint.add.AddFootprintRepository
import spock.lang.Specification

/**
 * AddFootprintServiceImplSpec
 */
class AddFootprintServiceImplSpec extends Specification {

    AddFootprintService addFootprintService
    AddFootprintRepository addFootprintRepository
    WriteChallengeReviewRepository writeChallengeReviewRepository
    IdGenerator idGenerator

    def setup() {
        addFootprintRepository = Mock(AddFootprintRepository.class)
        writeChallengeReviewRepository = Mock(WriteChallengeReviewRepository.class)
        idGenerator = Mock(IdGenerator.class)
        addFootprintService = new AddFootprintServiceImpl(addFootprintRepository, idGenerator,  writeChallengeReviewRepository)
    }

    def "에러가 없는 경우 발자국을 추가할 수 있다"() {
        def anChallengeReview = ChallengeReview.builder()
                    .challengeReviewId(challengeReviewId)
                    .build()

        writeChallengeReviewRepository.findById(challengeReviewId) >> Optional.of(anChallengeReview)

        addFootprintRepository.save(_ as Footprint) >> Footprint.builder()
                    .footprintId(footprintId)
                    .footprintAmount(footprintAmount)
                    .challengeReview(anChallengeReview)
                    .acquirementType(acquirementType)
                    .build()

        when:
        def saveResult = addFootprintService.addFootprint(challengeReviewId)

        then:
        saveResult.getFootprintId() == footprintId
        saveResult.getFootprintAmount() == footprintAmount
        saveResult.getParentId() == challengeReviewId
        saveResult.getAcquirementType() == acquirementType

        where:
        footprintId   | footprintAmount | challengeReviewId   | acquirementType
        'footprintId' | 1            | 'challengeReviewId' | AcquirementType.CHALLENGE_REVIEW
    }

    def "존재하지 않는 챌린지 리뷰이면 에러"() {
        def anChallengeReview = ChallengeReview.builder()
                .challengeReviewId(challengeReviewId)
                .build()

        writeChallengeReviewRepository.findById(challengeReviewId) >> Optional.empty()

        addFootprintRepository.save(_ as Footprint) >> Footprint.builder()
                .footprintId(footprintId)
                .footprintAmount(footprintAmount)
                .challengeReview(anChallengeReview)
                .acquirementType(acquirementType)
                .build()

        when:
        addFootprintService.addFootprint(challengeReviewId)

        then:
        thrown(ParentIdNotFoundException)

        where:
        footprintId   | footprintAmount | challengeReviewId   | acquirementType
        'footprintId' | 1            | 'challengeReviewId' | AcquirementType.CHALLENGE_REVIEW
    }
}
