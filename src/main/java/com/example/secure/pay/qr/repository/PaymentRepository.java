package com.example.secure.pay.qr.repository;

import com.example.secure.pay.qr.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Optional<Payment> findByToken(String token);

    Optional<Payment> findBySessionId(String sessionId);

    Optional<Payment> findTopByStatusOrderByCreatedAtDesc(String status);

    List<Payment> findByStatus(String status);

    Optional<Payment> findByPaymentIntentId(String paymentIntentId);
}
