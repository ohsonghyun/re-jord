package com.dev6.rejordbe.presentation.controller.challenge.info;

import com.dev6.rejordbe.domain.challenge.dto.ChallengeResult;
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
@RequestMapping("/v1/challengeInfo")
@lombok.RequiredArgsConstructor
public class ChallengeInfoController {

    @ApiOperation(
            value = "오늘 챌린지 정보",
            nickname = "todayChallengeInfo",
            notes = "오늘 챌린지 정보 API.",
            response = ChallengeResult.class,
            authorizations = {@Authorization(value = "TBD")},
            tags = "오늘 챌린지 정보 컨트롤러")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상")
    })
    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE}
    )
    public ResponseEntity<ChallengeResult> todayChallengeInfo() {
        return ResponseEntity.ok(ChallengeResult.builder()
                .challengeId("CH_challengeId")
                .title("찬 물로 세탁하기")
                .contents("찬물로 세탁하면 온수로 세탁할 때보다 10%의 탄소배출을 줄일 수 있습니다.")
                .footprintAmount(1)
                .badgeId("BG_badgeId")
                .build());
    }
}
