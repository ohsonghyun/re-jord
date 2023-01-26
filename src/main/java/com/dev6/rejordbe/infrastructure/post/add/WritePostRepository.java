package com.dev6.rejordbe.infrastructure.post.add;

import com.dev6.rejordbe.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * PostRepository
 */
public interface WritePostRepository extends JpaRepository<Post, String> {
}
