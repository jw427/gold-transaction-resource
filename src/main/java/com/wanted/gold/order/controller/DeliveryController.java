package com.wanted.gold.order.controller;

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
    public ResponseEntity<String> completeDelivery(@PathVariable Long deliveryId) {
        String response = deliveryService.completeDelivery(deliveryId);
        return ResponseEntity.ok().body(response);
    }

    // 배송 정보 수정
    @PatchMapping("/{deliveryId}")
    public ResponseEntity<String> modifyDelivery(@PathVariable Long deliveryId, @RequestBody ModifyDeliveryRequestDto requestDto) {
        String response = deliveryService.modifyDelivery(deliveryId, requestDto);
        return ResponseEntity.ok().body(response);
    }
}
