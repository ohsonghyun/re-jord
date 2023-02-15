package com.dev6.rejordbe.application.scheduler;

/**
 * ChallengeScheduler
 * <p>챌린지 스케줄링</p>
 */
public interface ChallengeScheduler {

    /**
     * 24시간 마다 새로운 챌린지 설정
     *
     * @param
     * @return
     */
    void run();
}
