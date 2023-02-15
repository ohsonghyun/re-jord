package com.dev6.rejordbe.application.scheduler;

import com.dev6.rejordbe.domain.challenge.Challenge;
import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.exception.ChallengeNotFoundException;
import com.dev6.rejordbe.infrastructure.challenge.read.ReadChallengeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    public void run() {
        Challenge randomChallenge = readChallengeRepository.randomChallenge()
                .orElseThrow(() -> new ChallengeNotFoundException(ExceptionCode.CHALLENGE_NOT_FOUND.name()));

        Challenge targetChallenge = readChallengeRepository.findChallengeByFlag(true)
                .orElse(randomChallenge.update(Challenge.builder().flag(randomChallenge.getFlag()).build()));

        targetChallenge.update(Challenge.builder().flag(targetChallenge.getFlag()).build());

        if (!randomChallenge.getFlag()) {
            randomChallenge.update(Challenge.builder().flag(randomChallenge.getFlag()).build());
        }
    }
}
