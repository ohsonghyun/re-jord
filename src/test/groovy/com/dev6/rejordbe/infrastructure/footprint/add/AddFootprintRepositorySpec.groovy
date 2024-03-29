package com.dev6.rejordbe.infrastructure.footprint.add

import com.dev6.rejordbe.TestConfig
import com.dev6.rejordbe.domain.challengeReview.ChallengeReview
import com.dev6.rejordbe.domain.footprint.FootprintAcquirementType
import com.dev6.rejordbe.domain.footprint.Footprint
import com.dev6.rejordbe.infrastructure.challengeReview.read.ReadChallengeReviewRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import java.time.LocalDateTime

/**
 * AddFootprintRepositorySpec
 */
@DataJpaTest
@EnableJpaAuditing
@Import(TestConfig.class)
class AddFootprintRepositorySpec extends Specification {

    @PersistenceContext
    EntityManager entityManager

    @Autowired
    ReadChallengeReviewRepository challengeReviewRepository
    @Autowired
    AddFootprintRepository addFootprintRepository

    def "발자국을 추가할 수 있다"() {
        given:
        // 현재 시간
        def now = LocalDateTime.now()
        // 챌린지 리뷰 생성
        def challengeReview = ChallengeReview.builder()
                        .challengeReviewId('challengeReviewId')
                        .build()
        challengeReviewRepository.save(challengeReview)

        def newFootprint = Footprint.builder()
                    .footprintId('footprintId')
                    .footprintAmount(1)
                    .parentId(challengeReview.getChallengeReviewId())
                    .footprintAcquirementType(FootprintAcquirementType.CHALLENGE_REVIEW)
                    .build()

        when:
        addFootprintRepository.save(newFootprint)

        entityManager.flush()
        entityManager.clear()

        then:
        def footprintOptional = addFootprintRepository.findById(newFootprint.getFootprintId())
        footprintOptional.isPresent()
        def aFootprint = footprintOptional.orElseThrow()
        aFootprint.getFootprintId() == newFootprint.getFootprintId()
        aFootprint.getFootprintAmount() == newFootprint.getFootprintAmount()
        aFootprint.getFootprintAcquirementType() == newFootprint.getFootprintAcquirementType()
        aFootprint.getCreatedDate().isAfter(now)
        aFootprint.getModifiedDate().isAfter(now)

    }

}
