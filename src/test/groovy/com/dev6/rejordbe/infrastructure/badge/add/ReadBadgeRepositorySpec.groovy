package com.dev6.rejordbe.infrastructure.badge.add

import com.dev6.rejordbe.TestConfig
import com.dev6.rejordbe.domain.badge.Badge
import com.dev6.rejordbe.domain.badge.BadgeAcquirementType
import com.dev6.rejordbe.domain.badge.BadgeCode
import com.dev6.rejordbe.domain.challenge.Challenge
import com.dev6.rejordbe.domain.challenge.ChallengeFlagType
import com.dev6.rejordbe.domain.challengeReview.ChallengeReview
import com.dev6.rejordbe.domain.challengeReview.ChallengeReviewType
import com.dev6.rejordbe.domain.user.Users
import com.dev6.rejordbe.exception.IllegalParameterException
import com.dev6.rejordbe.infrastructure.badge.read.ReadBadgeRepository
import com.dev6.rejordbe.infrastructure.challenge.read.ReadChallengeRepository
import com.dev6.rejordbe.infrastructure.challengeReview.read.ReadChallengeReviewRepository
import com.dev6.rejordbe.infrastructure.user.SignUpRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import spock.lang.Specification
import spock.lang.Unroll

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * ReadBadgeRepositorySpec
 */
@DataJpaTest
@EnableJpaAuditing
@Import(TestConfig.class)
class ReadBadgeRepositorySpec extends Specification {

    @PersistenceContext
    EntityManager entityManager

    @Autowired
    ReadBadgeRepository readBadgeRepository
    @Autowired
    ReadChallengeRepository readChallengeRepository
    @Autowired
    ReadChallengeReviewRepository readChallengeReviewRepository
    @Autowired
    SignUpRepository signUpRepository

    def "uid가 일치하는 배지를 취득할 수 있다"() {
        def user = Users.builder()
                .uid('uid')
                .nickname('nickname')
                .build()
        signUpRepository.save(user)
        // 챌린지 추가
        def challenge = readChallengeRepository.save(new Challenge("CH0", "찬 물로 세탁하기", "오늘은 찬물로 세탁기를 돌리고 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.WATER_FAIRY, "imgFront0", "imgBack0", "#2894DE", ChallengeFlagType.TODAY))
        // 챌린지 게시글 추가
        for (int i = 0; i < 10; i++) {
            readChallengeReviewRepository.save(
                    ChallengeReview.builder()
                            .challengeReviewId('challengeReviewId' + i)
                            .contents('contents')
                            .challengeReviewType(ChallengeReviewType.HARDSHIP)
                            .challenge(challenge)
                            .user(user)
                            .build()
            )

            readBadgeRepository.save(
                    Badge.builder()
                            .badgeId('BG' + i)
                            .badgeCode(BadgeCode.WATER_FAIRY)
                            .badgeAcquirementType(BadgeAcquirementType.CHALLENGE_REVIEW)
                            .parentId('challengeReviewId' + i)
                            .build()
            )
        }

        when:
        def result = readBadgeRepository.searchBadgeByUid('uid')

        entityManager.flush()
        entityManager.clear()

        then:
        result.get(0).getBadgeCode() == BadgeCode.WATER_FAIRY
        result.get(0).getBadgeName() != null
        result.get(0).getImageUrl() != null
        result.size() == 1
    }

    def "일치하는 uid가 없는 경우"() {
        when:
        def result = readBadgeRepository.searchBadgeByUid('uid')

        then:
        result.size() == 0
    }

    @Unroll('#testCase')
    def "지정된 uid가 없으면 에러: 400"() {
        when:
        readBadgeRepository.searchBadgeByUid(uid)

        then:
        thrown(IllegalParameterException)

        where:
        testCase            | uid
        'uid가 null'        | null
        'uid가 emptyString' | ''
    }
}
