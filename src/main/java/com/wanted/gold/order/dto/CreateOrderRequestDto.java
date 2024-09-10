package com.wanted.gold.order.dto;

import com.wanted.gold.order.domain.OrderType;
import com.wanted.gold.product.domain.GoldType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateOrderRequestDto(
        @NotNull OrderType orderType,
        @NotNull BigDecimal quantity,
        @NotNull GoldType goldType,
        @NotBlank String address,
        @NotBlank String recipientName,
        @NotBlank String recipientPhone,
        @NotBlank String bankName,
        @NotBlank String bankAccount
) {
}
