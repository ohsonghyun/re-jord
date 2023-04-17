package com.dev6.rejordbe.presentation.controller.footprint.info;

import com.dev6.rejordbe.application.footprint.read.ReadFootprintService;
import com.dev6.rejordbe.domain.footprint.dto.FootprintResult;
import com.dev6.rejordbe.presentation.controller.argumentResolver.LoggedIn;
import com.dev6.rejordbe.presentation.controller.dto.exception.ErrorResponse;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * FootprintInfoController
 */
@Api(tags = "발자국 정보 컨트롤러")
@Slf4j
@RestController
@RequestMapping("/v1/footprintInfos")
@lombok.RequiredArgsConstructor
public class FootprintInfoController {

    private final ReadFootprintService readFootprintService;

    @ApiOperation(
            value = "UID로 발자국 취득 페이징",
            nickname = "allFootprintByUid",
            notes = "UID로 발자국 취득 페이징 API.",
            response = Page.class,
            authorizations = {@Authorization(value = "JWT")},
            tags = "발자국 정보 컨트롤러")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상"),
            @ApiResponse(code = 400, message = "UID가 없는 경우", response = ErrorResponse.class)
    })
    @GetMapping(
            value = "/withUid",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE}
    )
    public ResponseEntity<Page<FootprintResult>> allFootprintByUid(
            @ApiParam(hidden = true) @LoggedIn final String uid,
            @PageableDefault(page = 0, size = 10) final Pageable pageable
    ) {
        log.info("FootprintInfoController.allFootprintByUid: uid: {}", uid);
        return ResponseEntity.ok(readFootprintService.searchAllByUid(uid, pageable));
    }
}
