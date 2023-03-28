package com.dev6.rejordbe.application.challengeReview.add

import com.dev6.rejordbe.application.id.IdGenerator
import com.dev6.rejordbe.domain.badge.BadgeCode
import com.dev6.rejordbe.domain.challenge.dto.ChallengeResult
import com.dev6.rejordbe.domain.challengeReview.ChallengeReview
import com.dev6.rejordbe.domain.challengeReview.ChallengeReviewType
import com.dev6.rejordbe.domain.user.Users
import com.dev6.rejordbe.exception.IllegalParameterException
import com.dev6.rejordbe.exception.UserNotFoundException
import com.dev6.rejordbe.infrastructure.badge.add.AddBadgeRepository
import com.dev6.rejordbe.infrastructure.challengeReview.add.WriteChallengeReviewRepository
import com.dev6.rejordbe.infrastructure.footprint.add.AddFootprintRepository
import com.dev6.rejordbe.infrastructure.user.UserInfoRepository
import spock.lang.Specification
import spock.lang.Unroll

/**
 * WriteChallengeReviewServiceImplSpec
 */
class WriteChallengeReviewServiceImplSpec extends Specification {

    WriteChallengeReviewService writeChallengeReviewService
    WriteChallengeReviewRepository writeChallengeReviewRepository
    AddBadgeRepository addBadgeRepository;
    AddFootprintRepository addFootprintRepository;
    UserInfoRepository userInfoRepository
    IdGenerator idGenerator

    def setup() {
        writeChallengeReviewRepository = Mock(WriteChallengeReviewRepository.class)
        addBadgeRepository = Mock(AddBadgeRepository)
        addFootprintRepository = Mock(AddFootprintRepository)
        userInfoRepository = Mock(UserInfoRepository.class)
        idGenerator = Mock(IdGenerator.class)
        writeChallengeReviewService = new WriteChallengeReviewServiceImpl(writeChallengeReviewRepository, addBadgeRepository, addFootprintRepository, userInfoRepository, idGenerator)
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
                .footprintAmount(footprintAmount)
                .badgeCode(badgeCode)
                .user(anUser)
                .build()

        when:
        def saveResult = writeChallengeReviewService.writeChallengeReview(
                ChallengeReview.builder()
                        .challengeReviewId(challengeReviewId)
                        .contents(contents)
                        .challengeReviewType(challengeReviewType)
                        .footprintAmount(footprintAmount)
                        .badgeCode(badgeCode)
                        .user(anUser)
                        .build(), uid)

        then:
        saveResult.getChallengeReviewId() == challengeReviewId
        saveResult.getContents() == contents
        saveResult.getChallengeReviewType() == challengeReviewType
        saveResult.getUid() == uid

        where:
        challengeReviewId   | contents   | challengeReviewType          | footprintAmount | badgeCode             | uid
        'challengeReviewId' | 'contents' | ChallengeReviewType.HARDSHIP | 15              | BadgeCode.WATER_FAIRY | 'uid'
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
                .footprintAmount(footprintAmount)
                .badgeCode(badgeCode)
                .user(anUser)
                .build()

        when:
        writeChallengeReviewService.writeChallengeReview(
                ChallengeReview.builder()
                        .challengeReviewId(challengeReviewId)
                        .contents(contents)
                        .challengeReviewType(challengeReviewType)
                        .footprintAmount(footprintAmount)
                        .badgeCode(badgeCode)
                        .user(anUser)
                        .build(), uid)

        then:
        thrown(UserNotFoundException)

        where:
        challengeReviewId   | contents   | challengeReviewType         | footprintAmount | badgeCode             | uid
        'challengeReviewId' | 'contents' | ChallengeReviewType.FEELING | 15              | BadgeCode.WATER_FAIRY | 'uid'
        'challengeReviewId' | 'contents' | ChallengeReviewType.FEELING | 15              | BadgeCode.WATER_FAIRY | null

    }

    def "필수 입력값 content가 비어있으면 에러"() {
        given:
        def anUser = Users.builder()
                .uid(uid)
                .build()
        userInfoRepository.findById(uid) >> Optional.of(anUser)

        when:
        writeChallengeReviewService.writeChallengeReview(
                ChallengeReview.builder()
                        .challengeReviewId(challengeReviewId)
                        .contents(contents)
                        .challengeReviewType(challengeReviewType)
                        .footprintAmount(footprintAmount)
                        .badgeCode(badgeCode)
                        .user(anUser)
                        .build(), uid)

        then:
        thrown(IllegalParameterException)

        where:
        challengeReviewId   | contents | challengeReviewType         | footprintAmount | badgeCode             | uid
        'challengeReviewId' | ''       | ChallengeReviewType.FEELING | 15              | BadgeCode.WATER_FAIRY | 'uid'
        'challengeReviewId' | '  '     | ChallengeReviewType.FEELING | 15              | BadgeCode.WATER_FAIRY | 'uid'
        'challengeReviewId' | null     | ChallengeReviewType.FEELING | 15              | BadgeCode.WATER_FAIRY | 'uid'
    }

    @Unroll("#testCase")
    def "필수 입력값 #testCase가 비어있으면 에러"() {
        given:
        def anUser = Users.builder()
                .uid(uid)
                .build()

        userInfoRepository.findById(uid) >> Optional.of(anUser)

        when:
        writeChallengeReviewService.writeChallengeReview(
                ChallengeReview.builder()
                        .challengeReviewId(challengeReviewId)
                        .contents(contents)
                        .challengeReviewType(challengeReviewType)
                        .footprintAmount(footprintAmount)
                        .badgeCode(badgeCode)
                        .user(anUser)
                        .build(), uid)

        then:
        thrown(IllegalParameterException)

        where:
        testCase              | challengeReviewId   | contents   | challengeReviewType         | footprintAmount | badgeCode             | uid
        'challengeReviewType' | 'challengeReviewId' | 'contents' | null                        | 15              | BadgeCode.WATER_FAIRY | 'uid'
        'badgeCode'           | 'challengeReviewId' | 'contents' | ChallengeReviewType.FEELING | 15              | null                  | 'uid'
        'footprintAmount'     | 'challengeReviewId' | 'contents' | ChallengeReviewType.FEELING | null            | BadgeCode.WATER_FAIRY | 'uid'
    }
}