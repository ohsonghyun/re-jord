package com.dev6.rejordbe.application.post.add;

import com.dev6.rejordbe.application.id.IdGenerator;
import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.domain.post.Post;
import com.dev6.rejordbe.domain.post.dto.PostResult;
import com.dev6.rejordbe.domain.user.Users;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.exception.UserNotFoundException;
import com.dev6.rejordbe.infrastructure.post.add.WritePostRepository;
import com.dev6.rejordbe.infrastructure.user.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * WritePostServiceImpl
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@lombok.RequiredArgsConstructor
public class WritePostServiceImpl implements WritePostService {

    private final WritePostRepository writePostRepository;
    private final IdGenerator idGenerator;
    private final UserInfoRepository userInfoRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public PostResult writePost(@NonNull Post newPost, @NonNull String uid) {
        Users user = userInfoRepository.findById(uid).orElseThrow(() -> {
            log.warn("WritePostServiceImpl.writePost: USER_NOT_FOUND: uid: {}", uid);
            return new UserNotFoundException(ExceptionCode.USER_NOT_FOUND);
        });

        if (!StringUtils.hasText(newPost.getContents())) {
            log.info("WritePostServiceImpl.writePost: ILLEGAL_CONTENTS: {}", newPost.getContents());
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_CONTENTS);
        }

        Post postResult = writePostRepository.save(
                Post.builder()
                        .postId(idGenerator.generate("PS"))
                        .contents(newPost.getContents())
                        .postType(newPost.getPostType())
                        .user(user)
                        .build()
        );

        return PostResult.builder()
                .postId(postResult.getPostId())
                .contents(postResult.getContents())
                .postType(postResult.getPostType())
                .uid(postResult.getUser().getUid())
                .build();
    }
}
