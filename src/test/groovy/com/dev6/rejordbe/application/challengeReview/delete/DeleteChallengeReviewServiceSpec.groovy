package com.dev6.rejordbe.application.challengeReview.delete

import com.dev6.rejordbe.domain.challengeReview.ChallengeReview
import com.dev6.rejordbe.domain.user.Users
import com.dev6.rejordbe.exception.ChallengeReviewNotFoundException
import com.dev6.rejordbe.exception.IllegalParameterException
import com.dev6.rejordbe.exception.UnauthorizedUserException
import com.dev6.rejordbe.infrastructure.challengeReview.delete.DeleteChallengeReviewRepository
import spock.lang.Specification
import spock.lang.Unroll

/**
 * DeleteChallengeReviewServiceSpec
 */
class DeleteChallengeReviewServiceSpec extends Specification {

    private DeleteChallengeReviewServiceImpl deleteChallengeReviewService
    private DeleteChallengeReviewRepository deleteChallengeReviewRepository

    def setup() {
        deleteChallengeReviewRepository = Mock(DeleteChallengeReviewRepository)
        deleteChallengeReviewService = new DeleteChallengeReviewServiceImpl(deleteChallengeReviewRepository)
    }

    // 챌린지 리뷰 게시글 삭제 관련
    def "챌린지 리뷰 게시글을 삭제할 수 있다"() {
        given:
        def anUser = Users.builder()
                .uid('uid')
                .build()

        def anChallengeReview = ChallengeReview.builder()
                .challengeReviewId('challengeReviewId')
                .user(anUser)
                .build()

        // mock
        deleteChallengeReviewRepository.findById(_ as String) >> Optional.of(anChallengeReview)

        when:
        def challengeReviewId = deleteChallengeReviewService.deleteChallengeReview('challengeReviewId', 'uid')

        then:
        anChallengeReview.challengeReviewId == challengeReviewId
    }

    @Unroll
    def "실패 케이스: #testCase 반환"() {
        given:
        def anUser = Users.builder()
                .uid('uid')
                .build()

        def anChallengeReview = ChallengeReview.builder()
                .challengeReviewId('challengeReviewId')
                .user(anUser)
                .build()

        // mock
        deleteChallengeReviewRepository.findById(_ as String) >> Optional.of(anChallengeReview)


        when:
        deleteChallengeReviewService.deleteChallengeReview(challengeReviewId, uid)

        then:
        thrown(exception)

        where:
        testCase                               | exception                 | challengeReviewId   | uid
        'challengeReviewId가 정책에 어긋나는 경우' | IllegalParameterException | ''                  | 'uid'
        'challengeReviewId가 정책에 어긋나는 경우' | IllegalParameterException | null                | 'uid'
        '권한이 없는 유저가 접근한 경우'            | UnauthorizedUserException | 'challengeReviewId' | 'uid1'
    }

    def "존재하지 않는 챌린지 리뷰 게시글일 경우"() {
        given:
        // mock
        deleteChallengeReviewRepository.findById(_ as String) >> Optional.empty()

        when:
        deleteChallengeReviewService.deleteChallengeReview('challengeReviewId', 'uid')

        then:
        thrown(ChallengeReviewNotFoundException)
    }
    // / 챌린지 리뷰 게시글 삭제 관련
}
