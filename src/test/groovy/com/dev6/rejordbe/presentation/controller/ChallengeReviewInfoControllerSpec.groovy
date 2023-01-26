package com.dev6.rejordbe.presentation.controller

import com.dev6.rejordbe.application.challengeReview.read.ReadChallengeReviewService
import com.dev6.rejordbe.domain.challengeReview.ChallengeReviewType
import com.dev6.rejordbe.domain.challengeReview.dto.ChallengeReviewResult
import com.dev6.rejordbe.domain.exception.ExceptionCode
import com.dev6.rejordbe.presentation.controller.challengeReview.info.ChallengeReviewInfoController
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

/**
 * ChallengeReviewInfoControllerSpec
 */
@WebMvcTest(ChallengeReviewInfoController.class)
class ChallengeReviewInfoControllerSpec extends Specification {
    private final String BASE_URL = '/v1/challengeReviewInfo'

    @Autowired
    private MockMvc mvc
    @Autowired
    ObjectMapper objectMapper
    @MockBean
    ReadChallengeReviewService readChallengeReviewService

    def "기준 시간 이전의 리스트 획득"() {
        given:
        def now = LocalDateTime.now()
        when(readChallengeReviewService.allChallengeReviews(isA(LocalDateTime.class), isA(Pageable.class)))
                .thenReturn(new PageImpl<ChallengeReviewResult>(
                        List.of(
                                new ChallengeReviewResult('challengeReviewId1', 'content', ChallengeReviewType.FREE, 'uid1', 'nickname1', LocalDateTime.now()),
                                new ChallengeReviewResult('challengeReviewId2', 'content', ChallengeReviewType.FREE, 'uid2', 'nickname2', LocalDateTime.now())
                        ),
                        PageRequest.of(0, 5),
                        1)
                )

        expect:
        mvc.perform(get(BASE_URL)
                .param('requestTime', now.format(DateTimeFormatter.ISO_DATE_TIME))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.content[0].challengeReviewId').value('challengeReviewId1'))
                .andExpect(jsonPath('\$.content[1].challengeReviewId').value('challengeReviewId2'))
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
                .andExpect(jsonPath('\$.message').value(ExceptionCode.ILLEGAL_DATE_TIME.name()))
    }
}
