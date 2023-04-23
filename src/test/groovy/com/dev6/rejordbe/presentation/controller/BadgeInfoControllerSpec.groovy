package com.dev6.rejordbe.presentation.controller

import com.dev6.rejordbe.TestSecurityConfig
import com.dev6.rejordbe.application.badge.read.BadgeInfoService
import com.dev6.rejordbe.domain.badge.BadgeCode
import com.dev6.rejordbe.domain.badge.dto.BadgeByUidResult
import com.dev6.rejordbe.domain.exception.ExceptionCode
import com.dev6.rejordbe.exception.IllegalParameterException
import com.dev6.rejordbe.presentation.controller.badge.info.BadgeInfoController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.mockito.ArgumentMatchers.isA
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
/**
 * BadgeInfoControllerSpec
 */
@WebMvcTest(BadgeInfoController.class)
@Import(TestSecurityConfig.class)
class BadgeInfoControllerSpec extends Specification {
    private final String BASE_URL = '/v1/badgeInfos'

    @Autowired
    private MockMvc mvc
    @MockBean
    BadgeInfoService badgeInfoService

    def "uid가 일치하는 배지 리스트 획득"() {
        given :
        when(badgeInfoService.findBadgeByUid(isA(String.class)))
            .thenReturn(
                    List.of(
                            new BadgeByUidResult(BadgeCode.PRO_ACTIVATE, BadgeCode.PRO_ACTIVATE.getBadgeName(), BadgeCode.PRO_ACTIVATE.imageUrl),
                            new BadgeByUidResult(BadgeCode.WATER_FAIRY, BadgeCode.WATER_FAIRY.getBadgeName(), BadgeCode.WATER_FAIRY.imageUrl)
                    )
            )

        expect:
        mvc.perform(get(BASE_URL + '/withUid')
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath('\$.[0].badgeCode').value(BadgeCode.PRO_ACTIVATE.name()))
            .andExpect(jsonPath('\$.[1].badgeCode').value(BadgeCode.WATER_FAIRY.name()))
            .andExpect(jsonPath('\$.[0].badgeName').value(BadgeCode.PRO_ACTIVATE.getBadgeName()))
            .andExpect(jsonPath('\$.[1].badgeName').value(BadgeCode.WATER_FAIRY.getBadgeName()))
            .andExpect(jsonPath('\$.[0].imageUrl').value(BadgeCode.PRO_ACTIVATE.getImageUrl()))
            .andExpect(jsonPath('\$.[1].imageUrl').value(BadgeCode.WATER_FAIRY.getImageUrl()))
    }

    def "uid가 지정이 안 된 경우에는 에러: 400"() {
        given:
        when(badgeInfoService.findBadgeByUid(isA(String.class))).thenThrow(exception)

        expect:
        mvc.perform(get(BASE_URL + '/withUid')
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(resultStatus)
                .andExpect(jsonPath('\$.message').value(message))
        where:
        testCase                        | message                   | exception                              | resultStatus
        "유저 uid가 지정이 안 된 경우: 400" | ExceptionCode.ILLEGAL_UID | new IllegalParameterException(message) | status().isBadRequest()
    }
}
