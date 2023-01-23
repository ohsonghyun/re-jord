package com.dev6.rejordbe.presentation.controller.post.info;

import com.dev6.rejordbe.application.post.read.ReadPostService;
import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.domain.post.dto.PostResult;
import com.dev6.rejordbe.exception.IllegalParameterException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * PostInfoController
 */
@RestController
@RequestMapping("/v1/postInfo")
@lombok.RequiredArgsConstructor
public class PostInfoController {

    private final ReadPostService readPostService;

    @GetMapping
    public ResponseEntity<Page<PostResult>> allPosts(
            @NonNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime requestTime,
            final Pageable pageable
    ) {
        if (Objects.isNull(requestTime)) {
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_DATE_TIME.name());
        }
        return ResponseEntity.ok(readPostService.allPosts(requestTime, pageable));
    }
}
