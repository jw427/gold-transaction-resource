package com.wanted.gold.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "배송 정보 수정 요청 DTO")
public record ModifyDeliveryRequestDto(
        @Schema(description = "주소", example = "서울시 영등포구 의사당대로") String address,
        @Schema(description = "수령인 이름", example = "이지삼") String recipientName,
        @Schema(description = "수령인 전화번호", example = "01033334444") String recipientPhone
) {
}
