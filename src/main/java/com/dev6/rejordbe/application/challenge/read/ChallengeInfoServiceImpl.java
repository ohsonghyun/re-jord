package com.dev6.rejordbe.application.challenge.read;

import com.dev6.rejordbe.application.scheduler.ChallengeScheduler;
import com.dev6.rejordbe.domain.challenge.Challenge;
import com.dev6.rejordbe.domain.challenge.dto.ChallengeResult;
import com.dev6.rejordbe.infrastructure.challenge.read.ReadChallengeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * ChallengeInfoServiceImpl
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@lombok.RequiredArgsConstructor
public class ChallengeInfoServiceImpl implements ChallengeInfoService {

    private final ReadChallengeRepository readChallengeRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<ChallengeResult> findChallengeByFlag() {
        Optional<Challenge> challengeOptional = readChallengeRepository.findChallengeByFlag(true);

        // TODO 디폴트 값 어떻게 보낼지 고민하기기
       if (challengeOptional.isEmpty()) {
            return challengeOptional.map(anChallenge ->ChallengeResult.builder()
                    .challengeId("CHDefault")
                    .title("title")
                    .contents("contents")
                    .badgeId("badgeId")
                    .badgeName("badgeName")
                    .footprintAmount(15)
                    .imgFront("imgFront")
                    .imgBack("imgBack")
                    .textColor("textColor")
                    .build());
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
