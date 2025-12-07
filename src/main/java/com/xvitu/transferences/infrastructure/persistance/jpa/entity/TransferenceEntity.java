package com.xvitu.transferences.infrastructure.persistance.jpa.entity;

import com.xvitu.transferences.domain.enums.TransferenceStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "transferences")
public class TransferenceEntity {

    @Id
    private UUID id;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferenceStatus status;

    @Column(nullable = false)
    private Integer payerId;

    @Column(nullable = false)
    private Integer payeeId;

    public TransferenceEntity() {}

    public TransferenceEntity(
            UUID id,
            BigDecimal amount,
            TransferenceStatus status,
            Integer payerId,
            Integer payeeId
    ) {
        this.id = id;
        this.amount = amount;
        this.status = status;
        this.payerId = payerId;
        this.payeeId = payeeId;
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransferenceStatus getStatus() {
        return status;
    }

    public void setStatus(TransferenceStatus status) {
        this.status = status;
    }

    public Integer getPayerId() {
        return payerId;
    }

    public void setPayerId(Integer payerId) {
        this.payerId = payerId;
    }

    public Integer getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(Integer payeeId) {
        this.payeeId = payeeId;
    }
}
