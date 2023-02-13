package com.dev6.rejordbe.presentation.controller;

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

    @PostConstruct
    public void init() {
        initRoles.init();
        initUsers.init();
        initPosts.init();
        initChallengeReviews.init();
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
                                    .postType(PostType.OTHERS)
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
                                    .postType(PostType.OTHERS)
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
                                    .postType(PostType.OTHERS)
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
                                    .challengeReviewType(ChallengeReviewType.FREE)
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
                                    .challengeReviewType(ChallengeReviewType.FREE)
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
                                    .challengeReviewType(ChallengeReviewType.FREE)
                                    .user(user)
                                    .build()
                    );
                });
            }
        }
    }

}
