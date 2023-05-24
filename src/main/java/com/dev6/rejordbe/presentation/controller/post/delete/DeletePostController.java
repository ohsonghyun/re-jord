package com.dev6.rejordbe.presentation.controller.post.delete;

import com.dev6.rejordbe.application.post.delete.DeletePostService;
import com.dev6.rejordbe.presentation.controller.argumentResolver.LoggedIn;
import com.dev6.rejordbe.presentation.controller.dto.deletePost.DeletePostResponse;
import com.dev6.rejordbe.presentation.controller.dto.exception.ErrorResponse;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

/**
 * DeletePostController
 */
@Api(tags = "게시글 삭제 컨트롤러")
@Slf4j
@RestController
@RequestMapping("/v1/posts")
@lombok.RequiredArgsConstructor
public class DeletePostController {

    private final DeletePostService deletePostService;

    @ApiOperation(
            value = "게시물 삭제",
            nickname = "deletePost",
            notes = "게시물 삭제 API.",
            response = String.class,
            authorizations = {@Authorization(value = "JWT")},
            tags = "게시글 삭제 컨트롤러"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "정상"),
            @ApiResponse(code = 400, message = "정책 위반 데이터", response = ErrorResponse.class),
            @ApiResponse(code = 403, message = "옳지 않은 데이터 접근", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "존재하지 않는 게시글", response = ErrorResponse.class)
    })
    @DeleteMapping(
            value = "/{postId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE}
    )
    public ResponseEntity<DeletePostResponse> deletePost(
            @Schema(description = "삭제할 게시글 id", required = true) @NonNull @PathVariable String postId,
            @ApiParam(hidden = true) @LoggedIn final String uid
    ) {
        log.info("DeletePostController.deletePost: postId: {}", postId);
        String deletedPostId = deletePostService.deletePost(postId, uid);

        return ResponseEntity.status(HttpStatus.OK).body(
                DeletePostResponse.builder()
                        .postId(deletedPostId)
                        .build());
    }
}
