package com.dev6.rejordbe.infrastructure.challengeReview.delete

import com.dev6.rejordbe.TestConfig
import com.dev6.rejordbe.domain.badge.Badge
import com.dev6.rejordbe.domain.badge.BadgeCode
import com.dev6.rejordbe.domain.challenge.Challenge
import com.dev6.rejordbe.domain.challenge.ChallengeFlagType
import com.dev6.rejordbe.domain.challengeReview.ChallengeReview
import com.dev6.rejordbe.domain.challengeReview.ChallengeReviewType
import com.dev6.rejordbe.domain.footprint.Footprint
import com.dev6.rejordbe.domain.user.Users
import com.dev6.rejordbe.infrastructure.badge.add.AddBadgeRepository
import com.dev6.rejordbe.infrastructure.footprint.add.AddFootprintRepository
import com.dev6.rejordbe.infrastructure.user.SignUpRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import java.time.LocalDateTime

/**
 * DeleteChallengeReviewRepositorySpec
 */
@DataJpaTest
@EnableJpaAuditing
@Import(TestConfig.class)
class DeleteChallengeReviewRepositorySpec  extends Specification {

    @PersistenceContext
    EntityManager entityManager

    @Autowired
    SignUpRepository signUpRepository
    @Autowired
    AddFootprintRepository addFootprintRepository
    @Autowired
    AddBadgeRepository addBadgeRepository
    @Autowired
    DeleteChallengeReviewRepository deleteChallengeReviewRepository

    def "챌린지 리뷰를 삭제할 수 있다"() {
        // 현재 시간
        def now = LocalDateTime.now()
        // 유저 생성
        def user = signUpRepository.save(Users.builder().uid('uid').build())
        // 배지 생성
        def badge = addBadgeRepository.save(Badge.builder().badgeId('badgeId').build())
        // 발자국 생성
        def footprint = addFootprintRepository.save(Footprint.builder().footprintId('footprintId').build())
        // 챌린지 생성
        def challenge = new Challenge("CH0", "찬 물로 세탁하기", "오늘은 찬물로 세탁기를 돌리고 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.WATER_FAIRY, "imgFront0.png", "imgBack0.png", "#2894DE", ChallengeFlagType.TODAY)
        entityManager.persist(challenge)
        // 챌린지 리뷰 생성
        def challengeReview = deleteChallengeReviewRepository.save(ChallengeReview.builder()
                .challengeReviewId(challengeReviewId)
                .contents(contents)
                .challengeReviewType(challengeReviewType)
                .challenge(challenge)
                .user(user)
                .footprint(footprint)
                .badge(badge)
                .build())

        when:
        deleteChallengeReviewRepository.delete(challengeReview)

        entityManager.flush()
        entityManager.clear()

        then:
        def challengeReviewOptional = deleteChallengeReviewRepository.findById(challengeReview.getChallengeReviewId())
        def footprintOptional = addFootprintRepository.findById(footprint.getFootprintId())
        def badgeOptional = addBadgeRepository.findById(badge.getBadgeId())
        challengeReviewOptional == Optional.empty()
        footprintOptional == Optional.empty()
        badgeOptional == Optional.empty()

        where:
        challengeReviewId   | contents   | challengeReviewType
        'challengeReviewId' | 'contents' | ChallengeReviewType.FEELING
    }
}
