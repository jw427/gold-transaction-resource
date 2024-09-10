package com.wanted.gold.order.dto;

public record ModifyDeliveryRequestDto(
        String address,
        String recipientName,
        String recipientPhone
) {
}
