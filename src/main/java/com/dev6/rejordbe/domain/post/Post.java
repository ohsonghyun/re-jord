package com.dev6.rejordbe.domain.post;

import com.dev6.rejordbe.domain.BaseTime;
import com.dev6.rejordbe.domain.user.Users;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

/**
 * 게시글 도메인
 */
@Entity
@Slf4j
@lombok.Getter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder
public class Post extends BaseTime {
    @Id
    @Column(name = "post_id")
    private String postId;

    @Column(name = "contents", length = 3000)
    private String contents;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "post_type")
    private PostType postType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private Users user;
}
