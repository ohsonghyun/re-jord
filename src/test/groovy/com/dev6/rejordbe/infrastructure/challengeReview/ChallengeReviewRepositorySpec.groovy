package com.dev6.rejordbe.infrastructure.challengeReview

import com.dev6.rejordbe.TestConfig
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
 * ChallengeReviewRepositorySpec
 */
@DataJpaTest
@EnableJpaAuditing
@Import(TestConfig.class)
class ChallengeReviewRepositorySpec extends Specification {

    @PersistenceContext
    EntityManager entityManager

    @Autowired
    SignUpRepository signUpRepository
    @Autowired
    ChallengeReviewRepository challengeReviewRepository

    def "챌린지 후기를 추가할 수 있다"() {
        given:
        // 현재 시간
        def now = LocalDateTime.now()
        // 유저 생성
        def user = Users.builder()
                .uid('uid')
                .build()
        signUpRepository.save(user)

        def newChallengeReview = ChallengeReview.builder()
                .challengeReviewId('challengeReviewId')
                .contents('contents')
                .challengeReviewType(ChallengeReviewType.FREE)
                .user(user)
                .build()

        when:
        challengeReviewRepository.save(newChallengeReview)

        entityManager.flush()
        entityManager.clear()

        then:
        def challengeReviewOptional = challengeReviewRepository.findById(newChallengeReview.getChallengeReviewId())
        challengeReviewOptional.isPresent()
        def aChallengeReview = challengeReviewOptional.orElseThrow()
        aChallengeReview.getChallengeReviewId() == newChallengeReview.getChallengeReviewId()
        aChallengeReview.getContents() == newChallengeReview.getContents()
        aChallengeReview.getChallengeReviewType() == newChallengeReview.getChallengeReviewType()
        aChallengeReview.getCreatedDate().isAfter(now)
        aChallengeReview.getModifiedDate().isAfter(now)
    }

}
