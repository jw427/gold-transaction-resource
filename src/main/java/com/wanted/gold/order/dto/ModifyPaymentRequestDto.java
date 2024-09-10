package com.wanted.gold.order.dto;

public record ModifyPaymentRequestDto(
        String bankName,
        String bankAccount
) {
}
