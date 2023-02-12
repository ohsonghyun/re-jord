package com.dev6.rejordbe.infrastructure.badge.add

import com.dev6.rejordbe.TestConfig
import com.dev6.rejordbe.domain.badge.AcquirementType
import com.dev6.rejordbe.domain.badge.Badge
import com.dev6.rejordbe.domain.badge.BadgeCode
import com.dev6.rejordbe.domain.challengeReview.ChallengeReview
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
 * AddBadgeRepositorySpec
 */
@DataJpaTest
@EnableJpaAuditing
@Import(TestConfig.class)
class AddBadgeRepositorySpec extends Specification {

    @PersistenceContext
    EntityManager entityManager

    @Autowired
    ReadChallengeReviewRepository challengeReviewRepository
    @Autowired
    AddBadgeRepository addBadgeRepository

    def "배지를 추가할 수 있다"() {
        given:
        // 현재 시간
        def now = LocalDateTime.now()
        // 챌린지 리뷰 생성
        def challengeReview = ChallengeReview.builder()
                .id('challengeReviewId')
                .build()
        challengeReviewRepository.save(challengeReview)

        def newBadge = Badge.builder()
                .badgeId('badgeId')
                .badgeCode(BadgeCode.FIRST_WEEK)
                .parent(challengeReview)
                .acquirementType(AcquirementType.CHALLENGE_REVIEW)
                .build()

        when:
        addBadgeRepository.save(newBadge)

        entityManager.flush()
        entityManager.clear()

        then:
        def badgeOptional = addBadgeRepository.findById(newBadge.getBadgeId())
        badgeOptional.isPresent()
        def aBadge = badgeOptional.orElseThrow()
        aBadge.getBadgeId() == newBadge.getBadgeId()
        aBadge.getBadgeCode() == newBadge.getBadgeCode()
        aBadge.getAcquirementType() == newBadge.getAcquirementType()
        aBadge.getCreatedDate().isAfter(now)
        aBadge.getModifiedDate().isAfter(now)
    }


}
