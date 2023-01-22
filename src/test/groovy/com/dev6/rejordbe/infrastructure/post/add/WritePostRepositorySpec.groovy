package com.dev6.rejordbe.infrastructure.post.add

import com.dev6.rejordbe.TestConfig
import com.dev6.rejordbe.domain.post.Post
import com.dev6.rejordbe.domain.post.PostType
import com.dev6.rejordbe.domain.post.ReviewType
import com.dev6.rejordbe.domain.user.Users
import com.dev6.rejordbe.infrastructure.user.SignUpRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import java.time.LocalDateTime

/**
 * PostRepositorySpec
 */
@DataJpaTest
@EnableJpaAuditing
@Import(TestConfig.class)
class WritePostRepositorySpec extends Specification {

    @PersistenceContext
    EntityManager entityManager

    @Autowired
    SignUpRepository signUpRepository
    @Autowired
    WritePostRepository writePostRepository

    def "게시글을 추가할 수 있다"() {
        given:
        // 현재 시간
        def now = LocalDateTime.now()
        // 유저 생성
        def user = Users.builder()
                .uid('uid')
                .build()
        signUpRepository.save(user)


        def newPost = Post.builder()
                .postId('postId')
                .contents('contents')
                .postType(PostType.CHALLENGE)
                .reviewType(ReviewType.FEELING)
                .user(user)
                .build()

        when:
        writePostRepository.save(newPost)

        entityManager.flush()
        entityManager.clear()

        then:
        def postOptional = writePostRepository.findById(newPost.getPostId())
        postOptional.isPresent()
        def aPost = postOptional.orElseThrow()
        aPost.getPostId() == newPost.getPostId()
        aPost.getContents() == newPost.getContents()
        aPost.getPostType() == newPost.getPostType()
        aPost.getReviewType() == newPost.getReviewType()
        aPost.getCreatedDate().isAfter(now)
        aPost.getModifiedDate().isAfter(now)
    }

}
