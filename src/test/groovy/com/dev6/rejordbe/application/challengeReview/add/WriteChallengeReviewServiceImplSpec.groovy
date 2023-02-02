package com.dev6.rejordbe.application.challengeReview.add

import com.dev6.rejordbe.application.badge.add.AddBadgeService
import com.dev6.rejordbe.application.footprint.add.AddFootprintService
import com.dev6.rejordbe.application.id.IdGenerator
import com.dev6.rejordbe.domain.challengeReview.ChallengeReview
import com.dev6.rejordbe.domain.challengeReview.ChallengeReviewType
import com.dev6.rejordbe.domain.user.Users
import com.dev6.rejordbe.exception.IllegalParameterException
import com.dev6.rejordbe.exception.UserNotFoundException
import com.dev6.rejordbe.infrastructure.challengeReview.add.WriteChallengeReviewRepository
import com.dev6.rejordbe.infrastructure.user.UserInfoRepository
import spock.lang.Specification

/**
 * WriteChallengeReviewServiceImplSpec
 */
class WriteChallengeReviewServiceImplSpec extends Specification {

    WriteChallengeReviewService writeChallengeReviewService
    WriteChallengeReviewRepository writeChallengeReviewRepository
    UserInfoRepository userInfoRepository
    AddFootprintService addFootprintService
    AddBadgeService addBadgeService
    IdGenerator idGenerator

    def setup() {
        writeChallengeReviewRepository = Mock(WriteChallengeReviewRepository.class)
        idGenerator = Mock(IdGenerator.class)
        userInfoRepository = Mock(UserInfoRepository.class)
        addFootprintService = Mock(AddFootprintService.class)
        addBadgeService = Mock(AddBadgeService.class)
        writeChallengeReviewService = new WriteChallengeReviewServiceImpl(writeChallengeReviewRepository, idGenerator, userInfoRepository, addFootprintService, addBadgeService)
    }

    def "에러가 없는 경우 챌린지 리뷰를 등록할 수 있다"() {
        def anUser = Users.builder()
                .uid(uid)
                .build()

        userInfoRepository.findById(uid) >> Optional.of(anUser)

        writeChallengeReviewRepository.save(_ as ChallengeReview) >> ChallengeReview.builder()
                .challengeReviewId(challengeReviewId)
                .contents(contents)
                .challengeReviewType(challengeReviewType)
                .user(anUser)
                .build()

        when:
        def saveResult = writeChallengeReviewService.writeChallengeReview(
                ChallengeReview.builder()
                    .challengeReviewId(challengeReviewId)
                    .contents(contents)
                    .challengeReviewType(challengeReviewType)
                    .user(anUser)
                    .build(), uid)

        then:
        saveResult.getChallengeReviewId() == challengeReviewId
        saveResult.getContents() == contents
        saveResult.getChallengeReviewType() == challengeReviewType
        saveResult.getUid() == uid

        where:
        challengeReviewId   | contents   | challengeReviewType         | uid
        'challengeReviewId' | 'contents' | ChallengeReviewType.FEELING | 'uid'
    }

    def "존재하지 않는 유저면 에러"() {
        def anUser = Users.builder()
                .uid(uid)
                .build()

        userInfoRepository.findById(uid) >> Optional.empty()

        writeChallengeReviewRepository.save(_ as ChallengeReview) >> ChallengeReview.builder()
                .challengeReviewId(challengeReviewId)
                .contents(contents)
                .challengeReviewType(challengeReviewType)
                .user(anUser)
                .build()

        when:
        writeChallengeReviewService.writeChallengeReview(
                ChallengeReview.builder()
                        .challengeReviewId(challengeReviewId)
                        .contents(contents)
                        .challengeReviewType(challengeReviewType)
                        .user(anUser)
                        .build(), uid)

        then:
        thrown(UserNotFoundException)

        where:
        challengeReviewId   | contents   | challengeReviewType         | uid
        'challengeReviewId' | 'contents' | ChallengeReviewType.FEELING | 'uid'
        'challengeReviewId' | 'contents' | ChallengeReviewType.FEELING | null

    }

    def "필수 입력값 content가 비어있으면 에러"() {
        def anUser = Users.builder()
                .uid(uid)
                .build()

        userInfoRepository.findById(uid) >> Optional.of(anUser)

        writeChallengeReviewRepository.save(_ as ChallengeReview) >> ChallengeReview.builder()
                .challengeReviewId(challengeReviewId)
                .contents(contents)
                .challengeReviewType(challengeReviewType)
                .user(anUser)
                .build()

        when:
        writeChallengeReviewService.writeChallengeReview(
                ChallengeReview.builder()
                        .challengeReviewId(challengeReviewId)
                        .contents(contents)
                        .challengeReviewType(challengeReviewType)
                        .user(anUser)
                        .build(), uid)

        then:
        thrown(IllegalParameterException)

        where:
        challengeReviewId   | contents | challengeReviewType         | uid
        'challengeReviewId' | ''       | ChallengeReviewType.FEELING | 'uid'
        'challengeReviewId' | '  '     | ChallengeReviewType.FEELING | 'uid'
        'challengeReviewId' | null     | ChallengeReviewType.FEELING | 'uid'
    }
}
