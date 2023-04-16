package com.dev6.rejordbe.presentation.controller.footprint.info

import com.dev6.rejordbe.TestSecurityConfig
import com.dev6.rejordbe.application.footprint.read.ReadFootprintService
import com.dev6.rejordbe.domain.badge.BadgeCode
import com.dev6.rejordbe.domain.exception.ExceptionCode
import com.dev6.rejordbe.domain.footprint.FootprintAcquirementType
import com.dev6.rejordbe.domain.footprint.dto.FootprintResult
import com.dev6.rejordbe.exception.IllegalParameterException
import com.dev6.rejordbe.presentation.controller.challenge.info.ChallengeInfoController
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import java.time.LocalDateTime

import static org.mockito.ArgumentMatchers.isA
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * FootprintInfoControllerSpec
 */
@WebMvcTest(FootprintInfoController)
@Import(TestSecurityConfig.class)
class FootprintInfoControllerSpec extends Specification {

    @Autowired
    MockMvc mvc
    @Autowired
    ObjectMapper objectMapper
    @MockBean
    ReadFootprintService readFootprintService

    private static final String baseUrl = '/v1/footprintInfos'

    // -----------------------------------------------
    // allFootprintByUid
    // -----------------------------------------------

    def "UID로 사용자가 달성한 발자국을 취득할 수 있다"() {
        given:
        when(readFootprintService.searchAllByUid(isA(String.class), isA(Pageable.class)))
                .thenReturn(
                        new PageImpl<FootprintResult>(
                                List.of(
                                        new FootprintResult('footprintId', 15, 'parentId', FootprintAcquirementType.CHALLENGE_REVIEW, 'title', BadgeCode.WATER_FAIRY, LocalDateTime.now())
                                ),
                                PageRequest.of(0, 5),
                                2
                        )
                )

        expect:
        mvc.perform(get(baseUrl + '/withUid?page=0&size=5'))
                .andExpect(status().isOk())
                .andExpect(jsonPath('\$.content[0].footprintId').value('footprintId'))
                .andExpect(jsonPath('\$.content[0].footprintAmount').value(15))
                .andExpect(jsonPath('\$.content[0].parentId').value('parentId'))
                .andExpect(jsonPath('\$.content[0].footprintAcquirementType').value('CHALLENGE_REVIEW'))
                .andExpect(jsonPath('\$.content[0].title').value('title'))
                .andExpect(jsonPath('\$.content[0].badgeCode').value('WATER_FAIRY'))
                .andExpect(jsonPath('\$.content[0].createdDate').isNotEmpty())
                .andExpect(jsonPath('\$.pageable.pageNumber').value(0))
                .andExpect(jsonPath('\$.pageable.pageSize').value(5))
                .andExpect(jsonPath('\$.totalPages').value(1))
                .andExpect(jsonPath('\$.totalElements').value(1))
    }

    def "UID가 없으면 에러: 400"() {
        given:
        when(readFootprintService.searchAllByUid(isA(String.class), isA(Pageable.class)))
                .thenThrow(new IllegalParameterException(ExceptionCode.ILLEGAL_PARAM))

        expect:
        mvc.perform(get(baseUrl + '/withUid?page=0&size=5'))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath('\$.message').value(ExceptionCode.ILLEGAL_PARAM))
    }
}
