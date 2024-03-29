package com.dev6.rejordbe.presentation.controller.challengeReview.add;

import com.dev6.rejordbe.application.challengeReview.add.WriteChallengeReviewService;
import com.dev6.rejordbe.domain.challengeReview.dto.ChallengeReviewResult;
import com.dev6.rejordbe.presentation.controller.argumentResolver.LoggedIn;
import com.dev6.rejordbe.presentation.controller.dto.addChallengeReview.AddChallengeReviewRequest;
import com.dev6.rejordbe.presentation.controller.dto.addChallengeReview.AddChallengeReviewResponse;
import com.dev6.rejordbe.presentation.controller.dto.exception.ErrorResponse;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AddChallengeReviewController
 */
@Api(tags = "챌린지 리뷰 작성 컨트롤러")
@Slf4j
@RestController
@RequestMapping("/v1/challengeReviews")
@lombok.RequiredArgsConstructor
public class AddChallengeReviewController {

    private final WriteChallengeReviewService writeChallengeReviewService;

    @ApiOperation(
            value = "챌린지 리뷰 작성",
            nickname = "addChallengeReview",
            notes = "챌린지 리뷰 작성 API. 세션쿠키 필요",
            response = AddChallengeReviewResponse.class,
            authorizations = {@Authorization(value = "JWT")},
            tags = "챌린지 리뷰 작성 컨트롤러"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "정상"),
            @ApiResponse(code = 400, message = "옳지 않은 데이터 접근, 정책 위반 챌린지 리뷰", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "존재하지 않는 유저, 존재하지 않는 챌린지 리뷰", response = ErrorResponse.class)
    })
    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE},
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AddChallengeReviewResponse> addChallengeReview(
            @ApiParam(value = "챌린지 리뷰 작성 정보", required = true) @RequestBody final AddChallengeReviewRequest addChallengeReviewRequest,
            @ApiParam(hidden = true) @LoggedIn String uid
    ) {
        log.info("AddChallengeReviewController.addChallengeReview: challengeId: {}, reviewType: {}, badgeCode: {}, footpringAmount: {}",
                addChallengeReviewRequest.getChallengeId(),
                addChallengeReviewRequest.getChallengeReviewType());

        ChallengeReviewResult challengeReviewResult = writeChallengeReviewService.writeChallengeReview(
                addChallengeReviewRequest.getChallengeId(),
                addChallengeReviewRequest.getContents(),
                addChallengeReviewRequest.getChallengeReviewType(),
                uid);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AddChallengeReviewResponse.builder()
                        .challengeReviewId(challengeReviewResult.getChallengeReviewId())
                        .contents(challengeReviewResult.getContents())
                        .challengeReviewType(challengeReviewResult.getChallengeReviewType())
                        .uid(challengeReviewResult.getUid())
                        .build());
    }
}
