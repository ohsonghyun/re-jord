package com.dev6.rejordbe.application.challengeReview.read

import com.dev6.rejordbe.application.post.read.ReadPostServiceImpl
import com.dev6.rejordbe.domain.challengeReview.ChallengeReviewType
import com.dev6.rejordbe.domain.challengeReview.dto.ChallengeReviewResult
import com.dev6.rejordbe.domain.post.PostType
import com.dev6.rejordbe.domain.post.dto.PostResult
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
                        new ChallengeReviewResult('challengeReviewId1', 'content', ChallengeReviewType.FREE, 'uid1', 'nickname1', LocalDateTime.now()),
                        new ChallengeReviewResult('challengeReviewId2', 'content', ChallengeReviewType.FREE, 'uid2', 'nickname2', LocalDateTime.now())
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
        it.extractingResultOf('getContents').containsOnly('content')
        it.extractingResultOf('getChallengeReviewType').containsOnly(ChallengeReviewType.FREE)
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
}
