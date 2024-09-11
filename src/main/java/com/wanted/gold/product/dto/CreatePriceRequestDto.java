package com.wanted.gold.product.dto;

import com.wanted.gold.order.domain.OrderType;
import com.wanted.gold.product.domain.GoldType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(title = "가격 등록 요청 DTO")
public record CreatePriceRequestDto(
        @NotNull(message = "주문 유형을 입력해주세요.") @Schema(description = "주문 유형") OrderType orderType,
        @NotNull(message = "그램 당 가격을 입력해주세요.") @Schema(description = "그램 당 가격", example = "100") Long pricePerGram,
        @NotBlank(message = "설명을 작성해주세요.") @Schema(description = "설명", example = "가격 변동") String content,
        @NotNull(message = "품목을 입력해주세요.") @Schema(description = "금 종류") GoldType goldType
        ) {
}
