package com.wanted.gold.order.controller;

import com.wanted.gold.order.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
