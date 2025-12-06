package com.xvitu.transferences.infrastructure;

import com.xvitu.transferences.domain.enums.TransferenceStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "transferences")
public class Transference {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferenceStatus status;

    @Column(nullable = false)
    private UUID payerId;

    @Column(nullable = false)
    private UUID payeeId;

    public Transference(BigDecimal amount, TransferenceStatus status, UUID payerId, UUID payeeId) {
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

    public UUID getPayerId() {
        return payerId;
    }

    public void setPayerId(UUID payerId) {
        this.payerId = payerId;
    }

    public UUID getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(UUID payeeId) {
        this.payeeId = payeeId;
    }
}
