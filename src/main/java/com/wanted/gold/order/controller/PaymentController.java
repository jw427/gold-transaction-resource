package com.wanted.gold.order.controller;

import com.wanted.gold.order.dto.ModifyPaymentRequestDto;
import com.wanted.gold.order.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    // 결제 상태 수정 - 입금 완료 또는 송금 완료
    @PatchMapping("/{paymentId}/complete")
    public ResponseEntity<String> completePayment(@PathVariable Long paymentId) {
        String response = paymentService.completePayment(paymentId);
        return ResponseEntity.ok().body(response);
    }

    // 결제 정보 수정
    @PatchMapping("/{paymentId}")
    public ResponseEntity<String> modifyPayment(@PathVariable Long paymentId, @RequestBody ModifyPaymentRequestDto requestDto) {
        String response = paymentService.modifyPayment(paymentId, requestDto);
        return ResponseEntity.ok().body(response);
    }
}
