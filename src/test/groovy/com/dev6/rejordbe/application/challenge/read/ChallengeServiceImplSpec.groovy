package com.dev6.rejordbe.application.challenge.read

import com.dev6.rejordbe.domain.badge.BadgeCode
import com.dev6.rejordbe.domain.challenge.Challenge
import com.dev6.rejordbe.domain.challenge.ChallengeFlagType
import com.dev6.rejordbe.infrastructure.challenge.read.ReadChallengeRepository
import spock.lang.Specification

/**
 * ChallengeServiceSpec
 */
class ChallengeServiceImplSpec extends Specification {

    ReadChallengeRepository readChallengeRepository
    ChallengeInfoService readChallengeService

    def setup() {
        readChallengeRepository = Mock(ReadChallengeRepository.class)
        readChallengeService = new ChallengeInfoServiceImpl(readChallengeRepository)
    }

    def "flag로 챌린지 검색이 가능하다"() {
        given:
        def anChallenge = Challenge.builder()
                .challengeId(challengeId)
                .title(title)
                .contents(contents)
                .footprintAmount(footprintAmount)
                .badgeCode(badgeCode)
                .imgFront(imgFront)
                .imgBack(imgBack)
                .textColor(textColor)
                .flag(flag)
                .build()
        // mock
        readChallengeRepository.findChallengeByFlag(_ as ChallengeFlagType) >> Optional.of(anChallenge)

        when:
        def challengeResult = readChallengeService.findTodayChallengeInFlag().orElseThrow()

        then:
        anChallenge != null
        anChallenge.getChallengeId() == challengeId
        anChallenge.getTitle() == title
        anChallenge.getContents() == contents
        anChallenge.getFootprintAmount() == footprintAmount
        anChallenge.getBadgeCode() == badgeCode
        challengeResult.getBadgeName() == badgeName
        anChallenge.getImgFront() == imgFront
        anChallenge.getImgBack() == imgBack
        anChallenge.getTextColor() == textColor
        anChallenge.getFlag() == flag

        where:
        challengeId   | title   | contents   | footprintAmount | badgeCode             | badgeName                | imgFront   | imgBack   | textColor   | flag
        "challengeId" | "title" | "contents" | 15              | BadgeCode.WATER_FAIRY | badgeCode.getBadgeName() | "imgFront" | "imgBack" | "textColor" | ChallengeFlagType.TODAY
    }

    def "flag가 true인 챌린지가 없을 경우 flag가 DEFAULT인 챌린지를 갖고온다"() {
        given:
        def anChallenge = Challenge.builder()
                .challengeId(challengeId)
                .title(title)
                .contents(contents)
                .footprintAmount(footprintAmount)
                .badgeCode(badgeCode)
                .imgFront(imgFront)
                .imgBack(imgBack)
                .textColor(textColor)
                .flag(flag)
                .build()
        // mock
        readChallengeRepository.findChallengeByFlag(ChallengeFlagType.TODAY) >> Optional.empty()
        readChallengeRepository.findChallengeByFlag(ChallengeFlagType.DEFAULT) >> Optional.of(anChallenge)

        when:
        def challengeResult = readChallengeService.findTodayChallengeInFlag().orElseThrow()

        then:
        anChallenge != null
        anChallenge.getChallengeId() == challengeId
        anChallenge.getTitle() == title
        anChallenge.getContents() == contents
        anChallenge.getFootprintAmount() == footprintAmount
        anChallenge.getBadgeCode() == badgeCode
        challengeResult.getBadgeName() == badgeName
        anChallenge.getImgFront() == imgFront
        anChallenge.getImgBack() == imgBack
        anChallenge.getTextColor() == textColor
        anChallenge.getFlag() == flag

        where:
        challengeId   | title   | contents   | footprintAmount | badgeCode         | badgeName                | imgFront   | imgBack   | textColor   | flag
        "CHDefault"   | "title" | "contents" | 15              | BadgeCode.DEFAULT | badgeCode.getBadgeName() | "imgFront" | "imgBack" | "textColor" | ChallengeFlagType.DEFAULT
    }
}
