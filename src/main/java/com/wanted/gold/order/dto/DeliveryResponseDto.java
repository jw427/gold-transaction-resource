package com.wanted.gold.order.dto;

import com.wanted.gold.order.domain.DeliveryStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(title = "배송 정보 응답 DTO")
public record DeliveryResponseDto(
        @Schema(description = "배송 상태", example = "PENDING") DeliveryStatus deliveryStatus,
        @Schema(description = "배송 시각") LocalDateTime deliveryAt,
        @Schema(description = "주소", example = "서울시 영등포구 여의대방로") String address,
        @Schema(description = "수령인 이름", example = "이지원") String recipientName,
        @Schema(description = "수령인 전화번호", example = "01011112222")String recipientPhone
) {
}
