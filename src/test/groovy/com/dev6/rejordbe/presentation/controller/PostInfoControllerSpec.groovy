package com.dev6.rejordbe.presentation.controller

import com.dev6.rejordbe.application.post.read.ReadPostService
import com.dev6.rejordbe.domain.exception.ExceptionCode
import com.dev6.rejordbe.domain.post.PostType
import com.dev6.rejordbe.domain.post.dto.PostResult
import com.dev6.rejordbe.presentation.controller.post.info.PostInfoController
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import static org.mockito.ArgumentMatchers.isA
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(PostInfoController.class)
class PostInfoControllerSpec extends Specification {
    private final String BASE_URL = '/v1/postInfo'

    @Autowired
    private MockMvc mvc
    @Autowired
    ObjectMapper objectMapper
    @MockBean
    ReadPostService readPostService

    def "기준 시간 이전의 리스트 획득"() {
        given:
        def now = LocalDateTime.now()
        when(readPostService.allPosts(isA(LocalDateTime.class), isA(Pageable.class)))
                .thenReturn(new PageImpl<PostResult>(
                        List.of(
                                new PostResult('postId1', 'content', PostType.OTHERS, 'uid1'),
                                new PostResult('postId2', 'content', PostType.OTHERS, 'uid2')
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
                .andExpect(jsonPath('\$.message').value(ExceptionCode.ILLEGAL_DATE_TIME.name()))
    }
}
