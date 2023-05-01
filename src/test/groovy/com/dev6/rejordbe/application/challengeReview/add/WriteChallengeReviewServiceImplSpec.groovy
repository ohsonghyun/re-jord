package com.dev6.rejordbe.application.challengeReview.add

import com.dev6.rejordbe.application.id.IdGenerator
import com.dev6.rejordbe.domain.badge.BadgeCode
import com.dev6.rejordbe.domain.challenge.Challenge
import com.dev6.rejordbe.domain.challenge.ChallengeFlagType
import com.dev6.rejordbe.domain.challenge.dto.ChallengeResult
import com.dev6.rejordbe.domain.challengeReview.ChallengeReview
import com.dev6.rejordbe.domain.challengeReview.ChallengeReviewType
import com.dev6.rejordbe.domain.user.Users
import com.dev6.rejordbe.exception.IllegalParameterException
import com.dev6.rejordbe.exception.UserNotFoundException
import com.dev6.rejordbe.infrastructure.badge.add.AddBadgeRepository
import com.dev6.rejordbe.infrastructure.challenge.read.ReadChallengeRepository
import com.dev6.rejordbe.infrastructure.challengeReview.add.WriteChallengeReviewRepository
import com.dev6.rejordbe.infrastructure.footprint.add.AddFootprintRepository
import com.dev6.rejordbe.infrastructure.user.UserInfoRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import spock.lang.Specification
import spock.lang.Unroll

/**
 * WriteChallengeReviewServiceImplSpec
 */
class WriteChallengeReviewServiceImplSpec extends Specification {

    WriteChallengeReviewService writeChallengeReviewService
    ReadChallengeRepository readChallengeRepository
    WriteChallengeReviewRepository writeChallengeReviewRepository
    AddBadgeRepository addBadgeRepository;
    AddFootprintRepository addFootprintRepository;
    UserInfoRepository userInfoRepository
    IdGenerator idGenerator

    // shared
    Challenge challenge = new Challenge("CH0", "찬 물로 세탁하기", "오늘은 찬물로 세탁기를 돌리고 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.WATER_FAIRY, "imgFront0.png", "imgBack0.png", "#2894DE", ChallengeFlagType.TODAY)

    def setup() {
        writeChallengeReviewRepository = Mock(WriteChallengeReviewRepository.class)
        readChallengeRepository = Mock(ReadChallengeRepository.class)
        addBadgeRepository = Mock(AddBadgeRepository)
        addFootprintRepository = Mock(AddFootprintRepository)
        userInfoRepository = Mock(UserInfoRepository.class)
        idGenerator = Mock(IdGenerator.class)
        writeChallengeReviewService = new WriteChallengeReviewServiceImpl(
                writeChallengeReviewRepository,
                readChallengeRepository,
                addBadgeRepository,
                addFootprintRepository,
                userInfoRepository,
                idGenerator)
    }

    def "에러가 없는 경우 챌린지 리뷰를 등록할 수 있다"() {
        def anUser = Users.builder().uid(uid).build()
        userInfoRepository.findById(uid) >> Optional.of(anUser)
        readChallengeRepository.findById(_ as String) >> Optional.of(challenge)
        writeChallengeReviewRepository.save(_ as ChallengeReview) >> ChallengeReview.builder()
                .challengeReviewId(challengeReviewId)
                .contents(contents)
                .challengeReviewType(challengeReviewType)
                .challenge(challenge)
                .user(anUser)
                .build()

        when:
        def saveResult = writeChallengeReviewService.writeChallengeReview(
                challenge.getChallengeId(),
                contents,
                challengeReviewType,
                uid)

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
        userInfoRepository.findById(uid) >> Optional.empty()

        when:
        writeChallengeReviewService.writeChallengeReview(
                challenge.getChallengeId(),
                contents,
                challengeReviewType,
                uid
        )

        then:
        thrown(UserNotFoundException)

        where:
        contents   | challengeReviewType         | uid
        'contents' | ChallengeReviewType.FEELING | 'uid'
        'contents' | ChallengeReviewType.FEELING | null
    }

    def "필수 입력값 content가 비어있으면 에러"() {
        given:
        def anUser = Users.builder().uid(uid).build()
        userInfoRepository.findById(uid) >> Optional.of(anUser)
        readChallengeRepository.findById(_ as String) >> Optional.of(challenge)

        when:
        writeChallengeReviewService.writeChallengeReview(
                challenge.getChallengeId(),
                contents,
                challengeReviewType,
                uid
        )

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
        def anUser = Users.builder().uid(_ as String).build()
        readChallengeRepository.findById(_ as String) >> Optional.of(challenge)
        readChallengeRepository.findById(null) >> { throw new InvalidDataAccessApiUsageException("test") }
        userInfoRepository.findById(_ as String) >> Optional.of(anUser)

        when:
        writeChallengeReviewService.writeChallengeReview(
                challengeId,
                'contents',
                challengeReviewType,
                'uid'
        )

        then:
        thrown(IllegalParameterException)

        where:
        testCase              | challengeReviewType         | challengeId
        'challengeReviewType' | null                        | 'CH0'
        'challengeId'         | ChallengeReviewType.FEELING | null
    }
}