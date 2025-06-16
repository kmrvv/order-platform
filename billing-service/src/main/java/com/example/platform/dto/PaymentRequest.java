package com.example.platform.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    @NotNull(message = "orderId cannot be null")
    private Long orderId;

    @NotNull(message = "orderId cannot be null")
    @Min(value = 1, message = "Amount must be greater than zero")
    private BigDecimal amount;
}
