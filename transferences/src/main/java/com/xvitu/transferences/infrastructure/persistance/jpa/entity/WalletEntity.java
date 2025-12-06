package com.xvitu.transferences.infrastructure.persistance.jpa.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "wallets")
public class WalletEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal availableAmount;

    public WalletEntity(UUID id, BigDecimal availableAmount, Integer userId) {
        this.id = id;
        this.userId = userId;
        this.availableAmount = availableAmount;
    }

    public UUID getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
    }
}
