package com.example.secure.pay.qr.service;

import com.example.secure.pay.qr.dto.PaymentResponseDTO;
import com.example.secure.pay.qr.model.Payment;
import com.example.secure.pay.qr.repository.PaymentRepository;
import com.example.secure.pay.qr.util.QRCodeGenerator;
import com.google.zxing.WriterException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.model.checkout.SessionCollection;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionListParams;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;


    /**
     * Yeni ödeme oluşturur, veritabanına kaydeder ve ödeme için QR kod üretir.
     */
    @Transactional
    public PaymentResponseDTO createPayment(Payment payment) throws IOException, WriterException, StripeException {
        log.info("Creating payment: {}", payment);

        Payment newPayment = buildNewPayment(payment);
        Session session = createStripeSession(newPayment);

        newPayment.setPaymentIntentId(session.getPaymentIntent());
        newPayment.setSessionId(session.getId());

        savePaymentWithQRCode(newPayment, session.getUrl());

        return buildPaymentResponse(session.getUrl(), newPayment.getQrCodeBytes());
    }

    private Payment buildNewPayment(Payment payment) {
        Payment newPayment = new Payment();
        newPayment.setToken(UUID.randomUUID().toString());
        newPayment.setAmount(payment.getAmount());
        newPayment.setCurrency(payment.getCurrency());
        newPayment.setStatus("PENDING");
        newPayment.setDescription(payment.getDescription());
        newPayment.setCreatedAt(Instant.now());
        return newPayment;
    }

    private Session createStripeSession(Payment payment) throws StripeException {
        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setDescription(payment.getDescription())
                        .setName("IPHONE 13")
                        .build();

        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency(payment.getCurrency())
                        .setUnitAmountDecimal(payment.getAmount())
                        .setProductData(productData)
                        .build();

        SessionCreateParams.LineItem lineItem =
                SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(priceData)
                        .build();

        SessionCreateParams params = SessionCreateParams.builder()
                .setSuccessUrl("http://localhost:8080/success")
                .setCancelUrl("http://localhost:8080/cancel")
                .addLineItem(lineItem)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .build();

        Session session = Session.create(params);
        log.info("Stripe session created with URL: {}", session.getUrl());

        return session;
    }

    private void savePaymentWithQRCode(Payment payment, String checkoutUrl) throws IOException, WriterException {
        byte[] qrBytes = QRCodeGenerator.generateQRCodeImage(checkoutUrl);
        payment.setQrCodeBytes(qrBytes);
        paymentRepository.save(payment);
        log.info("Payment saved with QR code");
    }

    private PaymentResponseDTO buildPaymentResponse(String checkoutUrl, byte[] qrCodeBytes) {
        String qrBase64 = "data:image/png;base64," + Base64.getEncoder().encodeToString(qrCodeBytes);
        return new PaymentResponseDTO(checkoutUrl, qrBase64);
    }


    /**
     * Tamamlanmış ödeme oturumlarının listesini getirir.
     */
    public List<Session> listCompletedSessions() throws StripeException {
        SessionListParams params = SessionListParams.builder()
                .setLimit(5L)
                .setStatus(SessionListParams.Status.COMPLETE)
                .build();

        SessionCollection sessions = Session.list(params);
        return sessions.getData();
    }

    /**
     * Token ile ödeme kaydını getirir.
     */
    public Payment getPaymentByToken(String token) {
        return paymentRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Payment not found for token: " + token));
    }

    /**
     * 30 saniyede bir bekleyen ödemelerin durumunu kontrol eder ve günceller.
     */
    @Scheduled(fixedDelay = 30000)
    public void checkPendingPayments() {
        Optional<Payment> pendingPaymentOpt = paymentRepository.findTopByStatusOrderByCreatedAtDesc("PENDING");

        if (pendingPaymentOpt.isEmpty()) {
            log.info("No PENDING payments found");
            return;
        }

        Payment payment = pendingPaymentOpt.get();

        try {
            String status = getPaymentStatusFromStripe(payment.getSessionId());

            if (isPaymentCompleted(status)) {
                payment.setStatus("PAID");
                paymentRepository.save(payment);

                // TODO: Websocket ve mail bildirimleri buraya eklenecek

                log.info("Payment {} marked as PAID", payment.getSessionId());
            } else {
                log.info("Payment {} current status: {}", payment.getSessionId(), status);
            }
        } catch (StripeException e) {
            log.error("Error checking payment status for session {}: {}", payment.getSessionId(), e.getMessage());
        }
    }

    private String getPaymentStatusFromStripe(String sessionId) throws StripeException {
        Session session = Session.retrieve(sessionId);
        PaymentIntent paymentIntent = PaymentIntent.retrieve(session.getPaymentIntent());
        return paymentIntent.getStatus();
    }

    private boolean isPaymentCompleted(String status) {
        return "succeeded".equalsIgnoreCase(status) || "paid".equalsIgnoreCase(status);
    }
}
