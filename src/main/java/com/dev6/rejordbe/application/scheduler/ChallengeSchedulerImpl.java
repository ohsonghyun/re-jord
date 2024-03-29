package com.dev6.rejordbe.application.scheduler;

import com.dev6.rejordbe.domain.challenge.Challenge;
import com.dev6.rejordbe.domain.challenge.ChallengeFlagType;
import com.dev6.rejordbe.infrastructure.challenge.read.ReadChallengeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ChallengeSchedulerImpl
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@lombok.RequiredArgsConstructor
public class ChallengeSchedulerImpl implements ChallengeScheduler {

    private final ReadChallengeRepository readChallengeRepository;

    /**
     * {@inheritDoc}
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    @Override
    public void updateChallengeEveryday() {
        Challenge randomChallenge = readChallengeRepository.randomChallenge();

        Challenge targetChallenge = readChallengeRepository.findChallengeByFlag(ChallengeFlagType.TODAY)
                .orElse(randomChallenge.updateFlagToTheOtherDay());

        if(targetChallenge != null) {
            targetChallenge.updateFlagToTheOtherDay();
        }

        if (randomChallenge.getFlag().equals(ChallengeFlagType.NOT_TODAY)) {
            randomChallenge.updateFlagToToday();
        }
    }
}
