package com.wanted.gold.order.service;

import com.wanted.gold.exception.BadRequestException;
import com.wanted.gold.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderValidator {
    private static final String REGEX = "^\\d+(\\.\\d{1,2})?$";
    // 수량 소수 2번째 자리까지
    public void validateQuantity(BigDecimal quantity) {
        if(quantity != null && !quantity.toPlainString().matches(REGEX))
            throw new BadRequestException(ErrorCode.ORDER_QUANTITY_INVALID);
    }
}
