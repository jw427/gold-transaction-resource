package com.wanted.gold.order.dto;

import com.wanted.gold.order.domain.DeliveryStatus;

import java.time.LocalDateTime;

public record DeliveryResponseDto(
        DeliveryStatus deliveryStatus,
        LocalDateTime deliveryAt,
        String address,
        String recipientName,
        String recipientPhone
) {
}
