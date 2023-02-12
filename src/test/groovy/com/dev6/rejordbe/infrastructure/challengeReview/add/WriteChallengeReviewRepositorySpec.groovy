package com.dev6.rejordbe.infrastructure.challengeReview.add

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
        def user = Users.builder()
                .uid('uid')
                .build()
        signUpRepository.save(user)

        def newChallengeReview = ChallengeReview.builder()
                    .id('challengeReviewId')
                    .contents('contents')
                    .challengeReviewType(ChallengeReviewType.FEELING)
                    .user(user)
                    .build()

        when:
        writeChallengeReviewRepository.save(newChallengeReview)

        entityManager.flush()
        entityManager.clear()

        then:
        def challengeReviewOptional = writeChallengeReviewRepository.findById(newChallengeReview.getId())
        challengeReviewOptional.isPresent()
        def aChallengeReview = challengeReviewOptional.orElseThrow()
        aChallengeReview.getId() == newChallengeReview.getId()
        aChallengeReview.getContents() == newChallengeReview.getContents()
        aChallengeReview.getChallengeReviewType() == newChallengeReview.getChallengeReviewType()
        aChallengeReview.getCreatedDate().isAfter(now)
        aChallengeReview.getModifiedDate().isAfter(now)

    }
}
