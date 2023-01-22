package com.dev6.rejordbe.presentation.controller

import com.dev6.rejordbe.application.post.add.WritePostService
import com.dev6.rejordbe.domain.exception.ExceptionCode
import com.dev6.rejordbe.domain.post.Post
import com.dev6.rejordbe.domain.post.PostType
import com.dev6.rejordbe.domain.post.dto.PostResult
import com.dev6.rejordbe.exception.IllegalParameterException
import com.dev6.rejordbe.exception.UserNotFoundException
import com.dev6.rejordbe.presentation.controller.dto.addPost.AddPostRequest
import com.dev6.rejordbe.presentation.controller.post.add.PostAddController
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Unroll

import static org.mockito.ArgumentMatchers.isA
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(PostAddController)
class PostAddControllerSpec extends Specification {

    @Autowired
    MockMvc mvc
    @Autowired
    ObjectMapper objectMapper
    @MockBean
    WritePostService writePostService

    private static final String baseUrl = '/v1/post'

    def "게시글 등록 성공시 201을 반환한다"() {
        given:
        when(writePostService.writePost(isA(Post.class), isA(String.class)))
                .thenReturn(
                        PostResult.builder()
                                .postId(postId)
                                .contents(contents)
                                .postType(postType)
                                .uid(uid)
                                .build())

        expect:
        mvc.perform(
                post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        AddPostRequest.builder()
                                                .postId(postId)
                                                .contents(contents)
                                                .postType(postType)
                                                .uid(uid)
                                                .build()
                                )))
                .andExpect(status().isCreated())
                .andExpect(jsonPath('\$.postId').value(postId))
                .andExpect(jsonPath('\$.contents').value(contents))
                .andExpect(jsonPath('\$.postType').value(postType.name()))
                .andExpect(jsonPath('\$.uid').value(uid))

        where:
        postId   | contents   | postType           | uid
        'postId' | 'contents' | PostType.SHARE     | 'uid'
    }

    @Unroll
    def "실패 케이스: #testCase 반환"() {
        given:
        when(writePostService.writePost(isA(Post.class), isA(String.class))).thenThrow(exception)

        expect:
        mvc.perform(
                post(baseUrl)
                .contentType(MediaType.APPLICATION_JSON).content(
                        objectMapper.writeValueAsString(
                                AddPostRequest.builder()
                                        .postId('postId')
                                        .contents('contents')
                                        .postType(PostType.OTHERS)
                                        .uid('uid')
                                        .build()
                        )))
                .andExpect(resultStatus)
                .andExpect(jsonPath("\$.message").value(message))

        where:
        testCase               | message                              | exception                               | resultStatus
        '정책 위반 데이터: 400'   | ExceptionCode.ILLEGAL_CONTENT.name() | new IllegalParameterException(message)  | status().isBadRequest()
        '존재하지 않는 유저: 404' | ExceptionCode.USER_NOT_FOUND.name()  | new UserNotFoundException(message)      | status().isNotFound()

    }
}
