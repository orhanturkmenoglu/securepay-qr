package com.example.secure.pay.qr.controller;

import com.example.secure.pay.qr.dto.PaymentResponseDTO;
import com.example.secure.pay.qr.model.Payment;
import com.example.secure.pay.qr.repository.PaymentRepository;
import com.example.secure.pay.qr.service.PaymentService;
import com.example.secure.pay.qr.util.QRCodeGenerator;
import com.google.zxing.WriterException;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentRepository  paymentRepository;

    /**
     * Yeni bir ödeme talebi oluşturur ve QR + link döner.
     */
    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(@RequestBody Payment payment) throws IOException, WriterException, StripeException {
        PaymentResponseDTO paymentResponseDTO = paymentService.createPayment(payment);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponseDTO);
    }

    @GetMapping(value = "/qr/{token}",produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getQrCodeImage(@PathVariable String token) throws IOException, WriterException {

        Payment payment = paymentService.getPaymentByToken(token);

        byte[] qrCodeBytes = payment.getQrCodeBytes();

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(qrCodeBytes);
    }

}
