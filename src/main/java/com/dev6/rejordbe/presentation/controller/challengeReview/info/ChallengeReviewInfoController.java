package com.dev6.rejordbe.presentation.controller.challengeReview.info;

import com.dev6.rejordbe.application.challengeReview.read.ReadChallengeReviewService;
import com.dev6.rejordbe.domain.challengeReview.dto.ChallengeReviewResult;
import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.presentation.controller.dto.exception.ErrorResponse;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * ChallengeReviewInfoController
 */
@Api(tags = "챌린지 게시글 정보 컨트롤러")
@Slf4j
@RestController
@RequestMapping("/v1/challengeReviewInfos")
@lombok.RequiredArgsConstructor
public class ChallengeReviewInfoController {

    private final ReadChallengeReviewService readChallengeReviewService;

    @ApiOperation(
            value = "모든 챌린지 게시글 페이징",
            nickname = "allChallengeReviews",
            notes = "모든 챌린지 게시글 페이징 API.",
            response = Page.class,
            authorizations = {@Authorization(value = "TBD")},
            tags = "챌린지 게시글 정보 컨트롤러")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상"),
            @ApiResponse(code = 400, message = "취득기준시각이 없는 경우", response = ErrorResponse.class)
    })
    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE}
    )
    public ResponseEntity<Page<ChallengeReviewResult>> allChallengeReviews(
            @NonNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @ApiParam(value = "취득기준시각", example = "2023-01-23T23:16:59", required = true)
            final LocalDateTime requestTime,
            final Pageable pageable
    ) {
        log.info("ChallengeReviewInfoController.allChallengeReviews: requestTime: {}", requestTime);
        if (Objects.isNull(requestTime)) {
            log.warn("ChallengeReviewInfoController.allChallengeReviews: ILLEGAL_DATE_TIME: {}", requestTime);
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_DATE_TIME.name());
        }
        return ResponseEntity.ok(readChallengeReviewService.allChallengeReviews(requestTime, pageable));
    }
}
