package com.wanted.gold.order.dto;

import java.util.List;
import java.util.Map;

public record OrderListPaginationResponseDto<T>(
        boolean success,
        String message,
        List<T> data,
        Map<String, String> links
) {
}
