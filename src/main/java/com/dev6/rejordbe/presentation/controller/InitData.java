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
                em.persist(
                        Challenge.builder()
                                .challengeId("CH0")
                                .title("찬 물로 세탁하기")
                                .contents("오늘은 찬물로 세탁기를 돌리고 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.WATER_FAIRY)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("2894DE")
                                .flag(ChallengeFlagType.TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CH1")
                                .title("샤워 10분 안에 끝내기")
                                .contents("오늘은 샤워를 10분 안에 끝내고 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.WATER_FAIRY)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("2894DE")
                                .flag(ChallengeFlagType.NOT_TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CH2")
                                .title("물 받아서 설거지하기")
                                .contents("오늘은 물 받아서 설거지하고 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.WATER_FAIRY)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("2894DE")
                                .flag(ChallengeFlagType.NOT_TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CH3")
                                .title("물 받아서 세안하기")
                                .contents("오늘은 물 받아서 세수하고 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.WATER_FAIRY)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("2894DE")
                                .flag(ChallengeFlagType.NOT_TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CH4")
                                .title("물 받아서 양치하기")
                                .contents("오늘은 물 받아서 양치하고 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.WATER_FAIRY)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("2894DE")
                                .flag(ChallengeFlagType.NOT_TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CH5")
                                .title("비누 사용시 물 잠그기")
                                .contents("오늘은 비누를 사용할 때 물을 잠그고 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.WATER_FAIRY)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("2894DE")
                                .flag(ChallengeFlagType.NOT_TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CH6")
                                .title("유제품 섭취 안 하기")
                                .contents("오늘은 유제품을 섭취하지 않고 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.VEGETARIAN)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("419A2B")
                                .flag(ChallengeFlagType.NOT_TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CH7")
                                .title("한 끼 채식하기")
                                .contents("오늘은 한 끼 채식하기로 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.VEGETARIAN)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("419A2B")
                                .flag(ChallengeFlagType.NOT_TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CH8")
                                .title("잔반 남기지 않기")
                                .contents("음식은 먹을 만큼만! 잔반 없는 식사로 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.MEAL_PLANNER)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("419A2B")
                                .flag(ChallengeFlagType.NOT_TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CH9")
                                .title("식단 계획하고 장보기")
                                .contents("식단에 맞춰 필요한 재료를 구매하고 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.MEAL_PLANNER)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("419A2B")
                                .flag(ChallengeFlagType.NOT_TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CH10")
                                .title("내일 도시락 준비하기")
                                .contents("내일 먹을 도시락을 미리 준비하고 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.MEAL_PLANNER)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("419A2B")
                                .flag(ChallengeFlagType.NOT_TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CH11")
                                .title("재래시장에서 장보기")
                                .contents("오늘은 재래시장에서 장보기로 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.MEAL_PLANNER)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("419A2B")
                                .flag(ChallengeFlagType.NOT_TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CH12")
                                .title("전기밥솥 보온 끄기")
                                .contents("오늘은 밥솥을 비우고 보온 끄기로 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.ENERGY_SAVER)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("EC7F00")
                                .flag(ChallengeFlagType.NOT_TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CH13")
                                .title("안 쓰는 플러그 뽑기")
                                .contents("오늘은 플러그를 뽑고 외출하기로 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.ENERGY_SAVER)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("EC7F00")
                                .flag(ChallengeFlagType.NOT_TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CH14")
                                .title("냉장고 비우기")
                                .contents("오늘은 냉장 & 냉동고를 비우고 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.ENERGY_SAVER)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("EC7F00")
                                .flag(ChallengeFlagType.NOT_TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CH15")
                                .title("TV 시청 시간 줄이기")
                                .contents("오늘은 TV 시청 시간을 줄이고 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.ENERGY_SAVER)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("EC7F00")
                                .flag(ChallengeFlagType.NOT_TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CH16")
                                .title("소등하고 외출하기")
                                .contents("오늘은 외출할 때 소등하기로 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.ENERGY_SAVER)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("EC7F00")
                                .flag(ChallengeFlagType.NOT_TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CH17")
                                .title("이메일함 정리하기")
                                .contents("오늘은 불필요한 이메일을 정리하고 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.DIGITAL_FAIRY)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("EC7F00")
                                .flag(ChallengeFlagType.NOT_TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CH18")
                                .title("보호모드 이용하기")
                                .contents("오늘은 보호모드로 인터넷을 이용하고 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.DIGITAL_FAIRY)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("EC7F00")
                                .flag(ChallengeFlagType.NOT_TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CH19")
                                .title("이면지 사용하기")
                                .contents("오늘은 이면지를 대신 사용하고 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.FOREST_LOVER)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("419A2B")
                                .flag(ChallengeFlagType.NOT_TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CH20")
                                .title("손수건 이용하기")
                                .contents("오늘은 휴지 대신 손수건을 이용하고 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.FOREST_LOVER)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("419A2B")
                                .flag(ChallengeFlagType.NOT_TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CH21")
                                .title("공원에서 쓰레기 줍기")
                                .contents("오늘은 산책하면서 쓰레기 줍기로 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.FOREST_LOVER)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("419A2B")
                                .flag(ChallengeFlagType.NOT_TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CH22")
                                .title("모바일 영수증 받기")
                                .contents("지류 영수증 대신 모바일 영수증으로 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.FOREST_LOVER)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("419A2B")
                                .flag(ChallengeFlagType.NOT_TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CH23")
                                .title("친환경 제품 구매하기")
                                .contents("오늘 쇼핑은 친환경 제품으로! 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.PRO_SHOPPER)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("D95E43")
                                .flag(ChallengeFlagType.NOT_TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CH24")
                                .title("장바구니 이용하기")
                                .contents("오늘은 장바구니를 이용하고 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.ENVIRONMENTAL_EXPERT)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("D95E43")
                                .flag(ChallengeFlagType.NOT_TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CH25")
                                .title("텀블러 이용하기")
                                .contents("텀블러나 다회용 컵을 이용하고 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.ENVIRONMENTAL_EXPERT)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("D95E43")
                                .flag(ChallengeFlagType.NOT_TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CH26")
                                .title("적정 실내 온도 유지하기")
                                .contents("냉난방 온도 1 ℃만 조절하고 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.ENVIRONMENTAL_EXPERT)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("D95E43")
                                .flag(ChallengeFlagType.NOT_TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CH27")
                                .title("재활용품 분리배출하기")
                                .contents("오늘은 재활용품을 분리배출하고 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.ENVIRONMENTAL_EXPERT)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("D95E43")
                                .flag(ChallengeFlagType.NOT_TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CH28")
                                .title("에코백 이용하기")
                                .contents("오늘은 에코백을 이용하고 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.ENVIRONMENTAL_EXPERT)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("D95E43")
                                .flag(ChallengeFlagType.NOT_TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CH29")
                                .title("대중교톻 이용하기")
                                .contents("오늘은 대중교통을 이용하고 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.PRO_ACTIVATE)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("D95E43")
                                .flag(ChallengeFlagType.NOT_TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CH30")
                                .title("만보 걷기")
                                .contents("오늘은 교통수단 대신 만보 걷기로 탄소발자국 줄이기에 동참해 봐요.")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.PRO_ACTIVATE)
                                .imgFront("imgFront0")
                                .imgBack("imgBack0")
                                .textColor("D95E43")
                                .flag(ChallengeFlagType.NOT_TODAY)
                                .build()
                );
                em.persist(
                        Challenge.builder()
                                .challengeId("CHDefault")
                                .title("title")
                                .contents("hello world")
                                .footprintAmount(15)
                                .badgeCode(BadgeCode.DEFAULT)
                                .imgFront("imgFront")
                                .imgBack("imgBack")
                                .textColor("textColor")
                                .flag(ChallengeFlagType.DEFAULT)
                                .build());
            }
        }
    }
}
