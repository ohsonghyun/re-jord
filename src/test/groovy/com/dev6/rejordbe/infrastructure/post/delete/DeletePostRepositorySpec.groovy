package com.dev6.rejordbe.infrastructure.post.delete

import com.dev6.rejordbe.TestConfig
import com.dev6.rejordbe.domain.post.Post
import com.dev6.rejordbe.domain.post.PostType
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

@DataJpaTest
@EnableJpaAuditing
@Import(TestConfig.class)
class DeletePostRepositorySpec extends Specification {

    @PersistenceContext
    EntityManager entityManager

    @Autowired
    SignUpRepository signUpRepository
    @Autowired
    DeletePostRepository deletePostRepository

    def "게시글을 삭제할 수 있다"() {
        given:
        // 유저 생성
        def user = Users.builder()
                .uid('uid')
                .build()
        signUpRepository.save(user)

        def newPost = Post.builder()
                .postId('postId')
                .contents('contents')
                .postType(PostType.SHARE)
                .user(user)
                .build()
        deletePostRepository.save(newPost)

        entityManager.flush()
        entityManager.clear()

        when:
        deletePostRepository.deleteByPostId('postId')

        entityManager.flush()
        entityManager.clear()

        then:
        def postOptional = deletePostRepository.findById(newPost.getPostId())
        postOptional == Optional.empty()
    }
}
