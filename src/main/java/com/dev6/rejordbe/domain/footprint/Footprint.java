package com.dev6.rejordbe.domain.footprint;

import com.dev6.rejordbe.domain.BaseTime;
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

    @Column(name = "footprint_amount")
    private Integer footprintAmount;

    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "acquirement_type")
    @Enumerated(value = EnumType.STRING)
    private FootprintAcquirementType footprintAcquirementType;
}
