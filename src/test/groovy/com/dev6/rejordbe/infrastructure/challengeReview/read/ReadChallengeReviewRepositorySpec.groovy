package com.dev6.rejordbe.infrastructure.challengeReview.read

import com.dev6.rejordbe.TestConfig
import com.dev6.rejordbe.domain.challengeReview.ChallengeReview
import com.dev6.rejordbe.domain.challengeReview.ChallengeReviewType
import com.dev6.rejordbe.domain.challengeReview.dto.ChallengeReviewResult
import com.dev6.rejordbe.domain.user.Users
import com.dev6.rejordbe.infrastructure.user.SignUpRepository
import org.assertj.core.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import spock.lang.Specification
import spock.lang.Unroll

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import java.time.LocalDateTime

/**
 * ReadChallengeReviewRepositorySpec
 */
@DataJpaTest
@EnableJpaAuditing
@Import(TestConfig.class)
class ReadChallengeReviewRepositorySpec extends Specification {

    @PersistenceContext
    EntityManager entityManager

    @Autowired
    SignUpRepository signUpRepository
    @Autowired
    ReadChallengeReviewRepository readChallengeReviewRepository

    def "특정시간 이전의 챌린지 게시글을 최신글 순으로 취득할 수 있다: 데이터가 있는 경우"() {
        given:
        // 유저 생성
        def user = Users.builder()
                .uid('uid')
                .nickname('nickname')
                .build()
        signUpRepository.save(user)

        // 챌린지 게시글 추가
        for (int i = 0; i < 10; i++) {
            readChallengeReviewRepository.save(
                    ChallengeReview.builder()
                            .challengeReviewId('challengeReviewId' + i)
                            .contents('contents')
                            .challengeReviewType(ChallengeReviewType.HARDSHIP)
                            .user(user)
                            .build()
            )
        }

        entityManager.flush()
        entityManager.clear()

        when:
        // 데이터 입력 후 시간
        def now = LocalDateTime.now()
        def allChallengeReviews = readChallengeReviewRepository.searchAll(now, PageRequest.of(0, 10))

        entityManager.flush()
        entityManager.clear()

        then:
        allChallengeReviews.getContent().size() == 10
        allChallengeReviews.getContent().get(0) instanceof ChallengeReviewResult
        def it = Assertions.assertThat(allChallengeReviews.getContent())
        // 최신글 순으로 반환
        it.extractingResultOf('getChallengeReviewId').containsExactly(
                'challengeReviewId9',
                'challengeReviewId8',
                'challengeReviewId7',
                'challengeReviewId6',
                'challengeReviewId5',
                'challengeReviewId4',
                'challengeReviewId3',
                'challengeReviewId2',
                'challengeReviewId1',
                'challengeReviewId0'
        )
        it.extractingResultOf('getContents').containsOnly('contents')
        it.extractingResultOf('getChallengeReviewType').containsOnly(ChallengeReviewType.HARDSHIP)
        it.extractingResultOf('getUid').containsOnly('uid')
        it.extractingResultOf('getNickname').containsOnly('nickname')
        it.extractingResultOf('getCreatedDate').isNotEmpty()
    }

    def "특정시간 이전의 챌린지 게시글 취득할 수 있다: 데이터가 없는 경우"() {
        // 데이터 입력 전 시간
        def now = LocalDateTime.now()
        // 유저 생성
        def user = Users.builder()
                .uid('uid')
                .build()
        signUpRepository.save(user)

        // 게시글 추가
        for (int i = 0; i < 10; i++) {
            readChallengeReviewRepository.save(
                    ChallengeReview.builder()
                            .challengeReviewId('challengeReviewId' + i)
                            .contents('contents')
                            .challengeReviewType(ChallengeReviewType.HARDSHIP)
                            .user(user)
                            .build()
            )
        }

        entityManager.flush()
        entityManager.clear()

        when:
        def allPosts = readChallengeReviewRepository.searchAll(now, PageRequest.of(0, 10))

        entityManager.flush()
        entityManager.clear()

        then:
        allPosts.getContent().size() == 0
    }

    @Unroll("#testCase")
    def "uid가 일치하는 챌린지 리뷰 게시글을 취득할 수 있다"() {
        given:
        // 유저 생성
        def user = Users.builder()
                .uid('uid')
                .nickname('nickname')
                .build()
        signUpRepository.save(user)

        // 챌린지 게시글 추가
        for (int i = 0; i < 10; i++) {
            readChallengeReviewRepository.save(
                    ChallengeReview.builder()
                            .challengeReviewId('challengeReviewId' + i)
                            .contents('contents')
                            .challengeReviewType(ChallengeReviewType.HARDSHIP)
                            .user(user)
                            .build()
            )
        }

        entityManager.flush()
        entityManager.clear()

        when:
        def result = readChallengeReviewRepository.searchChallengeReviewByUid(uid, PageRequest.of(0, 10))

        entityManager.flush()
        entityManager.clear()

        then:
        result.getContent().size() == expectSize

        where:
        testCase               | uid    | expectSize
        "일치하는 uid가 있는 경우" | 'uid'  | 10
        "일치하는 uid가 없는 경우" | 'uid1' | 0
    }

}
