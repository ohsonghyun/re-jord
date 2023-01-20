package com.dev6.rejordbe.application.post.add;

import com.dev6.rejordbe.application.id.IdGenerator;
import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.domain.post.Post;
import com.dev6.rejordbe.domain.post.dto.PostResult;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.infrastructure.post.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * WritePostServiceImpl
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@lombok.RequiredArgsConstructor
public class WritePostServiceImpl implements WritePostService {

    private final PostRepository postRepository;
    private final IdGenerator idGenerator;

    @Transactional
    @Override
    public PostResult writePost(@NonNull Post newPost) {
        if (newPost.getContents().trim().isEmpty()) {
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_CONTENT.name());
        }

        Post postResult = postRepository.save(
                Post.builder()
                        .postId(idGenerator.generate("PS"))
                        .contents(newPost.getContents())
                        .postType(newPost.getPostType())
                        .reviewType((newPost.getReviewType()))
                        .user(newPost.getUser())
                        .build()
        );

        return PostResult.builder()
                .postId(postResult.getPostId())
                .contents(postResult.getContents())
                .postType(postResult.getPostType())
                .reviewType(postResult.getReviewType())
                .uid(postResult.getUser().getUid())
                .build();
    }
}
