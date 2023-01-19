package com.dev6.rejordbe.infrastructure.post;

import com.dev6.rejordbe.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * PostRepository
 */
public interface PostRepository extends JpaRepository<Post, String> {
}
