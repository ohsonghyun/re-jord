package com.dev6.rejordbe.domain.reward;

import com.dev6.rejordbe.domain.BaseTime;

import javax.persistence.*;

/**
 * BadgeReward
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Getter
public class BadgeReward extends BaseTime {
    @Id
    @Column(name = "id")
    private String id;
}
