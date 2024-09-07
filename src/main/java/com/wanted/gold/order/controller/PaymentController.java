package com.wanted.gold.order.controller;

import com.wanted.gold.order.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    // 결제 상태 수정 - 입금 완료 또는 송금 완료
    @PatchMapping("/{paymentId}/complete")
    public ResponseEntity<String> updatePaymentStatus(@PathVariable Long paymentId) {
        String response = paymentService.completePayment(paymentId);
        return ResponseEntity.ok().body(response);
    }
}
