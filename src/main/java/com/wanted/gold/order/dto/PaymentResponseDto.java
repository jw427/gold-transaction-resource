package com.wanted.gold.order.dto;

import com.wanted.gold.order.domain.PaymentStatus;

import java.time.LocalDateTime;

public record PaymentResponseDto(
        PaymentStatus paymentStatus,
        LocalDateTime paymentAt,
        Long paymentAmount,
        String bankName,
        String bankAccount
) {
}
