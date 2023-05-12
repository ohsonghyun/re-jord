package com.dev6.rejordbe.presentation.controller


import com.dev6.rejordbe.application.challengeReview.add.WriteChallengeReviewService
import com.dev6.rejordbe.domain.badge.BadgeCode
import com.dev6.rejordbe.domain.challengeReview.ChallengeReview
import com.dev6.rejordbe.domain.challengeReview.ChallengeReviewType
import com.dev6.rejordbe.domain.challengeReview.dto.ChallengeReviewResult
import com.dev6.rejordbe.domain.exception.ExceptionCode
import com.dev6.rejordbe.exception.ParentIdNotFoundException
import com.dev6.rejordbe.exception.IllegalParameterException
import com.dev6.rejordbe.exception.UserNotFoundException
import com.dev6.rejordbe.presentation.controller.argumentResolver.LoggedInUserArgumentResolver
import com.dev6.rejordbe.presentation.controller.challengeReview.add.AddChallengeReviewController
import com.dev6.rejordbe.presentation.controller.challengeReview.add.AddChallengeReviewControllerAdvice
import com.dev6.rejordbe.presentation.controller.dto.addChallengeReview.AddChallengeReviewRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.ModelAndViewContainer
import spock.lang.Specification
import spock.lang.Unroll

import static org.mockito.ArgumentMatchers.isA
import static org.mockito.Mockito.when
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(AddChallengeReviewController)
class AddChallengeReviewControllerSpec extends Specification {

    MockMvc mvc
    AddChallengeReviewController addChallengeReviewController
    MockLoggedInUserArgumentResolver mockLoggedInUserArgumentResolver = new MockLoggedInUserArgumentResolver()

    @Autowired
    ObjectMapper objectMapper
    @MockBean
    WriteChallengeReviewService writeChallengeReviewService

    private static final String baseUrl = '/v1/challengeReviews'

    def setup() {
        addChallengeReviewController = new AddChallengeReviewController(writeChallengeReviewService)
        mvc = MockMvcBuilders.standaloneSetup(addChallengeReviewController)
                .setControllerAdvice(new AddChallengeReviewControllerAdvice())
                .setCustomArgumentResolvers(mockLoggedInUserArgumentResolver)
                .build()
    }

    def "챌린지 리뷰 등록 성공시 201을 반환한다"() {
        given:
        when(writeChallengeReviewService.writeChallengeReview(
                isA(String.class),
                isA(String.class),
                isA(ChallengeReviewType.class),
                isA(String.class)))
                .thenReturn(
                        ChallengeReviewResult.builder()
                                .challengeReviewId(challengeReviewId)
                                .contents(contents)
                                .challengeReviewType(challengeReviewType)
                                .uid(uid)
                                .build())

        expect:
        mvc.perform(
                post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        AddChallengeReviewRequest.builder()
                                                .contents(contents)
                                                .challengeReviewType(challengeReviewType)
                                                .challengeId(challengeId)
                                                .build()
                                )))
                .andExpect(status().isCreated())
                .andExpect(jsonPath('\$.challengeReviewId').value(challengeReviewId))
                .andExpect(jsonPath('\$.contents').value(contents))
                .andExpect(jsonPath('\$.challengeReviewType').value(challengeReviewType.name()))
                .andExpect(jsonPath('\$.uid').value(uid))

        where:
        challengeReviewId   | contents   | challengeReviewType         | challengeId   | uid
        'challengeReviewId' | 'contents' | ChallengeReviewType.FEELING | 'challengeId' | 'uid'
    }

    @Unroll
    def "실패 케이스: #testCase 반환"() {
        given:
        when(writeChallengeReviewService.writeChallengeReview(
                isA(String.class),
                isA(String.class),
                isA(ChallengeReviewType.class),
                isA(String.class)))
                .thenThrow(exception)

        expect:
        mvc.perform(
                post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        AddChallengeReviewRequest.builder()
                                                .contents('contents')
                                                .challengeReviewType(ChallengeReviewType.FEELING)
                                                .challengeId('challengeId')
                                                .build()
                                )))
                .andExpect(resultStatus)
                .andExpect(jsonPath("\$.message").value(message))

        where:
        testCase                  | message                                  | exception                              | resultStatus
        'contents 정책 위반 데이터: 400' | ExceptionCode.ILLEGAL_CONTENTS           | new IllegalParameterException(message) | status().isBadRequest()
        '존재하지 않는 유저: 404'         | ExceptionCode.USER_NOT_FOUND             | new UserNotFoundException(message)     | status().isNotFound()
        '존재하지 않는 챌린지 리뷰: 404'         | ExceptionCode.CHALLENGE_REVIEW_NOT_FOUND | new ParentIdNotFoundException(message) | status().isNotFound()
    }

    @Unroll
    def "존재하지 않는 챌린지 리뷰"() {
        given:
        when(writeChallengeReviewService.writeChallengeReview(
                isA(String.class),
                isA(String.class),
                isA(ChallengeReviewType.class),
                isA(String.class)))
                .thenThrow(exception)

        expect:
        mvc.perform(
                post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                objectMapper.writeValueAsString(
                                        AddChallengeReviewRequest.builder()
                                                .contents('contents')
                                                .challengeReviewType(ChallengeReviewType.FEELING)
                                                .challengeId('challengeId')
                                                .build()
                                )))
                .andExpect(resultStatus)
                .andExpect(jsonPath("\$.message").value(message))

        where:
        message                                  | exception                              | resultStatus
        ExceptionCode.CHALLENGE_REVIEW_NOT_FOUND | new ParentIdNotFoundException(message) | status().isNotFound()
    }

    private class MockLoggedInUserArgumentResolver extends LoggedInUserArgumentResolver {
        @Override
        Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
            return "uid"
        }
    }
}
