package com.wanted.gold.product.dto;

import com.wanted.gold.order.domain.OrderType;
import com.wanted.gold.product.domain.GoldType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePriceRequestDto(
        @NotNull OrderType orderType,
        @NotNull Long pricePerGram,
        @NotBlank String content,
        @NotNull GoldType goldType
        ) {
}
