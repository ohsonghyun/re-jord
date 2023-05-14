package com.dev6.rejordbe.presentation.controller

import com.dev6.rejordbe.TestSecurityConfig
import com.dev6.rejordbe.application.post.delete.DeletePostService
import com.dev6.rejordbe.domain.exception.ExceptionCode
import com.dev6.rejordbe.exception.IllegalParameterException
import com.dev6.rejordbe.exception.PostNotFoundException
import com.dev6.rejordbe.exception.UnauthorizedUserException
import com.dev6.rejordbe.presentation.controller.post.delete.DeletePostController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Unroll

import static org.mockito.ArgumentMatchers.isA
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(DeletePostController)
@Import(TestSecurityConfig.class)
class DeletePostControllerSpec extends Specification {

    @Autowired
    MockMvc mvc
    @MockBean
    DeletePostService deletePostService

    private static final String baseUrl = '/v1/posts'

    // 게시글 삭제 관련
    def "게시글 삭제에 성공한다."() {
        given:
        when(deletePostService.deletePost(isA(String.class), isA(String.class)))
                .thenReturn('postId')

        expect:
        mvc.perform(
                delete(baseUrl + '/postId')
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.postId').value(postId))

        where:
        postId   | userId
        'postId' | 'userId'
    }

    @Unroll("#testCase")
    def "게시글 삭제 실패한 경우"() {
        given:
        when(deletePostService.deletePost(isA(String.class), isA(String.class)))
                .thenThrow(exception)

        expect:
        mvc.perform(
                delete(baseUrl + '/postId')
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(resultStatus)
                .andExpect(jsonPath('\$.message').value(message))

        where:
        testCase                   | message                          | exception                              | resultStatus
        '존재하지 않는 게시글일 경우'   | ExceptionCode.POST_NOT_FOUND     | new PostNotFoundException(message)     | status().isNotFound()
        '정책에 어긋나는 경우'         | ExceptionCode.ILLEGAL_PARAM      | new IllegalParameterException(message) | status().isBadRequest()
        '권한이 없는 유저가 접근한 경우' | ExceptionCode.UNAUTHORIZED_USER  | new UnauthorizedUserException(message) | status().isForbidden()
    }
    // / 게시글 삭제 관련
}
