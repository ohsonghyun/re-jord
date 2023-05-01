package com.dev6.rejordbe.infrastructure.challengeReview.add

import com.dev6.rejordbe.TestConfig
import com.dev6.rejordbe.domain.badge.BadgeCode
import com.dev6.rejordbe.domain.challenge.Challenge
import com.dev6.rejordbe.domain.challenge.ChallengeFlagType
import com.dev6.rejordbe.domain.challengeReview.ChallengeReview
import com.dev6.rejordbe.domain.challengeReview.ChallengeReviewType
import com.dev6.rejordbe.domain.user.Users
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
 * WriteChallengeReviewRepositorySpec
 */
@DataJpaTest
@EnableJpaAuditing
@Import(TestConfig.class)
class WriteChallengeReviewRepositorySpec extends Specification {

    @PersistenceContext
    EntityManager entityManager

    @Autowired
    SignUpRepository signUpRepository
    @Autowired
    WriteChallengeReviewRepository writeChallengeReviewRepository

    def "챌린지 리뷰를 추가할 수 있다"() {
        given:
        // 현재 시간
        def now = LocalDateTime.now()
        // 유저 생성
        def user = signUpRepository.save(Users.builder().uid('uid').build())
        // 챌린지 생성
        def challenge = new Challenge("CH0", "찬 물로 세탁하기", "오늘은 찬물로 세탁기를 돌리고 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.WATER_FAIRY, "imgFront0.png", "imgBack0.png", "#2894DE", ChallengeFlagType.TODAY)
        entityManager.persist(challenge)

        when:
        writeChallengeReviewRepository.save(ChallengeReview.builder()
                .challengeReviewId(challengeReviewId)
                .contents(contents)
                .challengeReviewType(challengeReviewType)
                .challenge(challenge)
                .user(user)
                .build())

        entityManager.flush()
        entityManager.clear()

        then:
        def challengeReviewOptional = writeChallengeReviewRepository.findById(challengeReviewId)
        challengeReviewOptional.isPresent()
        def aChallengeReview = challengeReviewOptional.orElseThrow()
        aChallengeReview.getChallengeReviewId() == challengeReviewId
        aChallengeReview.getContents() == contents
        aChallengeReview.getChallengeReviewType() == challengeReviewType
        aChallengeReview.getChallenge().getChallengeId() == 'CH0'
        aChallengeReview.getCreatedDate().isAfter(now)
        aChallengeReview.getModifiedDate().isAfter(now)

        where:
        challengeReviewId   | contents   | challengeReviewType
        'challengeReviewId' | 'contents' | ChallengeReviewType.FEELING
    }
}
