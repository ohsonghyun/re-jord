package com.dev6.rejordbe.application.post.read;

import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.domain.post.dto.PostResult;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.exception.UserNotFoundException;
import com.dev6.rejordbe.infrastructure.post.read.ReadPostRepository;
import com.dev6.rejordbe.infrastructure.user.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
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
    private final UserInfoRepository userInfoRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<PostResult> allPosts(
            @NonNull final LocalDateTime offsetTime,
            @NonNull final Pageable pageable
    ) {
        if (Objects.isNull(offsetTime)) {
            log.warn("ReadPostServiceImpl.allPosts: ILLEGAL_DATE_TIME: {}", offsetTime);
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_DATE_TIME.name());
        }
        return readPostRepository.searchAll(offsetTime, pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<PostResult> postsWrittenByUid(
            @NonNull final String uid,
            @NonNull final Pageable pageable
    ) {
        if(StringUtils.isBlank(uid)) {
            log.warn("ReadPostServiceImpl.postsWrittenByUid: ILLEGAL_UID: {}", uid);
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_UID.name());
        }

        userInfoRepository.findById(uid).orElseThrow(() -> new UserNotFoundException(ExceptionCode.USER_NOT_FOUND.name()));

        return readPostRepository.searchPostByUid(uid, pageable);
    }
}
