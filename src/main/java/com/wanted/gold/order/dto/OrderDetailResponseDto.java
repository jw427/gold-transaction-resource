package com.wanted.gold.order.dto;

import com.wanted.gold.order.domain.OrderStatus;
import com.wanted.gold.order.domain.OrderType;
import com.wanted.gold.product.domain.GoldType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(title = "주문 상세 조회 응답 DTO")
public record OrderDetailResponseDto(
        @Schema(description = "주문 유형", example = "PURCHASE") OrderType orderType,
        @Schema(description = "주문 상태", example = "ORDER_COMPLETED") OrderStatus orderStatus,
        @Schema(description = "주문 가격", example = "1700") Long totalPrice,
        @Schema(description = "수량(그램)", example = "10") BigDecimal quantity,
        @Schema(description = "주문 생성 시각") LocalDateTime createdAt,
        @Schema(description = "주문 상태 업데이트 시각") LocalDateTime updatedAt,
        @Schema(description = "금 종류", example = "GOLD_999") GoldType goldType,
        DeliveryResponseDto deliveryResponseDto,
        PaymentResponseDto paymentResponseDto
) {
}
