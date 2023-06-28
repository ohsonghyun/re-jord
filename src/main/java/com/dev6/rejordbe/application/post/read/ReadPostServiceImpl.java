package com.dev6.rejordbe.application.post.read;

import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.domain.post.Post;
import com.dev6.rejordbe.domain.post.dto.PostResult;
import com.dev6.rejordbe.domain.post.dto.SearchPostCond;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.exception.PostNotFoundException;
import com.dev6.rejordbe.infrastructure.post.read.ReadPostRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * ReadPostServiceImpl
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@lombok.RequiredArgsConstructor
public class ReadPostServiceImpl implements ReadPostService {

    private final ReadPostRepository readPostRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<PostResult> allPosts(
            @NonNull final LocalDateTime offsetTime,
            @Nullable final SearchPostCond cond,
            @NonNull final Pageable pageable
    ) {
        if (Objects.isNull(offsetTime)) {
            log.warn("ReadPostServiceImpl.allPosts: ILLEGAL_DATE_TIME: {}", offsetTime);
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_DATE_TIME);
        }
        return readPostRepository.searchPostAll(offsetTime, cond, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<PostResult> postsWrittenByUid(
            @NonNull final String uid,
            @Nullable final SearchPostCond cond,
            @NonNull final Pageable pageable
    ) {
        if(StringUtils.isBlank(uid)) {
            log.warn("ReadPostServiceImpl.postsWrittenByUid: ILLEGAL_UID: {}", uid);
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_UID);
        }
        return readPostRepository.searchPostByUid(uid, cond, pageable);
    }


    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public PostResult updatePostInfo(@NonNull final Post newPostInfo) {
        if (!org.springframework.util.StringUtils.hasText(newPostInfo.getContents())) {
            log.info("ReadPostServiceImpl.updatePostInfo: ILLEGAL_CONTENTS: {}", newPostInfo.getContents());
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_CONTENTS);
        }

        Post targetPost = readPostRepository.findById(newPostInfo.getPostId())
                .orElseThrow(() -> {
                    log.info("ReadPostService.updatePostInfo: POST_NOT_FOUND: {}", newPostInfo.getPostId());
                    return new PostNotFoundException(ExceptionCode.POST_NOT_FOUND);
                });

        targetPost.update(Post.builder().contents(newPostInfo.getContents()).build());
        return PostResult.builder()
                .postId(targetPost.getPostId())
                .contents(targetPost.getContents())
                .postType(targetPost.getPostType())
                .uid(targetPost.getUser().getUid())
                .nickname(targetPost.getUser().getNickname())
                .createdDate(targetPost.getCreatedDate())
                .build();
    }
}
