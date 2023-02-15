package com.dev6.rejordbe.application.challenge.read

import com.dev6.rejordbe.domain.challenge.Challenge
import com.dev6.rejordbe.exception.ChallengeNotFoundException
import com.dev6.rejordbe.infrastructure.challenge.read.ReadChallengeRepository
import spock.lang.Specification

/**
 * ChallengeServiceSpec
 */
class ChallengeServiceImplSpec extends Specification {

    ReadChallengeRepository readChallengeRepository
    ReadChallengeService readChallengeService

    def setup() {
        readChallengeRepository = Mock(ReadChallengeRepository.class)
        readChallengeService = new ReadChallengeServiceImpl(readChallengeRepository)
    }

    def "flag로 챌린지 검색이 가능하다"() {
        given:
        def anChallenge = Challenge.builder()
                .challengeId(challengeId)
                .title(title)
                .contents(contents)
                .footprintAmount(footprintAmount)
                .badgeId(badgeId)
                .badgeName(badgeName)
                .imgFront(imgFront)
                .imgBack(imgBack)
                .textColor(textColor)
                .flag(flag)
                .build()
        // mock
        readChallengeRepository.findChallengeByFlag(_ as Boolean) >> Optional.of(anChallenge)

        expect:
        def challengeResult = readChallengeService.findChallengeByFlag(flag).orElseThrow()

        anChallenge != null
        anChallenge.getChallengeId() == challengeId
        anChallenge.getTitle() == title
        anChallenge.getContents() == contents
        anChallenge.getFootprintAmount() == footprintAmount
        anChallenge.getBadgeId() == badgeId
        anChallenge.getBadgeName() == badgeName
        anChallenge.getImgFront() == imgFront
        anChallenge.getImgBack() == imgBack
        anChallenge.getTextColor() == textColor
        anChallenge.getFlag() == flag

        where:
        challengeId   | title   | contents   | footprintAmount | badgeId   | badgeName   | imgFront   | imgBack   | textColor   | flag
        "challengeId" | "title" | "contents" | 15              | "badgeId" | "badgeName" | "imgFront" | "imgBack" | "textColor" | true
    }

    def "flag 가 true인 챌린지가 없으면 에러: ChallengeNotFoundException"() {
        given:
        // mock
        readChallengeRepository.findChallengeByFlag(_ as Boolean) >> Optional.empty()

        when:
        readChallengeService.findChallengeByFlag(true).orElseThrow()

        then:
        thrown(ChallengeNotFoundException)
    }
}
