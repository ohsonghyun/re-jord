package com.dev6.rejordbe.application.challengeReview.read

import com.dev6.rejordbe.domain.challenge.Challenge
import com.dev6.rejordbe.domain.challengeReview.ChallengeReview
import com.dev6.rejordbe.domain.challengeReview.ChallengeReviewType
import com.dev6.rejordbe.domain.challengeReview.dto.ChallengeReviewResult
import com.dev6.rejordbe.domain.user.Users
import com.dev6.rejordbe.exception.ChallengeReviewNotFoundException
import com.dev6.rejordbe.exception.IllegalParameterException
import com.dev6.rejordbe.infrastructure.challengeReview.read.ReadChallengeReviewRepository
import org.assertj.core.api.Assertions
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import spock.lang.Specification

import java.time.LocalDateTime

/**
 * ReadChallengeReviewServiceImplSpec
 */
class ReadChallengeReviewServiceImplSpec extends Specification {

    private ReadChallengeReviewServiceImpl readChallengeReviewService
    private ReadChallengeReviewRepository readChallengeReviewRepository

    def setup() {
        readChallengeReviewRepository = Mock(ReadChallengeReviewRepository)
        readChallengeReviewService = new ReadChallengeReviewServiceImpl(readChallengeReviewRepository)
    }

    def "기준 시각 이후의 모든 챌린지 게시글 정보를 획득할 수 있다"() {
        given:
        def now = LocalDateTime.now()
        def pageRequest = PageRequest.of(0, 5)
        readChallengeReviewRepository.searchAll(_ as LocalDateTime, _ as Pageable)
                >> new PageImpl<ChallengeReviewResult>(
                List.of(
                        new ChallengeReviewResult('challengeReviewId1', '찬 물로 세탁하기', 'content', ChallengeReviewType.HARDSHIP, 'uid1', 'nickname1', LocalDateTime.now()),
                        new ChallengeReviewResult('challengeReviewId2', '찬 물로 세탁하기', 'content', ChallengeReviewType.HARDSHIP, 'uid2', 'nickname2', LocalDateTime.now())
                ),
                pageRequest,
                1)

        when:
        def challengeReviews = readChallengeReviewService.allChallengeReviews(now, pageRequest)

        then:
        challengeReviews.getTotalPages() == 1
        challengeReviews.getTotalElements() == 2
        def it = Assertions.assertThat(challengeReviews.getContent())
        it.extractingResultOf('getChallengeReviewId').containsExactly('challengeReviewId1', 'challengeReviewId2')
        it.extractingResultOf('getTitle').containsOnly('찬 물로 세탁하기')
        it.extractingResultOf('getContents').containsOnly('content')
        it.extractingResultOf('getChallengeReviewType').containsOnly(ChallengeReviewType.HARDSHIP)
        it.extractingResultOf('getUid').containsExactly('uid1', 'uid2')
        it.extractingResultOf('getNickname').containsExactly('nickname1', 'nickname2')
        it.extractingResultOf('getCreatedDate').isNotEmpty()
    }

    def "기준 시각이 지정되지 않은 경우에는 에러: 400"() {
        when:
        readChallengeReviewService.allChallengeReviews(null, PageRequest.of(0, 5))

        then:
        thrown(IllegalParameterException)
    }

    // 게시글 데이터의 유무에 대한 테스트는 ReadChallengeReviewRepositorySpec 으로 대체

    def "uid와 일치하는 모든 챌린지 게시글 정보를 획득할 수 있다"() {
        given:
        def pageRequest = PageRequest.of(0, 5)
        readChallengeReviewRepository.searchChallengeReviewByUid(_ as String, _ as Pageable)
                >> new PageImpl<ChallengeReviewResult>(
                List.of(
                        new ChallengeReviewResult('challengeReviewId1', '찬 물로 세탁하기', 'content1', ChallengeReviewType.HARDSHIP, 'uid1', 'nickname1', LocalDateTime.now()),
                        new ChallengeReviewResult('challengeReviewId2', '찬 물로 세탁하기', 'content2', ChallengeReviewType.HARDSHIP, 'uid1', 'nickname1', LocalDateTime.now())
                ),
                pageRequest,
                1)

        when:
        def challengeReviews = readChallengeReviewService.challengeReviewsWrittenByUid('uid1', pageRequest)

        then:
        challengeReviews.getTotalPages() == 1
        challengeReviews.getTotalElements() == 2
        def it = Assertions.assertThat(challengeReviews.getContent())
        it.extractingResultOf('getChallengeReviewId').containsExactly('challengeReviewId1', 'challengeReviewId2')
        it.extractingResultOf('getTitle').containsOnly('찬 물로 세탁하기')
        it.extractingResultOf('getContents').containsExactly('content1', 'content2')
        it.extractingResultOf('getChallengeReviewType').containsOnly(ChallengeReviewType.HARDSHIP)
        it.extractingResultOf('getUid').containsOnly('uid1')
        it.extractingResultOf('getNickname').containsOnly('nickname1')
        it.extractingResultOf('getCreatedDate').isNotEmpty()
    }

    def "uid가 지정되지 않은 경우에는 에러: IllegalParameterException"() {
        when:
        readChallengeReviewService.challengeReviewsWrittenByUid(null, PageRequest.of(0, 5))

        then:
        thrown(IllegalParameterException)
    }

    // 챌린지 리뷰 게시글 수정 관련
    def "챌린지 리뷰 게시글 내용은 변경 가능하다"() {
        given:
        def anUser = Users.builder()
                .uid('uid')
                .build()

        def challenge = Challenge.builder()
                .title('찬 물로 샤워하기')
                .build()

        def anChallengeReview = ChallengeReview.builder()
                .contents(contents)
                .user(anUser)
                .challenge(challenge)
                .build()

        // mock
        readChallengeReviewRepository.findById(_) >> Optional.of(anChallengeReview)

        when:
        readChallengeReviewService.updateChallengeReviewInfo(ChallengeReview.builder().contents(newContents).build())

        then:
        anChallengeReview.getContents() == newContents

        where:
        contents   | newContents
        'contents' | 'newContents'
    }

    def "새로운 챌린지 리뷰 게시글 내용이 비어있으면 에러: IllegalParameterException"() {
        given:
        def anChallengeReview = ChallengeReview.builder()
                .contents(contents)
                .build()

        // mock
        readChallengeReviewRepository.findById(_) >> Optional.of(anChallengeReview)

        when:
        readChallengeReviewService.updateChallengeReviewInfo(ChallengeReview.builder().contents(newContents).build())

        then:
        thrown(IllegalParameterException)

        where:
        contents   | newContents
        'contents' | ''
        'contents' | ' '
        'contents' | null
    }

    def "내용을 수정할 챌린지 리뷰 게시글이 존재하니 않으면 에러: ChallengeReviewNotFoundException"() {
        given:
        // mock
        readChallengeReviewRepository.findById(_) >> Optional.empty()

        when:
        readChallengeReviewService.updateChallengeReviewInfo(ChallengeReview.builder().contents('contents').build())

        then:
        thrown(ChallengeReviewNotFoundException)
    }
    // / 챌린지 리뷰 게시글 수정 관련
}
