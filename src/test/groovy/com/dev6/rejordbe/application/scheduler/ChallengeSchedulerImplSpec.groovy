package com.dev6.rejordbe.application.scheduler

import com.dev6.rejordbe.domain.challenge.Challenge
import com.dev6.rejordbe.domain.challenge.ChallengeFlagType
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
        def randomChallenge = Challenge.builder().flag(ChallengeFlagType.NOT_TODAY).build()
        def targetChallenge = Challenge.builder().flag(ChallengeFlagType.TODAY).build()

        // mock
        readChallengeRepository.randomChallenge() >> randomChallenge
        readChallengeRepository.findChallengeByFlag(_ as ChallengeFlagType) >> Optional.of(targetChallenge)

        when:
        challengeScheduler.updateChallengeEveryday()

        then:
        randomChallenge.getFlag().equals(ChallengeFlagType.TODAY)
        targetChallenge.getFlag().equals(ChallengeFlagType.NOT_TODAY)
    }


    def "기존 설정된 플래그가 없는 경우"() {
        given:
        def randomChallenge = Challenge.builder().flag(ChallengeFlagType.NOT_TODAY).build()

        // mock
        readChallengeRepository.randomChallenge() >> randomChallenge
        readChallengeRepository.findChallengeByFlag(_ as ChallengeFlagType) >> Optional.empty()

        when:
        challengeScheduler.updateChallengeEveryday()

        then:
        randomChallenge.getFlag().equals(ChallengeFlagType.TODAY)
    }
}
