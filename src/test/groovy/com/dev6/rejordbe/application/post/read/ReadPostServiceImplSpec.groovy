package com.dev6.rejordbe.application.post.read

import com.dev6.rejordbe.domain.post.PostType
import com.dev6.rejordbe.domain.post.dto.PostResult
import com.dev6.rejordbe.exception.IllegalParameterException
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

    def "기준 시각 이후의 모든 게시글 정보를 획득할 수 있다"() {
        given:
        def now = LocalDateTime.now()
        def pageRequest = PageRequest.of(0, 5)
        readPostRepository.searchAll(_ as LocalDateTime, _ as Pageable)
                >> new PageImpl<PostResult>(
                List.of(
                        new PostResult('postId1', 'content', PostType.OTHERS, 'uid1', 'nickname1', LocalDateTime.now()),
                        new PostResult('postId2', 'content', PostType.OTHERS, 'uid2', 'nickname2', LocalDateTime.now())
                ),
                pageRequest,
                1)

        when:
        def posts = readPostService.allPosts(now, pageRequest)

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
        readPostService.allPosts(null, PageRequest.of(0, 5))

        then:
        thrown(IllegalParameterException)
    }

    // 게시글 데이터의 유무에 대한 테스트는 ReadPostRepositorySpec 으로 대체
}
