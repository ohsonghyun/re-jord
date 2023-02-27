package com.dev6.rejordbe.presentation.controller

import com.dev6.rejordbe.TestSecurityConfig
import com.dev6.rejordbe.application.challenge.read.ChallengeInfoService
import com.dev6.rejordbe.domain.badge.BadgeCode
import com.dev6.rejordbe.domain.challenge.dto.ChallengeResult
import com.dev6.rejordbe.domain.exception.ExceptionCode
import com.dev6.rejordbe.presentation.controller.challenge.info.ChallengeInfoController
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ChallengeInfoController)
@Import(TestSecurityConfig.class)
class ChallengeInfoControllerSpec extends Specification {

    @Autowired
    MockMvc mvc
    @Autowired
    ObjectMapper objectMapper
    @MockBean
    ChallengeInfoService readChallengeService

    private static final String baseUrl = '/v1/challengeInfos'

    def "챌린지 갖고오기에 성공하면 200을 반환한다"() {
        given:
        def anChallenge = ChallengeResult.builder()
                .challengeId(challengeId)
                .title(title)
                .contents(contents)
                .footprintAmount(footprintAmount)
                .badgeCode(badgeCode)
                .badgeName(badgeName)
                .imgFront(imgFront)
                .imgBack(imgBack)
                .textColor(textColor)
                .build()

        when(readChallengeService.findTodayChallengeInFlag())
            .thenReturn(Optional.of(anChallenge))

        expect:
        mvc.perform(get(baseUrl)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('\$.challengeId').value(challengeId))
                    .andExpect(jsonPath('\$.title').value(title))
                    .andExpect(jsonPath('\$.contents').value(contents))
                    .andExpect(jsonPath('\$.footprintAmount').value(footprintAmount))
                    .andExpect(jsonPath('\$.badgeCode').value(badgeCode.name()))
                    .andExpect(jsonPath('\$.badgeName').value(badgeName))
                    .andExpect(jsonPath('\$.imgFront').value(imgFront))
                    .andExpect(jsonPath('\$.imgBack').value(imgBack))
                    .andExpect(jsonPath('\$.textColor').value(textColor))


        where:
        challengeId   | title   | contents   | footprintAmount | badgeCode            | badgeName   | imgFront   | imgBack   | textColor
        "challengeId" | "title" | "contents" | 15              | BadgeCode.VEGETARIAN | "badgeName" | "imgFront" | "imgBack" | "textColor"
    }

    def "챌린지가 없으면 404 반환"() {
        given:
        when(readChallengeService.findTodayChallengeInFlag())
                .thenReturn(Optional.empty())

        expect:
        mvc.perform(get(baseUrl)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath('\$.message').value(ExceptionCode.CHALLENGE_NOT_FOUND.name()))

    }
}
