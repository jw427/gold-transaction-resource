package com.wanted.gold.order.dto;

import com.wanted.gold.order.domain.OrderStatus;
import com.wanted.gold.order.domain.OrderType;
import com.wanted.gold.product.domain.GoldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderDetailResponseDto(
        OrderType orderType,
        OrderStatus orderStatus,
        Long totalPrice,
        BigDecimal quantity,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        GoldType goldType,
        DeliveryResponseDto deliveryResponseDto,
        PaymentResponseDto paymentResponseDto
) {
}
