package com.example.secure.pay.qr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {

    private String paymentUrl;
    private String qrCodeBase64;
}
