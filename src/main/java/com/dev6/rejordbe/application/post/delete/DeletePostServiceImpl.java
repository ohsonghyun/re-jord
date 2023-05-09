package com.dev6.rejordbe.application.post.delete;

import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.domain.post.Post;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.exception.PostNotFoundException;
import com.dev6.rejordbe.exception.UnauthorizedUserException;
import com.dev6.rejordbe.infrastructure.post.delete.DeletePostRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

/**
 * DeletePostServiceImpl
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@lombok.RequiredArgsConstructor
public class DeletePostServiceImpl implements DeletePostService {

    private final DeletePostRepository deletePostRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public String deletePost(
            @NonNull final String postId,
            @NonNull final String uid
    ) {
        if(StringUtils.isBlank(postId)) {
            log.warn("DeletePostServiceImpl.deletePost: {}: postId is blank", ExceptionCode.ILLEGAL_PARAM);
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_PARAM);
        }

        Post targetPost = deletePostRepository.findById(postId).orElseThrow(() -> {
           log.warn("DeletePostServiceImpl.deletePost: POST_NOT_FOUND: postId: {}", postId);
            return new PostNotFoundException(ExceptionCode.POST_NOT_FOUND);
        });

        if(ObjectUtils.notEqual(uid, targetPost.getUser().getUid())) {
            log.warn("DeletePostServiceImpl.deletePost: UNAUTHORIZED_USER: uid: {}", uid);
            throw new UnauthorizedUserException(ExceptionCode.UNAUTHORIZED_USER);
        }

        deletePostRepository.deleteByPostId(postId);
        return postId;
    }
}
