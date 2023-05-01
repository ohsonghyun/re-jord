package com.dev6.rejordbe.application.post.read

import com.dev6.rejordbe.domain.post.Post
import com.dev6.rejordbe.domain.post.PostType
import com.dev6.rejordbe.domain.post.dto.PostResult
import com.dev6.rejordbe.domain.post.dto.SearchPostCond
import com.dev6.rejordbe.domain.user.Users
import com.dev6.rejordbe.exception.IllegalParameterException
import com.dev6.rejordbe.exception.PostNotFoundException
import com.dev6.rejordbe.infrastructure.post.read.ReadPostRepository
import org.assertj.core.api.Assertions
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import spock.lang.Specification

import java.time.LocalDateTime

/**
 * ReadPostServiceImplSpec
 */
class ReadPostServiceImplSpec extends Specification {

    private ReadPostServiceImpl readPostService
    private ReadPostRepository readPostRepository

    def setup() {
        readPostRepository = Mock(ReadPostRepository)
        readPostService = new ReadPostServiceImpl(readPostRepository)
    }

    // 모든 게시글 관련
    def "기준 시각 이후의 모든 게시글 정보를 획득할 수 있다"() {
        given:
        def now = LocalDateTime.now()
        def searchPostCond = new SearchPostCond(null)
        def pageRequest = PageRequest.of(0, 5)
        readPostRepository.searchAll(_ as LocalDateTime, _ as SearchPostCond, _ as Pageable)
                >> new PageImpl<PostResult>(
                List.of(
                        new PostResult('postId1', 'content', PostType.OTHERS, 'uid1', 'nickname1', LocalDateTime.now()),
                        new PostResult('postId2', 'content', PostType.OTHERS, 'uid2', 'nickname2', LocalDateTime.now())
                ),
                pageRequest,
                1)

        when:
        def posts = readPostService.allPosts(now, searchPostCond, pageRequest)

        then:
        posts.getTotalPages() == 1
        posts.getTotalElements() == 2
        def it = Assertions.assertThat(posts.getContent())
        it.extractingResultOf('getPostId').containsExactly('postId1', 'postId2')
        it.extractingResultOf('getContents').containsOnly('content')
        it.extractingResultOf('getPostType').containsOnly(PostType.OTHERS)
        it.extractingResultOf('getUid').containsExactly('uid1', 'uid2')
        it.extractingResultOf('getNickname').containsExactly('nickname1', 'nickname2')
        it.extractingResultOf('getCreatedDate').isNotEmpty()
    }

    def "기준 시각이 지정되지 않은 경우에는 에러: 400"() {
        when:
        readPostService.allPosts(null, new SearchPostCond(null), PageRequest.of(0, 5))

        then:
        thrown(IllegalParameterException)
    }
    // / 모든 게시글 관련

    // 게시글 데이터의 유무에 대한 테스트는 ReadPostRepositorySpec 으로 대체

    // uid가 일치하는 게시글 관련
    def "uid가 일치하는 모든 게시글 정보를 획득할 수 있다"() {
        given:
        def pageRequest = PageRequest.of(0, 5)
        readPostRepository.searchPostByUid(_ as String, _ as Pageable)
                >> new PageImpl<PostResult>(
                List.of(
                        new PostResult('postId1', 'content1', PostType.OTHERS, 'uid1', 'nickname1', LocalDateTime.now()),
                        new PostResult('postId2', 'content2', PostType.OTHERS, 'uid1', 'nickname1', LocalDateTime.now())
                ),
                pageRequest,
                1)

        when:
        def posts = readPostService.postsWrittenByUid('uid1', pageRequest)

        then:
        posts.getTotalPages() == 1
        posts.getTotalElements() == 2
        def it = Assertions.assertThat(posts.getContent())
        it.extractingResultOf('getPostId').containsExactly('postId1', 'postId2')
        it.extractingResultOf('getContents').containsExactly('content1', 'content2')
        it.extractingResultOf('getPostType').containsOnly(PostType.OTHERS)
        it.extractingResultOf('getUid').containsOnly('uid1')
        it.extractingResultOf('getNickname').containsOnly('nickname1')
        it.extractingResultOf('getCreatedDate').isNotEmpty()
    }

    def "uid가 지정되지 않은 경우에는 에러: 400"() {
        when:
        readPostService.postsWrittenByUid(null, PageRequest.of(0, 5))

        then:
        thrown(IllegalParameterException)
    }
    // / uid가 일치하는 게시글 관련


    // 게시글 수정 관련
    def "게시글 내용은 변경 가능하다"() {
        given:
        def anUser = Users.builder()
                .uid('uid')
                .build()

        def anPost = Post.builder()
                .contents(contents)
                .user(anUser)
                .build()

        // mock
        readPostRepository.findById(_) >> Optional.of(anPost)

        when:
        readPostService.updatePostInfo(Post.builder().contents(newContents).build())

        then:
        anPost.getContents() == newContents

        where:
        contents   | newContents
        'contents' | 'newContents'
    }

    def "새로운 게시글 내용이 비어있으면 에러: IllegalParameterException"() {
        given:
        def anPost = Post.builder()
                .contents(contents)
                .build()

        // mock
        readPostRepository.findById(_) >> Optional.of(anPost)

        when:
        readPostService.updatePostInfo(Post.builder().contents(newContents).build())

        then:
        thrown(IllegalParameterException)

        where:
        contents   | newContents
        'contents' | ''
        'contents' | ' '
        'contents' | null
    }

    def "내용을 수정할 게시글이 존재하니 않으면 에러: PostNotFoundException"() {
        given:
        // mock
        readPostRepository.findById(_) >> Optional.empty()

        when:
        readPostService.updatePostInfo(Post.builder().contents('contents').build())

        then:
        thrown(PostNotFoundException)
    }
    // / 게시글 수정 관련
}
