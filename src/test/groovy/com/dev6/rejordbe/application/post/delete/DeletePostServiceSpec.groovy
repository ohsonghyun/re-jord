package com.dev6.rejordbe.application.post.delete

import com.dev6.rejordbe.domain.post.Post
import com.dev6.rejordbe.domain.user.Users
import com.dev6.rejordbe.exception.IllegalParameterException
import com.dev6.rejordbe.exception.PostNotFoundException
import com.dev6.rejordbe.exception.UnauthorizedUserException
import com.dev6.rejordbe.infrastructure.post.delete.DeletePostRepository
import spock.lang.Specification
import spock.lang.Unroll

/**
 * DeletePostServiceSpec
 */
class DeletePostServiceSpec extends Specification {

    private DeletePostServiceImpl deletePostService
    private DeletePostRepository deletePostRepository

    def setup() {
        deletePostRepository = Mock(DeletePostRepository)
        deletePostService = new DeletePostServiceImpl(deletePostRepository)
    }

    // 게시글 삭제 관련
    def "게시글을 삭제할 수 있다"() {
        given:
        def anUser = Users.builder()
                .uid('uid')
                .build()

        def anPost = Post.builder()
                .postId('postId')
                .user(anUser)
                .build()

        // mock
        deletePostRepository.findById(_ as String) >> Optional.of(anPost)

        when:
        def postId = deletePostService.deletePost('postId', 'uid')

        then:
        anPost.postId == postId
    }

    @Unroll
    def "실패 케이스: #testCase 반환"() {
        given:
        def anUser = Users.builder()
                .uid('uid')
                .build()

        def anPost = Post.builder()
                .postId('postId')
                .user(anUser)
                .build()

        // mock
        deletePostRepository.findById(_ as String) >> Optional.of(anPost)

        when:
        deletePostService.deletePost(postId, uid)

        then:
        thrown(exception)

        where:
        testCase                    | exception                 | postId    | uid
        'postId가 정책에 어긋나는 경우' | IllegalParameterException | ''        | 'uid'
        'postId가 정책에 어긋나는 경우' | IllegalParameterException | null      | 'uid'
        '권한이 없는 유저가 접근한 경우' | UnauthorizedUserException | 'postId1' | 'uid1'
    }

    def "존재하지 않는 게시글일 경우"() {
        given:
        // mock
        deletePostRepository.findById(_ as String) >> Optional.empty()

        when:
        deletePostService.deletePost('postId', 'uid')

        then:
        thrown(PostNotFoundException)
    }
    // / 게시글 삭제 관련
}
