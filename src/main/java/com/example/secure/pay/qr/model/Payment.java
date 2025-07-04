package com.example.secure.pay.qr.model;

import com.example.secure.pay.qr.enums.CurrencyType;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Table(name = "payment")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "session_id")
    private String sessionId;

    private String token;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private CurrencyType currency;

    private String description;

    private String status;

    @Lob
    private byte [] qrCodeBytes;

    private Instant createdAt;

    private Instant updatedAt;


    @PrePersist
    public void init() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }
}
