package com.wanted.gold.order.dto;

import com.wanted.gold.order.domain.OrderStatus;
import com.wanted.gold.product.domain.GoldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderListResponseDto(
        OrderStatus orderStatus,
        Long totalPrice,
        BigDecimal quantity,
        LocalDateTime updatedAt,
        GoldType goldType
) {
}
