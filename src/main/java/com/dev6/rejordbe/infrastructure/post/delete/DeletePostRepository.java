package com.dev6.rejordbe.infrastructure.post.delete;

import com.dev6.rejordbe.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DeletePostRepository
 */
public interface DeletePostRepository extends JpaRepository<Post, String> {

    /**
     * postId가 일치하는 데이터 삭제
     *
     * @param postId {@code String}
     */
    void deleteByPostId(final String postId);
}
