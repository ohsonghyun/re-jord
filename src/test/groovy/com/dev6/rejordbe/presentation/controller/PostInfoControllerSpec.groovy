package com.dev6.rejordbe.presentation.controller

import com.dev6.rejordbe.TestSecurityConfig
import com.dev6.rejordbe.application.post.read.ReadPostService
import com.dev6.rejordbe.domain.exception.ExceptionCode
import com.dev6.rejordbe.domain.post.Post
import com.dev6.rejordbe.domain.post.PostType
import com.dev6.rejordbe.domain.post.dto.PostResult
import com.dev6.rejordbe.domain.post.dto.SearchPostCond
import com.dev6.rejordbe.domain.user.Users
import com.dev6.rejordbe.exception.DuplicatedNicknameException
import com.dev6.rejordbe.exception.IllegalParameterException
import com.dev6.rejordbe.exception.PostNotFoundException
import com.dev6.rejordbe.exception.UserNotFoundException
import com.dev6.rejordbe.presentation.controller.dto.updatePost.UpdatePostRequest
import com.dev6.rejordbe.presentation.controller.dto.userInfo.UpdateUserInfoRequest
import com.dev6.rejordbe.presentation.controller.post.info.PostInfoController
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import static org.mockito.ArgumentMatchers.isA
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(PostInfoController.class)
@Import(TestSecurityConfig.class)
class PostInfoControllerSpec extends Specification {
    private final String BASE_URL = '/v1/postInfos'

    @Autowired
    private MockMvc mvc
    @Autowired
    ObjectMapper objectMapper
    @MockBean
    ReadPostService readPostService

    // 모든 게시글 페이징 관련
    def "기준 시간 이전의 리스트 획득"() {
        given:
        def now = LocalDateTime.now()
        when(readPostService.allPosts(isA(LocalDateTime.class), isA(SearchPostCond), isA(Pageable.class)))
                .thenReturn(new PageImpl<PostResult>(
                        List.of(
                                new PostResult('postId1', 'content', PostType.OTHERS, 'uid1', 'nickname1', LocalDateTime.now()),
                                new PostResult('postId2', 'content', PostType.OTHERS, 'uid2', 'nickname2', LocalDateTime.now())
                        ),
                        PageRequest.of(0, 5),
                        1)
                )

        expect:
        mvc.perform(get(BASE_URL)
                .param('requestTime', now.format(DateTimeFormatter.ISO_DATE_TIME))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.content[0].postId').value('postId1'))
                .andExpect(jsonPath('\$.content[1].postId').value('postId2'))
                .andExpect(jsonPath('\$.content[0].uid').value('uid1'))
                .andExpect(jsonPath('\$.content[1].uid').value('uid2'))
                .andExpect(jsonPath('\$.content[0].nickname').value('nickname1'))
                .andExpect(jsonPath('\$.content[1].nickname').value('nickname2'))
                .andExpect(jsonPath('\$.content[0].createdDate').isNotEmpty())
                .andExpect(jsonPath('\$.content[1].createdDate').isNotEmpty())
                .andExpect(jsonPath('\$.pageable.pageNumber').value(0))
                .andExpect(jsonPath('\$.pageable.pageSize').value(5))
                .andExpect(jsonPath('\$.totalPages').value(1))
                .andExpect(jsonPath('\$.totalElements').value(2))
    }

    def "기준 시간 지정이 안 된 경우는 에러: 400"() {
        expect:
        mvc.perform(get(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath('\$.message').value(ExceptionCode.ILLEGAL_DATE_TIME))
    }
    // / 모든 게시글 페이징 관련

    // uid가 일치하는 게시글 페이징 관련
    def "유저 uid와 일치하는 리스트 획득"() {
        given:
        when(readPostService.postsWrittenByUid(isA(String.class), isA(Pageable.class)))
                .thenReturn(new PageImpl<PostResult>(
                        List.of(
                                new PostResult('postId1', 'content1', PostType.OTHERS, 'uid1', 'nickname1', LocalDateTime.now()),
                                new PostResult('postId2', 'content2', PostType.OTHERS, 'uid1', 'nickname1', LocalDateTime.now())
                        ),
                        PageRequest.of(0, 5),
                        1)
                )

        expect:
        mvc.perform(get(BASE_URL + '/withUid')
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.content[0].postId').value('postId1'))
                .andExpect(jsonPath('\$.content[1].postId').value('postId2'))
                .andExpect(jsonPath('\$.content[0].uid').value('uid1'))
                .andExpect(jsonPath('\$.content[1].uid').value('uid1'))
                .andExpect(jsonPath('\$.content[0].nickname').value('nickname1'))
                .andExpect(jsonPath('\$.content[1].nickname').value('nickname1'))
                .andExpect(jsonPath('\$.content[0].createdDate').isNotEmpty())
                .andExpect(jsonPath('\$.content[1].createdDate').isNotEmpty())
                .andExpect(jsonPath('\$.pageable.pageNumber').value(0))
                .andExpect(jsonPath('\$.pageable.pageSize').value(5))
                .andExpect(jsonPath('\$.totalPages').value(1))
                .andExpect(jsonPath('\$.totalElements').value(2))
    }

    def "유저 uid가 지정이 안 된 경우는 에러: 400"() {
        given:
        when(readPostService.postsWrittenByUid(isA(String.class), isA(Pageable.class))).thenThrow(exception)

        expect:
        mvc.perform(get(BASE_URL + '/withUid')
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(resultStatus)
                .andExpect(jsonPath('\$.message').value(message))

        where:
        testCase                  | message                   | exception                              | resultStatus
        "유저 uid가 지정이 안 된 경우: 400" | ExceptionCode.ILLEGAL_UID | new IllegalParameterException(message) | status().isBadRequest()
    }
    // / uid가 일치하는 게시글 페이징 관련

    // 게시글 수정 관련
    def "게시글 내용 변경에 성공하면 200 반환"() {
        given:
        when(readPostService.updatePostInfo(isA(Post.class)))
                .thenReturn(
                        PostResult.builder()
                        .postId(postId)
                        .contents(contents)
                        .build()
                )

        expect:
        mvc.perform(
                patch(BASE_URL + '/withPostId')
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                UpdatePostRequest.builder()
                                        .postId(postId)
                                        .contents(contents)
                                        .build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.postId').value(postId))
                .andExpect(jsonPath('\$.contents').value(contents))

        where:
        postId   | contents
        'postId' | 'contents'
    }

    @Unroll
    def "실패 케이스: #testCase 반환"() {
        given:
        when(readPostService.updatePostInfo(isA(Post.class))).thenThrow(exception)

        expect:
        mvc.perform(
                patch(BASE_URL + '/withPostId')
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                UpdatePostRequest.builder()
                                        .postId('postId')
                                        .contents('contents')
                                        .build())))
                .andExpect(resultStatus)
                .andExpect(jsonPath("\$.message").value(message))

        where:
        testCase                       | message                        | exception                              | resultStatus
        '정책에 맞지 않은 게시글 내용: 400' | ExceptionCode.ILLEGAL_CONTENTS | new IllegalParameterException(message) | status().isBadRequest()
        '존재하지 않는 게시글: 404'        | ExceptionCode.POST_NOT_FOUND   | new PostNotFoundException(message)     | status().isNotFound()
    }
    // / 게시글 수정 관련
}
