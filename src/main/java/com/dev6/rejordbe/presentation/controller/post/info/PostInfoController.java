package com.dev6.rejordbe.presentation.controller.post.info;

import com.dev6.rejordbe.application.post.read.ReadPostService;
import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.domain.post.Post;
import com.dev6.rejordbe.domain.post.dto.PostResult;
import com.dev6.rejordbe.domain.post.dto.SearchPostCond;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.presentation.controller.argumentResolver.LoggedIn;
import com.dev6.rejordbe.presentation.controller.dto.exception.ErrorResponse;
import com.dev6.rejordbe.presentation.controller.dto.updatePost.UpdatePostRequest;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * PostInfoController
 */
@Api(tags = "게시글 정보 컨트롤러")
@Slf4j
@RestController
@RequestMapping("/v1/postInfos")
@lombok.RequiredArgsConstructor
public class PostInfoController {

    private final ReadPostService readPostService;

    @ApiOperation(
            value = "모든 게시글 페이징",
            nickname = "allPosts",
            notes = "모든 게시글 페이징 API.",
            response = Page.class,
            authorizations = {@Authorization(value = "JWT")},
            tags = "게시글 정보 컨트롤러")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상"),
            @ApiResponse(code = 400, message = "취득기준시각이 없는 경우", response = ErrorResponse.class)
    })
    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE}
    )
    public ResponseEntity<Page<PostResult>> allPosts(
            @NonNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @ApiParam(value = "취득기준시각", example = "2023-01-23T23:16:59", required = true)
            final LocalDateTime requestTime,
            final SearchPostCond cond,
            @PageableDefault(page = 0, size = 10) final Pageable pageable
    ) {
        log.info("PostInfoController.allPosts: requestTime: {}", requestTime);
        if (Objects.isNull(requestTime)) {
            log.warn("PostInfoController.allPosts: ILLEGAL_DATE_TIME: {}", requestTime);
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_DATE_TIME);
        }
        return ResponseEntity.ok(readPostService.allPosts(requestTime, cond, pageable));
    }

    @ApiOperation(
            value = "유저 uid가 일치하는 게시글 페이징",
            nickname = "postsWrittenByUid",
            notes = "유저 uid가 일치하는 게시글 페이징 API",
            response = Page.class,
            authorizations = {@Authorization(value = "JWT")},
            tags = "내가 쓴 게시글 정보 컨트롤러")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상"),
            @ApiResponse(code = 400, message = "유저 uid가 없는 경우", response = ErrorResponse.class)
    })
    @GetMapping(
            value = "/withUid",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE}
    )
    public ResponseEntity<Page<PostResult>> postsWrittenByUid(
            @ApiParam(hidden = true) @LoggedIn final String uid,
            @PageableDefault(page = 0, size = 10) final Pageable pageable
    ) {
        return ResponseEntity.ok(readPostService.postsWrittenByUid(uid, pageable));
    }

    @ApiOperation(
            value = "게시글 수정",
            nickname = "updatePostContents",
            notes = "게시글 수정 API",
            response = PostResult.class,
            authorizations = {@Authorization(value = "JWT")},
            tags = "내가 쓴 게시글 정보 컨트롤러")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "게시글 변경 성공"),
            @ApiResponse(code = 400, message = "정책 위반 데이터", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "존재하지 않는 게시글", response = ErrorResponse.class)
    })
    @PatchMapping(
            value = "/withPostId",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE},
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PostResult> updatePostContents(
            @ApiParam(value = "수정할 게시글 정보", required = true) @RequestBody UpdatePostRequest request
   ) {
        log.info("PostInfoController.updatePostContents: contents: {}", request.getContents());
        return ResponseEntity.ok(readPostService.updatePostInfo(Post.builder().postId(request.getPostId()).contents(request.getContents()).build()));
    }
}
