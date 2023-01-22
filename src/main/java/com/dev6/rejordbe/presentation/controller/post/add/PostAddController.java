package com.dev6.rejordbe.presentation.controller.post.add;

import com.dev6.rejordbe.application.post.add.WritePostService;
import com.dev6.rejordbe.domain.post.dto.PostResult;
import com.dev6.rejordbe.presentation.controller.dto.addPost.AddPostRequest;
import com.dev6.rejordbe.presentation.controller.dto.addPost.AddPostResponse;
import com.dev6.rejordbe.presentation.controller.dto.exception.ErrorResponse;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * PostAddController
 */
@Api(tags = "게시글 컨트롤러")
@RestController
@RequestMapping("/v1/post")
@lombok.RequiredArgsConstructor
public class PostAddController {

    private final WritePostService writePostService;

    @ApiOperation(
            value = "게시물 등록",
            nickname = "addPost",
            notes = "게시물 등록 API",
            response = AddPostResponse.class,
            authorizations = {@Authorization(value = "TBD")},
            tags = "게시글 컨트롤러"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "정상"),
            @ApiResponse(code = 400, message = "정책 위반 데이터", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "존재하지 않는 유저", response = ErrorResponse.class)
    })
    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE},
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AddPostResponse> addPost(@ApiParam(value = "게시글 작성 정보", required = true) @RequestBody final AddPostRequest addPostRequest) {
        PostResult postResult = writePostService.writePost(addPostRequest.toPost(), addPostRequest.getUid());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AddPostResponse.builder()
                        .postId(postResult.getPostId())
                        .contents(postResult.getContents())
                        .postType(postResult.getPostType())
                        .uid(postResult.getUid())
                        .build());
    }
}
