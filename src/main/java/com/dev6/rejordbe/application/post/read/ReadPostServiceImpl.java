package com.dev6.rejordbe.application.post.read;

import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.domain.post.dto.PostResult;
import com.dev6.rejordbe.exception.IllegalParameterException;
import com.dev6.rejordbe.infrastructure.post.read.ReadPostRepository;
import lombok.extern.slf4j.Slf4j;
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
}