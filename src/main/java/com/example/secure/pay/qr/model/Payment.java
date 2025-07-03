package com.example.secure.pay.qr.model;

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

    @Column(name = "payment_intent_id")
    private String paymentIntentId;

    private String token;

    private BigDecimal amount;

    private String currency;

    private String description;

    private String status;

    @Lob
    private byte [] qrCodeBytes;

    private Instant createdAt;
}
