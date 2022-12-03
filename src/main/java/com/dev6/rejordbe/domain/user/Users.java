package com.dev6.rejordbe.domain.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 유저 도메인
 */
@Entity
@lombok.Getter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder
public class Users {

    @Id
    @Column(name = "uid")
    private String uid;

    @Column(name = "user_id", unique = true)
    private String userId;

    @Column(name = "nickname", unique = true)
    private String nickname;

    @Column(name = "password")
    private String password;

}
