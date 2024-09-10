package com.wanted.gold.order.controller;

import com.wanted.gold.exception.ErrorCode;
import com.wanted.gold.exception.UnauthorizedException;
import com.wanted.gold.order.domain.Order;
import com.wanted.gold.order.domain.OrderType;
import com.wanted.gold.order.dto.CreateOrderRequestDto;
import com.wanted.gold.order.dto.OrderDetailResponseDto;
import com.wanted.gold.order.dto.OrderListPaginationResponseDto;
import com.wanted.gold.order.dto.OrderListResponseDto;
import com.wanted.gold.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    // 주문 생성
    @PostMapping("")
    public ResponseEntity<String> createOrder(@RequestHeader(value = "Authorization") String token, @Valid @RequestBody CreateOrderRequestDto requestDto) {
        if(token == null || !token.startsWith("Bearer "))
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        String accessToken = token.split("Bearer ")[1];
        String response = orderService.createOrder(accessToken, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 주문 전체 목록 조회
    @GetMapping("")
    public ResponseEntity<OrderListPaginationResponseDto<OrderListResponseDto>> orderList(@RequestParam(required = true) LocalDate date,
                                                                                          @RequestParam(required = true) OrderType type,
                                                                                          @RequestParam(defaultValue = "0") int offset,
                                                                                          @RequestParam(defaultValue = "10") int limit) {
        OrderListPaginationResponseDto<OrderListResponseDto> orderPage = orderService.getOrders(date, type, offset, limit);
        // 주문이 하나도 없을 경우
        if(orderPage.data().size() == 0)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return ResponseEntity.ok().body(orderPage);
    }

    // 주문 상세 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponseDto> getOrder(@PathVariable Long orderId) {
        OrderDetailResponseDto responseDto = orderService.getOrder(orderId);
        return ResponseEntity.ok().body(responseDto);
    }

    // 주문 삭제
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
