package com.dev6.rejordbe.presentation.controller;

import com.dev6.rejordbe.domain.badge.BadgeAcquirementType;
import com.dev6.rejordbe.domain.badge.BadgeCode;
import com.dev6.rejordbe.domain.challenge.Challenge;
import com.dev6.rejordbe.domain.challenge.ChallengeFlagType;
import com.dev6.rejordbe.domain.challengeReview.ChallengeReview;
import com.dev6.rejordbe.domain.challengeReview.ChallengeReviewType;
import com.dev6.rejordbe.domain.post.Post;
import com.dev6.rejordbe.domain.post.PostType;
import com.dev6.rejordbe.domain.user.Users;
import com.dev6.rejordbe.domain.role.Role;
import com.dev6.rejordbe.domain.role.RoleType;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.IntStream;

/**
 * InitData
 */
@Profile("local")
@Component
@lombok.RequiredArgsConstructor
public class InitData {

    private final InitRoles initRoles;
    private final InitUsers initUsers;
    private final InitPosts initPosts;
    private final InitChallengeReviews initChallengeReviews;
    private final InitChallenge initChallenge;

    @PostConstruct
    public void init() {
        initRoles.init();
        initUsers.init();
        initPosts.init();
        initChallengeReviews.init();
        initChallenge.init();
    }

    // ROLES ---------------------------------------------------
    @Component
    static class InitRoles {
        @PersistenceContext
        EntityManager em;

        @Transactional
        public void init() {
            em.persist(new Role(RoleType.ROLE_ADMIN));
            em.persist(new Role(RoleType.ROLE_USER));
        }
    }

    // USERS ---------------------------------------------------
    @Component
    static class InitUsers {
        @PersistenceContext
        EntityManager em;

        @Transactional
        public void init() {
            List<Role> roles = em.createQuery("select r from Role r where r.name='ROLE_USER'", Role.class).getResultList();
            em.persist(
                    Users.builder()
                            .uid("web-uid")
                            .userId("web-client")
                            .nickname("웹유저")
                            .password("$2a$10$SOm1ZyPwzhJ2.RwpQob0defjIKe.aD6BRv.Rye6VYeVdHrpFUKQZm") // password
                            .roles(roles)
                            .build()
            );
            em.persist(
                    Users.builder()
                            .uid("android-uid")
                            .userId("android-client")
                            .nickname("안드로이드유저")
                            .password("$2a$10$SOm1ZyPwzhJ2.RwpQob0defjIKe.aD6BRv.Rye6VYeVdHrpFUKQZm") // password
                            .roles(roles)
                            .build()
            );
            em.persist(
                    Users.builder()
                            .uid("ios-uid")
                            .userId("ios-client")
                            .nickname("아이폰 유저")
                            .password("$2a$10$SOm1ZyPwzhJ2.RwpQob0defjIKe.aD6BRv.Rye6VYeVdHrpFUKQZm") // password
                            .roles(roles)
                            .build()
            );
        }
    }

    @Component
    static class InitPosts {
        @PersistenceContext
        EntityManager em;

        @Transactional
        public void init() {
            // WEB 유저 게시글
            {
                Users user = (Users) em.createQuery("select u from Users u where u.uid='web-uid'").getSingleResult();
                IntStream.range(0, 7).forEach(idx -> {
                    em.persist(
                            Post.builder()
                                    .postId("PS_webclient" + idx)
                                    .contents("hello world web" + idx)
                                    .postType(
                                            idx % 2 == 0
                                                    ? PostType.OTHERS
                                                    : PostType.SHARE
                                    )
                                    .user(user)
                                    .build()
                    );
                });
            }
            // 안드로이드 유저 게시글
            {
                Users user = (Users) em.createQuery("select u from Users u where u.uid='android-uid'").getSingleResult();
                IntStream.range(0, 10).forEach(idx -> {
                    em.persist(
                            Post.builder()
                                    .postId("PS_androidclient" + idx)
                                    .contents("hello world android" + idx)
                                    .postType(
                                            idx % 2 == 0
                                                    ? PostType.OTHERS
                                                    : PostType.SHARE
                                    )
                                    .user(user)
                                    .build()
                    );
                });
            }
            // iOS 유저 게시글
            {
                Users user = (Users) em.createQuery("select u from Users u where u.uid='ios-uid'").getSingleResult();
                IntStream.range(0, 9).forEach(idx -> {
                    em.persist(
                            Post.builder()
                                    .postId("PS_iosclient" + idx)
                                    .contents("hello world ios" + idx)
                                    .postType(
                                            idx % 2 == 0
                                                    ? PostType.OTHERS
                                                    : PostType.SHARE
                                    )
                                    .user(user)
                                    .build()
                    );
                });
            }
        }
    }

    @Component
    static class InitChallengeReviews {
        @PersistenceContext
        EntityManager em;

