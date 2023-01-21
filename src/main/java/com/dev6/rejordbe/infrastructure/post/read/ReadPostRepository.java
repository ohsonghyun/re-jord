package com.dev6.rejordbe.infrastructure.post.read;

import com.dev6.rejordbe.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ReadPostRepository
 */
public interface ReadPostRepository extends JpaRepository<Post, String>, ReadPostRepositoryCustom {
}
