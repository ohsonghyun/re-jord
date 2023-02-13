package com.dev6.rejordbe.domain.badge;

import com.dev6.rejordbe.domain.BaseTime;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

/**
 * 배지 도메인
 */
@Entity
@Slf4j
@lombok.Getter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder
public class Badge extends BaseTime {

    @Id
    @Column(name = "badge_id")
    private String badgeId;

    @Column(name = "badge_code")
    @Enumerated(value = EnumType.STRING)
    private BadgeCode badgeCode;

    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "acquirement_type")
    @Enumerated(value = EnumType.STRING)
    private BadgeAcquirementType badgeAcquirementType;
}
