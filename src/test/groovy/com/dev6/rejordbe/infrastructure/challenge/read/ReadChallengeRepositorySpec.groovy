package com.dev6.rejordbe.infrastructure.challenge.read

import com.dev6.rejordbe.TestConfig
import com.dev6.rejordbe.domain.challenge.Challenge
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

    def "flag == false 인 챌린지 중 랜덤 값을 갖고온다"() {
        // 챌린지 추가
        for (int i = 0; i < 5; i++) {
            readChallengeRepository.save(
                    Challenge.builder()
                            .challengeId("CH" + i)
                            .title("title" + i)
                            .contents("hello world" + i)
                            .footprintAmount(15)
                            .badgeId("BG" + i)
                            .badgeName("BGName" + i)
                            .imgFront("imgFront" + i)
                            .imgBack("imgBack" + i)
                            .textColor("textColor" + i)
                            .flag(false)
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

    def "flag == true 인 챌린지 값을 갖고온다"() {
        // 챌린지 추가
        readChallengeRepository.save(
                Challenge.builder()
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

    def "챌린지 flag를 수정할 수 있다"() {
        // 챌린지 추가
        for (int i = 0; i < 2; i++) {
            readChallengeRepository.save(
                    Challenge.builder()
                            .challengeId("CH" + i)
                            .title("title" + i)
                            .contents("hello world" + i)
                            .footprintAmount(15)
                            .badgeId("BG" + i)
                            .badgeName("BGName" + i)
                            .imgFront("imgFront" + i)
                            .imgBack("imgBack" + i)
                            .textColor("textColor" + i)
                            .flag(false)
                            .build()
            )
        }
        readChallengeRepository.save(
                Challenge.builder()
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
        )

        entityManager.flush()
        entityManager.clear()

        when:
        def anChallenge = readChallengeRepository.findChallengeByFlag(true).orElseThrow()
        anChallenge.updateFlag(
                Challenge.builder()
                .flag(anChallenge.getFlag())
                .build()
        )
        entityManager.flush()
        entityManager.clear()

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
        anChallenge.getFlag() == !flag

        where:
        challengeId   | title   | contents   | footprintAmount | badgeId   | badgeName   | imgFront   | imgBack   | textColor   | flag
        "challengeId" | "title" | "contents" | 15              | "badgeId" | "badgeName" | "imgFront" | "imgBack" | "textColor" | true

    }
}
