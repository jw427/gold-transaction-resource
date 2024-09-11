package com.wanted.gold.order.controller;

import com.wanted.gold.exception.ErrorCode;
import com.wanted.gold.exception.UnauthorizedException;
import com.wanted.gold.order.dto.ModifyPaymentRequestDto;
import com.wanted.gold.order.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "Payment API")
public class PaymentController {
    private final PaymentService paymentService;

    // 결제 상태 수정 - 입금 완료 또는 송금 완료
    @PatchMapping("/{paymentId}/complete")
    @Operation(summary = "결제 상태 완료로 수정", description = "결제 상태를 완료로 수정 시 사용하는 API\n" +
            "- Authorize에 액세스 토큰 값을 넣고 진행해주세요.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = {@Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "입금이 완료됐습니다."))})
    public ResponseEntity<String> completePayment(
            @Parameter(description = "상태 수정할 결제 식별번호", example = "14") @PathVariable Long paymentId,
            @RequestHeader(value = "Authorization", required = false) String token) {
        if(token == null || !token.startsWith("Bearer "))
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        String accessToken = token.split("Bearer ")[1];
        String response = paymentService.completePayment(paymentId, accessToken);
        return ResponseEntity.ok().body(response);
    }

    // 결제 정보 수정
    @PatchMapping("/{paymentId}")
    @Operation(summary = "결제 정보 수정", description = "결제 정보 수정 시 사용하는 API\n" +
            "- Authorize에 액세스 토큰 값을 넣고 진행해주세요.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = {@Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "결제 정보 수정을 완료했습니다."))})
    public ResponseEntity<String> modifyPayment(
            @Parameter(description = "정보 수정할 결제 식별번호", example = "14") @PathVariable Long paymentId,
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody ModifyPaymentRequestDto requestDto) {
        if(token == null || !token.startsWith("Bearer "))
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        String accessToken = token.split("Bearer ")[1];
        String response = paymentService.modifyPayment(paymentId, accessToken, requestDto);
        return ResponseEntity.ok().body(response);
    }
}
