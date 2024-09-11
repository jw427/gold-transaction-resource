package com.wanted.gold.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Schema(title = "주문 목록 조회 페이지네이션 응답 DTO")
public record OrderListPaginationResponseDto<T>(
        @Schema(description = "응답 여부", example = "true") boolean success,
        @Schema(description = "응답 메시지", example = "Success to search orders") String message,
        @Schema(description = "주문 목록 페이지네이션") List<T> data,
        @Schema(description = "페이지 링크") Map<String, String> links
) {
}
