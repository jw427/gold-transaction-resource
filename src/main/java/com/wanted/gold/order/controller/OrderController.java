package com.wanted.gold.order.controller;

import com.wanted.gold.order.domain.Order;
import com.wanted.gold.order.domain.OrderType;
import com.wanted.gold.order.dto.CreateOrderRequestDto;
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
    public ResponseEntity<String> createOrder(@Valid @RequestBody CreateOrderRequestDto requestDto) {
        String response = orderService.createOrder(requestDto);
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
}
