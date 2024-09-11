package com.wanted.gold.order.dto;

import com.wanted.gold.order.domain.OrderType;
import com.wanted.gold.product.domain.GoldType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(title = "주문 생성 요청 DTO")
public record CreateOrderRequestDto(
        @NotNull(message = "주문 유형을 입력해주세요.") @Schema(description = "주문 유형") OrderType orderType,
        @NotNull(message = "수량을 입력해주세요. 수량의 단위는 g(그램)으로, 소수 둘째자리까지 입력할 수 있습니다.") @Schema(description = "수량(그램)", example = "10") BigDecimal quantity,
        @NotNull(message = "품목을 입력해주세요.") @Schema(description = "금 종류") GoldType goldType,
        @NotBlank(message = "배송 주소를 작성해주세요.") @Schema(description = "주소", example = "서울시 영등포구 여의대방로") String address,
        @NotBlank(message = "수령인 이름을 작성해주세요.") @Schema(description = "수령인 이름", example = "이지원") String recipientName,
        @NotBlank(message = "수령인 전화번호를 작성해주세요.") @Schema(description = "수령인 전화번호", example = "01011112222") String recipientPhone,
        @NotBlank(message = "은행 이름을 입력해주세요.") @Schema(description = "은행 이름", example = "포스뱅크") String bankName,
        @NotBlank(message = "계좌번호를 입력해주세요.") @Schema(description = "계좌번호", example = "098-76-543210") String bankAccount
) {
}
