package com.dev6.rejordbe.domain.post;

import com.dev6.rejordbe.domain.BaseTime;
import com.dev6.rejordbe.domain.exception.ExceptionCode;
import com.dev6.rejordbe.domain.user.Users;
import com.dev6.rejordbe.exception.IllegalParameterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

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

    /**
     * 게시물을 수정
     *
     * @param post {@code Post} 수정 정보
     */
    public void update(@NonNull final Post post) {
        if(!org.springframework.util.StringUtils.hasText(post.getContents())) {
            log.info("Post.update: ILLEGAL_CONTENTS: 게시글 내용이 null 또는 EmptyString");
            throw new IllegalParameterException(ExceptionCode.ILLEGAL_CONTENTS);
        }
        this.contents = post.getContents();
    }
}
