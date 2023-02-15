package com.dev6.rejordbe.application.challenge.read;

import com.dev6.rejordbe.domain.challenge.Challenge;
import com.dev6.rejordbe.domain.challenge.dto.ChallengeResult;
import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.exception.ChallengeNotFoundException;
import com.dev6.rejordbe.infrastructure.challenge.read.ReadChallengeRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * ReadChallengeServiceImpl
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@lombok.RequiredArgsConstructor
public class ReadChallengeServiceImpl implements ReadChallengeService {

    private final ReadChallengeRepository readChallengeRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ChallengeResult> findChallengeByFlag(@NonNull final Boolean flag) {
        Optional<Challenge> challengeOptional = readChallengeRepository.findChallengeByFlag(flag);

        if (challengeOptional.isEmpty()) {
            throw new ChallengeNotFoundException(ExceptionCode.CHALLENGE_NOT_FOUND.name());
        }

        return challengeOptional.map(anChallenge ->
                ChallengeResult.builder()
                        .challengeId(anChallenge.getChallengeId())
                        .title(anChallenge.getTitle())
                        .contents(anChallenge.getContents())
                        .badgeId(anChallenge.getBadgeId())
                        .badgeName(anChallenge.getBadgeName())
                        .footprintAmount(anChallenge.getFootprintAmount())
                        .imgFront(anChallenge.getImgFront())
                        .imgBack(anChallenge.getImgBack())
                        .textColor(anChallenge.getTextColor())
                        .build());
    }
}
