package com.wanted.gold.order.controller;

import com.wanted.gold.order.dto.CreateOrderRequestDto;
import com.wanted.gold.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
