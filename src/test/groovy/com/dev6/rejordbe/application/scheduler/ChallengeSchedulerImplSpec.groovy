package com.dev6.rejordbe.application.scheduler

import com.dev6.rejordbe.domain.challenge.Challenge
import com.dev6.rejordbe.infrastructure.challenge.read.ReadChallengeRepository
import spock.lang.Specification

/**
 * ChallengeSchedulerImplSpec
 */
class ChallengeSchedulerImplSpec extends Specification {

    ReadChallengeRepository readChallengeRepository
    ChallengeScheduler challengeScheduler

    def setup() {
        readChallengeRepository = Mock(ReadChallengeRepository.class)
        challengeScheduler = new ChallengeSchedulerImpl(readChallengeRepository)
    }

    def "챌린지 기본값 설정"() {
        given:
        def randomChallenge = Challenge.builder().flag(false).build()
        def targetChallenge = Challenge.builder().flag(true).build()

        // mock
        readChallengeRepository.randomChallenge() >> randomChallenge
        readChallengeRepository.findChallengeByFlag(_ as Boolean) >> Optional.of(targetChallenge)

        when:
        challengeScheduler.setChallengeEveryMidnight()

        then:
        randomChallenge.getFlag() == true
        targetChallenge.getFlag() == false
    }


    def "기존 설정된 플래그가 없는 경우"() {
        given:
        def randomChallenge = Challenge.builder().flag(false).build()

        // mock
        readChallengeRepository.randomChallenge() >> randomChallenge
        readChallengeRepository.findChallengeByFlag(_ as Boolean) >> Optional.empty()

        when:
        challengeScheduler.setChallengeEveryMidnight()

        then:
        randomChallenge.getFlag() == true
    }
}
