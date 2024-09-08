package com.wanted.gold.product.controller;

import com.wanted.gold.product.dto.CreatePriceRequestDto;
import com.wanted.gold.product.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prices")
@RequiredArgsConstructor
public class PriceController {
    private final PriceService priceService;

    // 가격 등록
    @PostMapping("")
    public ResponseEntity<String> registerPrice(@RequestBody CreatePriceRequestDto requestDto) {
        String response = priceService.registerPrice(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
