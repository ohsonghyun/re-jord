package com.dev6.rejordbe.infrastructure.challenge.read

import com.dev6.rejordbe.TestConfig
import com.dev6.rejordbe.domain.badge.BadgeCode
import com.dev6.rejordbe.domain.challenge.Challenge
import com.dev6.rejordbe.domain.challenge.ChallengeFlagType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * ReadChallengeRepositorySpec
 */
@DataJpaTest
@EnableJpaAuditing
@Import(TestConfig.class)
class ReadChallengeRepositorySpec extends Specification {

    @PersistenceContext
    EntityManager entityManager

    @Autowired
    ReadChallengeRepository readChallengeRepository

    def "flag가 THE_OTHER_DAY 인 챌린지 중 랜덤 값을 갖고온다"() {
        // 챌린지 추가
        for (int i = 0; i < 5; i++) {
            readChallengeRepository.save(
                    Challenge.builder()
                            .challengeId("CH" + i)
                            .title("title" + i)
                            .contents("hello world" + i)
                            .footprintAmount(15)
                            .badgeCode(BadgeCode.FOREST_LOVER)
                            .badgeName("BGName" + i)
                            .imgFront("imgFront" + i)
                            .imgBack("imgBack" + i)
                            .textColor("textColor" + i)
                            .flag(ChallengeFlagType.NOT_TODAY)
                            .build()
            )
        }

        entityManager.flush()
        entityManager.clear()

        when:
        def random = readChallengeRepository.randomChallenge()

        then:
        random != null
    }

    def "id가 CHDefault 인 챌린지 값을 갖고온다"() {
        // 챌린지 추가
        readChallengeRepository.save(
                Challenge.builder()
                        .challengeId(challengeId)
                        .title(title)
                        .contents(contents)
                        .footprintAmount(footprintAmount)
                        .badgeCode(badgeCode)
                        .badgeName(badgeName)
                        .imgFront(imgFront)
                        .imgBack(imgBack)
                        .textColor(textColor)
                        .flag(flag)
                        .build()
        )

        entityManager.flush()
        entityManager.clear()

        when:
        def anChallenge = readChallengeRepository.findById(challengeId).orElseThrow()

        then:
        anChallenge != null
        anChallenge.getChallengeId() == challengeId
        anChallenge.getTitle() == title
        anChallenge.getContents() == contents
        anChallenge.getFootprintAmount() == footprintAmount
        anChallenge.getBadgeCode() == badgeCode
        anChallenge.getBadgeName() == badgeName
        anChallenge.getImgFront() == imgFront
        anChallenge.getImgBack() == imgBack
        anChallenge.getTextColor() == textColor
        anChallenge.getFlag() == flag

        where:
        challengeId   | title   | contents   | footprintAmount | badgeCode         | badgeName   | imgFront   | imgBack   | textColor   | flag
        "CHDefault"   | "title" | "contents" | 15              | BadgeCode.DEFAULT | "badgeName" | "imgFront" | "imgBack" | "textColor" | ChallengeFlagType.TODAY
    }

    def "flag == true 인 챌린지 값을 갖고온다"() {
        // 챌린지 추가
        readChallengeRepository.save(
                Challenge.builder()
                        .challengeId(challengeId)
                        .title(title)
                        .contents(contents)
                        .footprintAmount(footprintAmount)
                        .badgeCode(badgeCode)
                        .badgeName(badgeName)
                        .imgFront(imgFront)
                        .imgBack(imgBack)
                        .textColor(textColor)
                        .flag(flag)
                        .build()
        )

        entityManager.flush()
        entityManager.clear()

        when:
        def anChallenge = readChallengeRepository.findChallengeByFlag(flag).orElseThrow()

        then:
        anChallenge != null
        anChallenge.getChallengeId() == challengeId
        anChallenge.getTitle() == title
        anChallenge.getContents() == contents
        anChallenge.getFootprintAmount() == footprintAmount
        anChallenge.getBadgeCode() == badgeCode
        anChallenge.getBadgeName() == badgeName
        anChallenge.getImgFront() == imgFront
        anChallenge.getImgBack() == imgBack
        anChallenge.getTextColor() == textColor
        anChallenge.getFlag() == flag

        where:
        challengeId   | title   | contents   | footprintAmount | badgeCode             | badgeName   | imgFront   | imgBack   | textColor   | flag
        "challengeId" | "title" | "contents" | 15              | BadgeCode.PRO_SHOPPER | "badgeName" | "imgFront" | "imgBack" | "textColor" | ChallengeFlagType.TODAY
    }

    def "챌린지 flag를 수정할 수 있다"() {
        // 챌린지 추가
        for (int i = 0; i < 2; i++) {
            readChallengeRepository.save(
                    Challenge.builder()
                            .challengeId("CH" + i)
                            .title("title" + i)
                            .contents("hello world" + i)
                            .footprintAmount(15)
                            .badgeCode(BadgeCode.ENERGY_SAVER)
                            .badgeName("BGName" + i)
                            .imgFront("imgFront" + i)
                            .imgBack("imgBack" + i)
                            .textColor("textColor" + i)
                            .flag(ChallengeFlagType.NOT_TODAY)
                            .build()
            )
        }
        readChallengeRepository.save(
                Challenge.builder()
                        .challengeId(challengeId)
                        .title(title)
                        .contents(contents)
                        .footprintAmount(footprintAmount)
                        .badgeCode(BadgeCode.MEAL_PLANNER)
                        .badgeName(badgeName)
                        .imgFront(imgFront)
                        .imgBack(imgBack)
                        .textColor(textColor)
                        .flag(flag)
                        .build()
        )

        entityManager.flush()
        entityManager.clear()

        when:
        def anChallenge = readChallengeRepository.findChallengeByFlag(flag).orElseThrow()
        anChallenge.updateFlagToTheOtherDay()

        entityManager.flush()
        entityManager.clear()

        then:
        anChallenge != null
        anChallenge.getChallengeId() == challengeId
        anChallenge.getTitle() == title
        anChallenge.getContents() == contents
        anChallenge.getFootprintAmount() == footprintAmount
        anChallenge.getBadgeCode() == badgeCode
        anChallenge.getBadgeName() == badgeName
        anChallenge.getImgFront() == imgFront
        anChallenge.getImgBack() == imgBack
        anChallenge.getTextColor() == textColor
        anChallenge.getFlag().equals(ChallengeFlagType.NOT_TODAY)

        where:
        challengeId   | title   | contents   | footprintAmount | badgeCode              | badgeName   | imgFront   | imgBack   | textColor   | flag
        "challengeId" | "title" | "contents" | 15              | BadgeCode.MEAL_PLANNER | "badgeName" | "imgFront" | "imgBack" | "textColor" | ChallengeFlagType.TODAY

    }
}
