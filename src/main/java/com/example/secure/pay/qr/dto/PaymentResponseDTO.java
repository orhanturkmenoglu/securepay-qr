package com.example.secure.pay.qr.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Response DTO returned after creating a payment")
public class PaymentResponseDTO {

    @Schema(description = "Stripe Checkout payment session URL", example = "https://checkout.stripe.com/pay/cs_test_123456789")
    private String checkoutUrl;

    @Schema(description = "Base64 encoded QR code image of the payment URL", example = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUg...")
    private String qrCodeBase64;

    @Schema(description = "Unique token associated with the payment", example = "e1f1b69c-24c7-4e56-9b98-f27a1d2e1234")
    private String token;
}
