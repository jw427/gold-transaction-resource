package com.wanted.gold.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 기본
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    // 주문
    QUANTITY_TOO_MANY(HttpStatus.BAD_REQUEST, "주문할 수 없는 수량입니다. 수량을 다시 확인해주세요."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다. 다시 시도해주세요."),
    ORDER_DELETE_FAILED(HttpStatus.BAD_REQUEST, "주문을 삭제할 수 없습니다."),

    // 배송
    DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "배송 정보를 찾을 수 없습니다."),
    DELIVERY_FAILED(HttpStatus.CONFLICT, "배송을 진행할 수 없습니다. 다시 시도해주세요."),

    // 결제
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 정보를 찾을 수 없습니다."),
    PAYMENT_FAILED(HttpStatus.CONFLICT, "결제를 진행할 수 없습니다. 다시 시도해주세요."),
    PAYMENT_MODIFY_FAILED(HttpStatus.CONFLICT, "결제 정보를 수정할 수 없습니다. 다시 시도해주세요."),
    INVALID_PAYMENT_INFORMATION(HttpStatus.BAD_REQUEST, "결제 정보를 수정할 수 없습니다. 입력한 정보를 다시 확인해주세요."),

    // 상품
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다. 다시 시도해주세요."),

    // 가격
    PRICE_NOT_FOUND(HttpStatus.NOT_FOUND, "가격 정보를 찾을 수 없습니다. 죄송합니다.");

    private final HttpStatus status;
    private final String message;
}
