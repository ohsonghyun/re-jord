package com.dev6.rejordbe.domain.footprint;

import com.dev6.rejordbe.domain.BaseTime;
import com.dev6.rejordbe.domain.challengeReview.ChallengeReview;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

/**
 * 발자국 도메인
 */
@Entity
@Slf4j
@lombok.Getter
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder
public class Footprint extends BaseTime {

    @Id
    @Column(name = "footprint_id")
    private String footprintId;

    @Column(name = "footprint_num")
    private Integer footprintNum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private ChallengeReview challengeReview;

    @Column(name = "acquirement_type")
    @Enumerated(value = EnumType.STRING)
    private AcquirementType acquirementType;
}
