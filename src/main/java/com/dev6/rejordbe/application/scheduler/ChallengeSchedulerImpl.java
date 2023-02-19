package com.dev6.rejordbe.application.scheduler;

import com.dev6.rejordbe.domain.challenge.Challenge;
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
    @Scheduled(cron = "0 53 16 * * *")
    @Transactional
    @Override
    public void setChallengeEveryMidnight() {
        Challenge randomChallenge = readChallengeRepository.randomChallenge();

        Challenge targetChallenge = readChallengeRepository.findChallengeByFlag(true)
                .orElse(randomChallenge.updateFlag(Challenge.builder().flag(randomChallenge.getFlag()).build()));

        targetChallenge.updateFlag(Challenge.builder().flag(targetChallenge.getFlag()).build());

        if (!randomChallenge.getFlag()) {
            randomChallenge.updateFlag(Challenge.builder().flag(randomChallenge.getFlag()).build());
        }
    }
}
