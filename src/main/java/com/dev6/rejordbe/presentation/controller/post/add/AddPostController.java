package com.dev6.rejordbe.presentation.controller.post.add;

import com.dev6.rejordbe.application.post.add.WritePostService;
import com.dev6.rejordbe.domain.post.dto.PostResult;
import com.dev6.rejordbe.presentation.controller.argumentResolver.LoggedIn;
import com.dev6.rejordbe.presentation.controller.dto.addPost.AddPostRequest;
import com.dev6.rejordbe.presentation.controller.dto.addPost.AddPostResponse;
import com.dev6.rejordbe.presentation.controller.dto.exception.ErrorResponse;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AddPostController
 */
@Api(tags = "게시글 작성 컨트롤러")
@RestController
@RequestMapping("/v1/posts")
@lombok.RequiredArgsConstructor
public class AddPostController {

    private final WritePostService writePostService;

    @ApiOperation(
            value = "게시물 작성",
            nickname = "addPost",
            notes = "게시물 작성 API. 세션쿠키 필요",
            response = AddPostResponse.class,
            authorizations = {@Authorization(value = "TBD")},
            tags = "게시글 작성 컨트롤러"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "정상"),
            @ApiResponse(code = 400, message = "옳지 않은 데이터 접근", response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "정책 위반 데이터", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "존재하지 않는 유저", response = ErrorResponse.class)
    })
    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE},
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AddPostResponse> addPost(
            @ApiParam(value = "게시글 작성 정보", required = true) @RequestBody final AddPostRequest addPostRequest,
            @ApiParam(hidden = true) @LoggedIn final String uid
    ) {
        PostResult postResult = writePostService.writePost(addPostRequest.toPost(), uid);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AddPostResponse.builder()
                        .postId(postResult.getPostId())
                        .contents(postResult.getContents())
                        .postType(postResult.getPostType())
                        .uid(postResult.getUid())
                        .build());
    }
}
