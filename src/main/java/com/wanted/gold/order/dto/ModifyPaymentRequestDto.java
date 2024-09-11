package com.wanted.gold.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "결제 정보 수정 요청 DTO")
public record ModifyPaymentRequestDto(
        @Schema(description = "은행 이름", example = "대방은행") String bankName,
        @Schema(description = "은행 계좌 번호", example = "333-44-555555") String bankAccount
) {
}
