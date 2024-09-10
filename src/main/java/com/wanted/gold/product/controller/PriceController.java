package com.wanted.gold.product.controller;

import com.wanted.gold.exception.ErrorCode;
import com.wanted.gold.exception.UnauthorizedException;
import com.wanted.gold.product.dto.CreatePriceRequestDto;
import com.wanted.gold.product.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prices")
@RequiredArgsConstructor
public class PriceController {
    private final PriceService priceService;

    // 가격 등록
    @PostMapping("")
    public ResponseEntity<String> registerPrice(@RequestHeader(value = "Authorization") String token, @RequestBody CreatePriceRequestDto requestDto) {
        if(token == null || !token.startsWith("Bearer "))
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        String accessToken = token.split("Bearer ")[1];
        String response = priceService.registerPrice(accessToken, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
