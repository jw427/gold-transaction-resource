package com.wanted.gold.order.dto;

import com.wanted.gold.order.domain.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(title = "결제 정보 응답 DTO")
public record PaymentResponseDto(
        @Schema(description = "결제 상태", example = "PENDING") PaymentStatus paymentStatus,
        @Schema(description = "결제 시각") LocalDateTime paymentAt,
        @Schema(description = "결제 금액", example = "1700") Long paymentAmount,
        @Schema(description = "은행 이름", example = "포스뱅크") String bankName,
        @Schema(description = "은행 계좌 번호", example = "098-76-543210") String bankAccount
) {
}
