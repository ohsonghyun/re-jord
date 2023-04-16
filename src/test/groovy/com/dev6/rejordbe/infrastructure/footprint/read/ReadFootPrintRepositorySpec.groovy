package com.dev6.rejordbe.infrastructure.footprint.read

import com.dev6.rejordbe.TestConfig
import com.dev6.rejordbe.application.challengeReview.add.WriteChallengeReviewServiceImpl
import com.dev6.rejordbe.application.id.IdGeneratorImpl
import com.dev6.rejordbe.domain.badge.BadgeCode
import com.dev6.rejordbe.domain.challenge.Challenge
import com.dev6.rejordbe.domain.challenge.ChallengeFlagType
import com.dev6.rejordbe.domain.challengeReview.ChallengeReview
import com.dev6.rejordbe.domain.challengeReview.ChallengeReviewType
import com.dev6.rejordbe.domain.footprint.FootprintAcquirementType
import com.dev6.rejordbe.domain.footprint.dto.FootprintResult
import com.dev6.rejordbe.domain.role.Role
import com.dev6.rejordbe.domain.role.RoleType
import com.dev6.rejordbe.domain.user.Users
import com.dev6.rejordbe.exception.IllegalParameterException
import com.dev6.rejordbe.infrastructure.badge.add.AddBadgeRepository
import com.dev6.rejordbe.infrastructure.challenge.read.ReadChallengeRepository
import com.dev6.rejordbe.infrastructure.challengeReview.add.WriteChallengeReviewRepository
import com.dev6.rejordbe.infrastructure.footprint.add.AddFootprintRepository
import com.dev6.rejordbe.infrastructure.user.SignUpRepository
import com.dev6.rejordbe.infrastructure.user.UserInfoRepository
import org.assertj.core.api.Assertions
import org.assertj.core.api.ListAssert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import spock.lang.Specification
import spock.lang.Unroll

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * ReadFootPrintRepositoryTest
 */
@DataJpaTest
@EnableJpaAuditing
@Import(TestConfig.class)
class ReadFootPrintRepositorySpec extends Specification {

    @PersistenceContext
    EntityManager entityManager

    @Autowired
    ReadFootPrintRepository readFootPrintRepository

    WriteChallengeReviewServiceImpl writeChallengeReviewService
    @Autowired
    SignUpRepository signUpRepository
    @Autowired
    AddFootprintRepository addFootprintRepository
    @Autowired
    WriteChallengeReviewRepository writeChallengeReviewRepository
    @Autowired
    AddBadgeRepository addBadgeRepository
    @Autowired
    UserInfoRepository userInfoRepository
    @Autowired
    ReadChallengeRepository readChallengeRepository

    def setup() {
        writeChallengeReviewService = new WriteChallengeReviewServiceImpl(
                writeChallengeReviewRepository,
                readChallengeRepository,
                addBadgeRepository,
                addFootprintRepository,
                userInfoRepository,
                new IdGeneratorImpl())
    }

    def "uid로 발자국을 취득할 수 있다"() {
        given:
        // 롤 정의
        def role = new Role(RoleType.ROLE_USER)
        entityManager.persist(role)

        // 유저 생성
        signUpRepository.save(
                Users.builder()
                        .uid(uid)
                        .userId('userId')
                        .nickname('nickname')
                        .password('password')
                        .roles(Collections.singletonList(role))
                        .build())

        // 챌린지 추가
        def challenge = new Challenge(
                "CH0",
                "challengeTitle",
                "challengeContents",
                15,
                BadgeCode.WATER_FAIRY,
                "imgFront0",
                "imgBack0",
                "#2894DE",
                ChallengeFlagType.TODAY)
        entityManager.persist(challenge);

        // ChallengeReview 추가
        writeChallengeReviewService.writeChallengeReview(
                'CH0',
                'challengeContents',
                ChallengeReviewType.HARDSHIP,
                uid)

        when:
        def resultPage = readFootPrintRepository.searchAllByUid(uid, PageRequest.of(0, 5))

        then:
        resultPage.getTotalElements() == 1
        resultPage.getSize() == 5
        resultPage.getNumberOfElements() == 1
        def it = Assertions.assertThat(resultPage.getContent())
        it.extractingResultOf('getFootprintId').isNotNull()
        it.extractingResultOf('getFootprintAmount').containsExactly(15)
        it.extractingResultOf('getParentId').isNotNull()
        it.extractingResultOf('getFootprintAcquirementType').containsExactly(FootprintAcquirementType.CHALLENGE_REVIEW)
        it.extractingResultOf('getTitle').containsExactly('challengeTitle')
        it.extractingResultOf('getBadgeCode').containsExactly(BadgeCode.WATER_FAIRY)
        it.extractingResultOf('getCreatedDate').isNotNull()

        where:
        uid << ['uid']
    }

    @Unroll('#testCase')
    def "byUid 검색에서 지정된 uid가 없으면 에러: 400"() {
        when:
        readFootPrintRepository.searchAllByUid(uid, PageRequest.of(0, 5))

        then:
        thrown(IllegalParameterException)

        where:
        testCase | uid
        'uid가 null' | null
        'uid가 emptyString' | ''
    }
}
