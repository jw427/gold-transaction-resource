package com.wanted.gold.order.controller;

import com.wanted.gold.exception.ErrorCode;
import com.wanted.gold.exception.UnauthorizedException;
import com.wanted.gold.order.dto.ModifyDeliveryRequestDto;
import com.wanted.gold.order.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;

    // 배송 상태 수정 - 발송 완료 또는 수령 완료
    @PatchMapping("/{deliveryId}/complete")
    public ResponseEntity<String> completeDelivery(@PathVariable Long deliveryId, @RequestHeader(value = "Authorization") String token) {
        if(token == null || !token.startsWith("Bearer "))
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        String accessToken = token.split("Bearer ")[1];
        String response = deliveryService.completeDelivery(deliveryId, accessToken);
        return ResponseEntity.ok().body(response);
    }

    // 배송 정보 수정
    @PatchMapping("/{deliveryId}")
    public ResponseEntity<String> modifyDelivery(@PathVariable Long deliveryId, @RequestHeader(value = "Authorization") String token, @RequestBody ModifyDeliveryRequestDto requestDto) {
        if(token == null || !token.startsWith("Bearer "))
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        String accessToken = token.split("Bearer ")[1];
        String response = deliveryService.modifyDelivery(deliveryId, accessToken, requestDto);
        return ResponseEntity.ok().body(response);
    }
}