        @Transactional
        public void init() {
            // WEB 유저 게시글
            {
                Users user = (Users) em.createQuery("select u from Users u where u.uid='web-uid'").getSingleResult();
                IntStream.range(0, 7).forEach(idx -> {
                    em.persist(
                            ChallengeReview.builder()
                                    .challengeReviewId("CR_webclient" + idx)
                                    .contents("hello world web" + idx)
                                    .challengeReviewType(ChallengeReviewType.HARDSHIP)
                                    .user(user)
                                    .build()
                    );
                });
            }
            // 안드로이드 유저 게시글
            {
                Users user = (Users) em.createQuery("select u from Users u where u.uid='android-uid'").getSingleResult();
                IntStream.range(0, 10).forEach(idx -> {
                    em.persist(
                            ChallengeReview.builder()
                                    .challengeReviewId("CR_androidclient" + idx)
                                    .contents("hello world android" + idx)
                                    .challengeReviewType(ChallengeReviewType.HARDSHIP)
                                    .user(user)
                                    .build()
                    );
                });
            }
            // iOS 유저 게시글
            {
                Users user = (Users) em.createQuery("select u from Users u where u.uid='ios-uid'").getSingleResult();
                IntStream.range(0, 9).forEach(idx -> {
                    em.persist(
                            ChallengeReview.builder()
                                    .challengeReviewId("CR_iosclient" + idx)
                                    .contents("hello world ios" + idx)
                                    .challengeReviewType(ChallengeReviewType.HARDSHIP)
                                    .user(user)
                                    .build()
                    );
                });
            }
        }
    }

    @Component
    static class InitChallenge {
        @PersistenceContext
        EntityManager em;

