package com.dev6.rejordbe.presentation.controller.challenge.info;

import com.dev6.rejordbe.application.challenge.read.ChallengeInfoService;
import com.dev6.rejordbe.domain.challenge.dto.ChallengeResult;
import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.exception.ChallengeNotFoundException;
import com.dev6.rejordbe.presentation.controller.dto.exception.ErrorResponse;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ChallengeInfoController
 */
@Api(tags = "오늘 챌린지 정보 컨트롤러")
@Slf4j
@RestController
@RequestMapping("/v1/challengeInfos")
@lombok.RequiredArgsConstructor
public class ChallengeInfoController {

    private final ChallengeInfoService readChallengeService;

    @ApiOperation(
            value = "오늘 챌린지 정보",
            nickname = "todayChallengeInfos",
            notes = "오늘 챌린지 정보 API.",
            response = ChallengeResult.class,
            authorizations = {@Authorization(value = "TBD")},
            tags = "오늘 챌린지 정보 컨트롤러")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상"),
            @ApiResponse(code = 404, message = "챌린지가 없는 경우", response = ErrorResponse.class)
    })
    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE}
    )
    public ResponseEntity<ChallengeResult> todayChallengeInfo() {
        ChallengeResult result = readChallengeService.findChallengeByFlag()
                .orElseThrow(() -> new ChallengeNotFoundException(ExceptionCode.CHALLENGE_NOT_FOUND.name()));

        return ResponseEntity.ok(result);
    }
}
