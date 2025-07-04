package com.example.secure.pay.qr.dto;

import com.example.secure.pay.qr.enums.CurrencyType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO containing information required to create a payment")
public class PaymentRequestDTO {

    @Schema(description = "Payment amount in the smallest currency unit (e.g., cents)", example = "10000", required = true)
    private BigDecimal amount;

    @Schema(description = "Currency code in ISO 4217 format", example = "TRY", required = true)
    private CurrencyType currency;

    @Schema(description = "Description for the payment", example = "IPHONE 13", required = true)
    private String description;
}