        @Transactional
        public void init() {
            {
                em.persist(new Challenge("CH0", "찬 물로 세탁하기", "오늘은 찬물로 세탁기를 돌리고 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.WATER_FAIRY, "imgFront0", "imgBack0", "#2894DE", ChallengeFlagType.TODAY));
                em.persist(new Challenge("CH1", "샤워 10분 안에 끝내기", "오늘은 샤워를 10분 안에 끝내고 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.WATER_FAIRY, "imgFront0", "imgBack0", "#2894DE", ChallengeFlagType.NOT_TODAY));
                em.persist(new Challenge("CH2", "물 받아서 설거지하기", "오늘은 물 받아서 설거지하고 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.WATER_FAIRY, "imgFront0", "imgBack0", "#2894DE", ChallengeFlagType.NOT_TODAY));
                em.persist(new Challenge("CH3", "물 받아서 세안하기", "오늘은 물 받아서 세수하고 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.WATER_FAIRY, "imgFront0", "imgBack0", "#2894DE", ChallengeFlagType.NOT_TODAY));
                em.persist(new Challenge("CH4", "물 받아서 양치하기", "오늘은 물 받아서 양치하고 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.WATER_FAIRY, "imgFront0", "imgBack0", "#2894DE", ChallengeFlagType.NOT_TODAY));
                em.persist(new Challenge("CH5", "비누 사용시 물 잠그기", "오늘은 비누를 사용할 때 물을 잠그고 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.WATER_FAIRY, "imgFront0", "imgBack0", "#2894DE", ChallengeFlagType.NOT_TODAY));
                em.persist(new Challenge("CH6", "유제품 섭취 안 하기", "오늘은 유제품을 섭취하지 않고 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.VEGETARIAN, "imgFront0", "imgBack0", "#419A2B", ChallengeFlagType.NOT_TODAY));
                em.persist(new Challenge("CH7", "한 끼 채식하기", "오늘은 한 끼 채식하기로 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.VEGETARIAN, "imgFront0", "imgBack0", "#419A2B", ChallengeFlagType.NOT_TODAY));
                em.persist(new Challenge("CH8", "잔반 남기지 않기", "음식은 먹을 만큼만! 잔반 없는 식사로 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.MEAL_PLANNER, "imgFront0", "imgBack0", "#419A2B", ChallengeFlagType.NOT_TODAY));
                em.persist(new Challenge("CH9", "식단 계획하고 장보기", "식단에 맞춰 필요한 재료를 구매하고 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.MEAL_PLANNER, "imgFront0", "imgBack0", "#419A2B", ChallengeFlagType.NOT_TODAY));
                em.persist(new Challenge("CH10", "내일 도시락 준비하기", "내일 먹을 도시락을 미리 준비하고 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.MEAL_PLANNER, "imgFront0", "imgBack0", "#419A2B", ChallengeFlagType.NOT_TODAY));
                em.persist(new Challenge("CH11", "재래시장에서 장보기", "오늘은 재래시장에서 장보기로 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.MEAL_PLANNER, "imgFront0", "imgBack0", "#419A2B", ChallengeFlagType.NOT_TODAY));
                em.persist(new Challenge("CH12", "전기밥솥 보온 끄기", "오늘은 밥솥을 비우고 보온 끄기로 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.ENERGY_SAVER, "imgFront0", "imgBack0", "#EC7F00", ChallengeFlagType.NOT_TODAY));
                em.persist(new Challenge("CH13", "안 쓰는 플러그 뽑기", "오늘은 플러그를 뽑고 외출하기로 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.ENERGY_SAVER, "imgFront0", "imgBack0", "#EC7F00", ChallengeFlagType.NOT_TODAY));
                em.persist(new Challenge("CH14", "냉장고 비우기", "오늘은 냉장 & 냉동고를 비우고 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.ENERGY_SAVER, "imgFront0", "imgBack0", "#EC7F00", ChallengeFlagType.NOT_TODAY));
                em.persist(new Challenge("CH15", "TV 시청 시간 줄이기", "오늘은 TV 시청 시간을 줄이고 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.ENERGY_SAVER, "imgFront0", "imgBack0", "#EC7F00", ChallengeFlagType.NOT_TODAY));
                em.persist(new Challenge("CH16", "소등하고 외출하기", "오늘은 외출할 때 소등하기로 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.ENERGY_SAVER, "imgFront0", "imgBack0", "#EC7F00", ChallengeFlagType.NOT_TODAY));
                em.persist(new Challenge("CH17", "이메일함 정리하기", "오늘은 불필요한 이메일을 정리하고 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.DIGITAL_FAIRY, "imgFront0", "imgBack0", "#EC7F00", ChallengeFlagType.NOT_TODAY));
                em.persist(new Challenge("CH18", "보호모드 이용하기", "오늘은 보호모드로 인터넷을 이용하고 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.DIGITAL_FAIRY, "imgFront0", "imgBack0", "#EC7F00", ChallengeFlagType.NOT_TODAY));
                em.persist(new Challenge("CH19", "이면지 사용하기", "오늘은 이면지를 대신 사용하고 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.FOREST_LOVER, "imgFront0", "imgBack0", "#419A2B", ChallengeFlagType.NOT_TODAY));
                em.persist(new Challenge("CH20", "손수건 이용하기", "오늘은 휴지 대신 손수건을 이용하고 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.FOREST_LOVER, "imgFront0", "imgBack0", "#419A2B", ChallengeFlagType.NOT_TODAY));
                em.persist(new Challenge("CH21", "공원에서 쓰레기 줍기", "오늘은 산책하면서 쓰레기 줍기로 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.FOREST_LOVER, "imgFront0", "imgBack0", "#419A2B", ChallengeFlagType.NOT_TODAY));
                em.persist(new Challenge("CH22", "모바일 영수증 받기", "지류 영수증 대신 모바일 영수증으로 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.FOREST_LOVER, "imgFront0", "imgBack0", "#419A2B", ChallengeFlagType.NOT_TODAY));
                em.persist(new Challenge("CH23", "친환경 제품 구매하기", "오늘 쇼핑은 친환경 제품으로! 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.PRO_SHOPPER, "imgFront0", "imgBack0", "#D95E43", ChallengeFlagType.NOT_TODAY));
                em.persist(new Challenge("CH24", "장바구니 이용하기", "오늘은 장바구니를 이용하고 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.ENVIRONMENTAL_EXPERT, "imgFront0", "imgBack0", "#D95E43", ChallengeFlagType.NOT_TODAY));
                em.persist(new Challenge("CH25", "텀블러 이용하기", "텀블러나 다회용 컵을 이용하고 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.ENVIRONMENTAL_EXPERT, "imgFront0", "imgBack0", "#D95E43", ChallengeFlagType.NOT_TODAY));
                em.persist(new Challenge("CH26", "적정 실내 온도 유지하기", "냉난방 온도 1 ℃만 조절하고 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.ENVIRONMENTAL_EXPERT, "imgFront0", "imgBack0", "#D95E43", ChallengeFlagType.NOT_TODAY));
                em.persist(new Challenge("CH27", "재활용품 분리배출하기", "오늘은 재활용품을 분리배출하고 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.ENVIRONMENTAL_EXPERT, "imgFront0", "imgBack0", "#D95E43", ChallengeFlagType.NOT_TODAY));
                em.persist(new Challenge("CH28", "에코백 이용하기", "오늘은 에코백을 이용하고 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.ENVIRONMENTAL_EXPERT, "imgFront0", "imgBack0", "#D95E43", ChallengeFlagType.NOT_TODAY));
                em.persist(new Challenge("CH29", "대중교톻 이용하기", "오늘은 대중교통을 이용하고 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.PRO_ACTIVATE, "imgFront0", "imgBack0", "#D95E43", ChallengeFlagType.NOT_TODAY));
                em.persist(new Challenge("CH30", "만보 걷기", "오늘은 교통수단 대신 만보 걷기로 탄소발자국 줄이기에 동참해 봐요.", 15, BadgeCode.PRO_ACTIVATE, "imgFront0", "imgBack0", "#D95E43", ChallengeFlagType.NOT_TODAY));
                em.persist(
                        Challenge.builder()
                                .challengeId("CHDefault")
                                .title("title")
                                .contents("hello world")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.DEFAULT)
                                .imgFront("imgFront")
                                .imgBack("imgBack")
                                .textColor("#000000")
                                .flag(ChallengeFlagType.DEFAULT)
                                .build());
            }
        }
    }
}
