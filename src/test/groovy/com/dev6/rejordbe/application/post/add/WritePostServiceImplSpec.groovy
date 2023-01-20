package com.dev6.rejordbe.application.post.add

import com.dev6.rejordbe.application.id.IdGenerator
import com.dev6.rejordbe.domain.post.Post
import com.dev6.rejordbe.domain.post.PostType
import com.dev6.rejordbe.domain.post.ReviewType
import com.dev6.rejordbe.domain.user.Users
import com.dev6.rejordbe.exception.IllegalParameterException
import com.dev6.rejordbe.infrastructure.post.PostRepository
import spock.lang.Specification

/**
 * WritePostServiceImplSpec
 */
class WritePostServiceImplSpec extends Specification {

    WritePostService writePostService
    PostRepository postRepository
    IdGenerator idGenerator

    def setup() {
        postRepository = Mock(PostRepository.class)
        idGenerator = Mock(IdGenerator.class)
        writePostService = new WritePostServiceImpl(postRepository, idGenerator)
    }

    def "에러가 없는 경우 게시물을 등록할 수 있다"() {
        def anUser = Users.builder()
                .uid(uid)
                .build()

        postRepository.save(_ as Post) >> Post.builder()
                .postId(postId)
                .contents(contents)
                .postType(postType)
                .reviewType(reviewType)
                .user(anUser)
                .build()

        when:
        def saveResult = writePostService.writePost(
                Post.builder()
                        .postId(postId)
                        .contents(contents)
                        .postType(postType)
                        .reviewType(reviewType)
                        .user(anUser)
                        .build())

        then:
        saveResult.getPostId() == postId
        saveResult.getContents() == contents
        saveResult.getPostType() == postType
        saveResult.getReviewType() == reviewType
        saveResult.getUid() == uid

        where:
        postId   | contents   |  postType          | reviewType         | uid
        'postId' | 'contents' | PostType.CHALLENGE | ReviewType.FEELING | 'uid'
    }

    def "필수 입력값 content가 비어있으면 에러"() {
        def anUser = Users.builder()
                .uid(uid)
                .build()

        postRepository.save(_ as Post) >> Post.builder()
                .postId(postId)
                .contents(contents)
                .postType(postType)
                .reviewType(reviewType)
                .user(anUser)
                .build()

        when:
        def saveResult = writePostService.writePost(
                Post.builder()
                        .postId(postId)
                        .contents(contents)
                        .postType(postType)
                        .reviewType(reviewType)
                        .user(anUser)
                        .build())

        then:
        thrown(IllegalParameterException)

        where:
        postId   | contents   |  postType          | reviewType         | uid
        'postId' | ''         | PostType.CHALLENGE | ReviewType.FEELING | 'uid'
        'postId' | '  '       | PostType.CHALLENGE | ReviewType.FEELING | 'uid'

    }

}
