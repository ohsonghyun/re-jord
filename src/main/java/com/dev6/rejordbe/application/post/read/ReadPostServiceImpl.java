package com.dev6.rejordbe.application.post.read;

import com.dev6.rejordbe.domain.post.dto.PostResult;
import com.dev6.rejordbe.infrastructure.post.read.ReadPostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * ReadPostServiceImpl
 */
@Service
@lombok.RequiredArgsConstructor
public class ReadPostServiceImpl implements ReadPostService {

    private final ReadPostRepository readPostRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<PostResult> allPosts(final LocalDateTime offsetTime, final Pageable pageable) {
        return null;
    }
}
