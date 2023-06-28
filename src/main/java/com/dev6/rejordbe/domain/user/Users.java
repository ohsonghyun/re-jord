package com.dev6.rejordbe.domain.user;

import com.dev6.rejordbe.domain.BaseTime;
import com.dev6.rejordbe.domain.challengeReview.ChallengeReview;
import com.dev6.rejordbe.domain.post.Post;
import com.dev6.rejordbe.domain.role.Role;
import com.dev6.rejordbe.exception.IllegalParameterException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 유저 도메인
 */
@Entity
@Slf4j
@lombok.Getter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder
public class Users extends BaseTime {

    @Id
    @Column(name = "uid")
    private String uid;

    @Column(name = "user_id", unique = true)
    private String userId;

    @Column(name = "nickname", unique = true)
    private String nickname;

    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ChallengeReview> challengeReviews = new ArrayList<>();


    /**
     * 닉네임을 수정
     *
     * @param users {@code Users} 수정 정보
     */
    public void update(@NonNull final Users users) {
        // 20221213 시점에는 닉네임만 변경가능
        if (StringUtils.isBlank(users.getNickname())) {
            log.info("Users.update: ILLEGAL_NICKNAME: 닉네임이 null 또는 EmptyString");
            throw new IllegalParameterException("ILLEGAL_NICKNAME");
        }
        this.nickname = users.getNickname();
    }
}
