package com.dev6.rejordbe.application.post.read;

import com.dev6.rejordbe.domain.post.dto.PostResult;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

/**
 * ReadPostServiceImpl
 */
public class ReadPostServiceImpl implements ReadPostService {

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<PostResult> allPosts(final LocalDateTime offsetTime) {
        return null;
    }
}
