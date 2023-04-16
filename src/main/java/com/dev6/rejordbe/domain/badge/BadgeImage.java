package com.dev6.rejordbe.domain.badge;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

/**
 * 배지 이미지
 */
@Entity
@Slf4j
@lombok.Getter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder
public class BadgeImage {

    @Id
    @Column(name = "badge_code")
    @Enumerated(value = EnumType.STRING)
    private BadgeCode badgeCode;

    @Column(name = "image_url")
    private String imageUrl;
}
