package com.example.secure.pay.qr.controller;

import com.example.secure.pay.qr.dto.PaymentResponseDTO;
import com.example.secure.pay.qr.model.Payment;
import com.example.secure.pay.qr.service.PaymentService;
import com.google.zxing.WriterException;
import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "Payment API")
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Yeni bir ödeme talebi oluşturur ve QR + link döner.
     */
    @Operation(
            summary = "Create a new payment",
            description = "Creates a new Stripe payment session based on the provided payment details, generates a dynamic QR code, and returns the payment link along with the QR code.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Payment successfully created",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResponseDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error while creating payment")
            }
    )
    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(
            @Parameter(description = "Payment details", required = true)
            @RequestBody Payment payment) throws IOException, WriterException, StripeException {
        PaymentResponseDTO paymentResponseDTO = paymentService.createPayment(payment);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponseDTO);
    }


    @Operation(
            summary = "Get QR code image by token",
            description = "Returns the PNG image of the QR code associated with the payment identified by the given token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "QR code image returned",
                            content = @Content(mediaType = "image/png")),
                    @ApiResponse(responseCode = "404", description = "Payment not found for the given token")
            }
    )
    @GetMapping(value = "/qr/{token}",produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getQrCodeImage(
            @Parameter(description = "Unique payment token", required = true)
            @PathVariable String token) {
        try {
            Payment payment = paymentService.getPaymentByToken(token);
            byte[] qrCodeBytes = payment.getQrCodeBytes();
            if (qrCodeBytes == null || qrCodeBytes.length == 0) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(qrCodeBytes);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
