package com.dev6.rejordbe.application.challenge.read

import com.dev6.rejordbe.application.scheduler.ChallengeScheduler
import com.dev6.rejordbe.domain.challenge.Challenge
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
                .badgeId(badgeId)
                .badgeName(badgeName)
                .imgFront(imgFront)
                .imgBack(imgBack)
                .textColor(textColor)
                .flag(flag)
                .build()
        // mock
        readChallengeRepository.findChallengeByFlag(_ as Boolean) >> Optional.of(anChallenge)

        when:
        def challengeResult = readChallengeService.findChallengeByFlag().orElseThrow()

        then:
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

    def "flag가 true인 챌린지가 없을 경우 id가 CHDefault인 챌린지를 갖고온다"() {
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
        readChallengeRepository.findChallengeByFlag(_ as Boolean) >> Optional.empty()
        readChallengeRepository.findById(_ as String) >> Optional.of(anChallenge)

        when:
        def challengeResult = readChallengeService.findChallengeByFlag().orElseThrow()

        then:
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
        "CHDefault" | "title" | "contents" | 15              | "badgeId" | "badgeName" | "imgFront" | "imgBack" | "textColor" | true
    }
}
