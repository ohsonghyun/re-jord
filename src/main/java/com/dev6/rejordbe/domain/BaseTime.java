package com.dev6.rejordbe.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * BaseTime
 */
@lombok.Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTime {
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Seoul");

    @PrePersist
    public void prePersist() {
        createdDate = ZonedDateTime.now(ZONE_ID).toLocalDateTime();
        modifiedDate = ZonedDateTime.now(ZONE_ID).toLocalDateTime();
    }

    @PostPersist
    public void postPersist() {
        modifiedDate = ZonedDateTime.now(ZONE_ID).toLocalDateTime();
    }
}
