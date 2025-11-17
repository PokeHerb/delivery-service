package org.pokeherb.deliveryservice.global.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {

    @CreatedDate
    @Column(updatable = false)
    protected LocalDateTime createdAt;

    @CreatedBy
    @Column(updatable = false)
    protected String createdBy;

    @LastModifiedDate
    protected LocalDateTime updatedAt;

    @LastModifiedBy
    protected String updatedBy;

    protected LocalDateTime deletedAt;

    protected String deletedBy;

    public void softDelete(String username) {
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = username;
    }
}