package com.dev6.rejordbe.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;
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

    @PrePersist
    public void prePersist() {
        createdDate = ZonedDateTime.now().toLocalDateTime();
        modifiedDate = ZonedDateTime.now().toLocalDateTime();
    }

    @PostPersist
    public void postPersist() {
        modifiedDate = ZonedDateTime.now().toLocalDateTime();
    }
}
