package com.dev6.rejordbe.application.post.add

import com.dev6.rejordbe.application.id.IdGenerator
import com.dev6.rejordbe.domain.post.Post
import com.dev6.rejordbe.domain.post.PostType
import com.dev6.rejordbe.domain.user.Users
import com.dev6.rejordbe.exception.IllegalParameterException
import com.dev6.rejordbe.exception.UserNotFoundException
import com.dev6.rejordbe.infrastructure.post.add.WritePostRepository
import com.dev6.rejordbe.infrastructure.user.UserInfoRepository
import spock.lang.Specification

/**
 * WritePostServiceImplSpec
 */
class WritePostServiceImplSpec extends Specification {

    WritePostService writePostService
    WritePostRepository writePostRepository
    UserInfoRepository userInfoRepository
    IdGenerator idGenerator

    def setup() {
        writePostRepository = Mock(WritePostRepository.class)
        idGenerator = Mock(IdGenerator.class)
        userInfoRepository = Mock(UserInfoRepository.class)
        writePostService = new WritePostServiceImpl(writePostRepository, idGenerator, userInfoRepository)
    }

    def "에러가 없는 경우 게시물을 등록할 수 있다"() {
        def anUser = Users.builder()
                        .uid(uid)
                        .build()

        userInfoRepository.findById(uid) >> Optional.of(anUser)

        writePostRepository.save(_ as Post) >> Post.builder()
                .postId(postId)
                .contents(contents)
                .postType(postType)
                .user(anUser)
                .build()

        when:
        def saveResult = writePostService.writePost(
                Post.builder()
                        .postId(postId)
                        .contents(contents)
                        .postType(postType)
                        .user(anUser)
                        .build(), uid)

        then:
        saveResult.getPostId() == postId
        saveResult.getContents() == contents
        saveResult.getPostType() == postType
        saveResult.getUid() == uid

        where:
        postId   | contents   |  postType          | uid
        'postId' | 'contents' | PostType.SHARE     | 'uid'
    }

    def "존재하지 않는 유저면 에러"() {
        def anUser = Users.builder()
                .uid(uid)
                .build()

        userInfoRepository.findById(uid) >> Optional.empty()

        writePostRepository.save(_ as Post) >> Post.builder()
                .postId(postId)
                .contents(contents)
                .postType(postType)
                .user(anUser)
                .build()

        when:
        def saveResult = writePostService.writePost(
                Post.builder()
                        .postId(postId)
                        .contents(contents)
                        .postType(postType)
                        .user(anUser)
                        .build(), uid)

        then:
        thrown(UserNotFoundException)

        where:
        postId   | contents   | postType           | uid
        'postId' | 'contents' | PostType.SHARE     | 'uid'
        'postId' | 'contents' | PostType.SHARE     | 'uid'
    }

    def "필수 입력값 content가 비어있으면 에러"() {
        def anUser = Users.builder()
                .uid(uid)
                .build()

        userInfoRepository.findById(uid) >> Optional.of(anUser)

        writePostRepository.save(_ as Post) >> Post.builder()
                .postId(postId)
                .contents(contents)
                .postType(postType)
                .user(anUser)
                .build()

        when:
        def saveResult = writePostService.writePost(
                Post.builder()
                        .postId(postId)
                        .contents(contents)
                        .postType(postType)
                        .user(anUser)
                        .build(), uid)

        then:
        thrown(IllegalParameterException)

        where:
        postId   | contents   | postType           | uid
        'postId' | ''         | PostType.SHARE     | 'uid'
        'postId' | '  '       | PostType.SHARE     | 'uid'
        'postId' | null       | PostType.SHARE     | 'uid'
    }

}
