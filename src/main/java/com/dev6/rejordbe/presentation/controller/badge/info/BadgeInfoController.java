package com.dev6.rejordbe.presentation.controller.badge.info;

import com.dev6.rejordbe.application.badge.read.BadgeInfoService;
import com.dev6.rejordbe.domain.badge.dto.BadgeByUidResult;
import com.dev6.rejordbe.presentation.controller.argumentResolver.LoggedIn;
import com.dev6.rejordbe.presentation.controller.dto.exception.ErrorResponse;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * BadgeInfoController
 */
@Api(tags = "배지 정보 컨트롤러")
@Slf4j
@RestController
@RequestMapping("/v1/badgeInfos")
@lombok.RequiredArgsConstructor
public class BadgeInfoController {

    private final BadgeInfoService badgeInfoService;

    @ApiOperation(
            value = "내 배지 리스트",
            nickname = "myBadgeByUid",
            notes = "내 배지 리스트 API",
            response = List.class,
            authorizations = {@Authorization(value = "JWT")},
            tags = "배지 정보 컨트롤러"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상"),
            @ApiResponse(code = 400, message = "유저 uid가 없는 경우", response = ErrorResponse.class)
    })
    @GetMapping(
            value = "/withUid",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE}
    )
    public ResponseEntity<List<BadgeByUidResult>> myBadgeByUid(
            @ApiParam(hidden = true) @LoggedIn String uid) {
        return ResponseEntity.ok(badgeInfoService.findBadgeByUid(uid));
    }
}
