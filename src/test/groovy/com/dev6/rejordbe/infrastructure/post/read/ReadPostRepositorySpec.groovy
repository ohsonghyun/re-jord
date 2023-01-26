package com.dev6.rejordbe.infrastructure.post.read

import com.dev6.rejordbe.TestConfig
import com.dev6.rejordbe.domain.post.Post
import com.dev6.rejordbe.domain.post.PostType
import com.dev6.rejordbe.domain.post.dto.PostResult
import com.dev6.rejordbe.domain.user.Users
import com.dev6.rejordbe.infrastructure.user.SignUpRepository
import org.assertj.core.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import java.time.LocalDateTime

/**
 * ReadPostRepositorySpec
 */
@DataJpaTest
@EnableJpaAuditing
@Import(TestConfig.class)
class ReadPostRepositorySpec extends Specification {

    @PersistenceContext
    EntityManager entityManager

    @Autowired
    SignUpRepository signUpRepository
    @Autowired
    ReadPostRepository readPostRepository

    def "특정시간 이전의 게시글을 최신글 순으로 취득할 수 있다: 데이터가 있는 경우"() {
        given:
        // 유저 생성
        def user = Users.builder()
                .uid('uid')
                .nickname('nickname')
                .build()
        signUpRepository.save(user)

        // 게시글 추가
        for (int i = 0; i < 10; i++) {
            readPostRepository.save(
                    Post.builder()
                            .postId('postId' + i)
                            .contents('contents')
                            .postType(PostType.SHARE)
                            .user(user)
                            .build()
            )
        }

        entityManager.flush()
        entityManager.clear()

        when:
        // 데이터 입력 후 시간
        def now = LocalDateTime.now()
        def allPosts = readPostRepository.searchAll(now, PageRequest.of(0, 10))

        entityManager.flush()
        entityManager.clear()

        then:
        allPosts.getContent().size() == 10
        allPosts.getContent().get(0) instanceof PostResult
        def it = Assertions.assertThat(allPosts.getContent())
        // 최신글 순으로 반환
        it.extractingResultOf('getPostId').containsExactly(
                'postId9',
                'postId8',
                'postId7',
                'postId6',
                'postId5',
                'postId4',
                'postId3',
                'postId2',
                'postId1',
                'postId0'
        )
        it.extractingResultOf('getContents').containsOnly('contents')
        it.extractingResultOf('getUid').containsOnly('uid')
        it.extractingResultOf('getNickname').containsOnly('nickname')
        it.extractingResultOf('getCreatedDate').isNotEmpty()
    }

    def "특정시간 이전의 게시글 취득할 수 있다: 데이터가 없는 경우"() {
        // 데이터 입력 전 시간
        def now = LocalDateTime.now()
        // 유저 생성
        def user = Users.builder()
                .uid('uid')
                .build()
        signUpRepository.save(user)

        // 게시글 추가
        for (int i = 0; i < 10; i++) {
            readPostRepository.save(
                    Post.builder()
                            .postId('postId' + i)
                            .contents('contents')
                            .postType(PostType.SHARE)
                            .user(user)
                            .build()
            )
        }

        entityManager.flush()
        entityManager.clear()

        when:
        def allPosts = readPostRepository.searchAll(now, PageRequest.of(0, 10))

        entityManager.flush()
        entityManager.clear()

        then:
        allPosts.getContent().size() == 0
    }

}
