package com.wanted.gold.order.controller;

import com.wanted.gold.exception.ErrorCode;
import com.wanted.gold.exception.UnauthorizedException;
import com.wanted.gold.order.dto.ModifyDeliveryRequestDto;
import com.wanted.gold.order.service.DeliveryService;
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
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
@Tag(name = "Delivery", description = "Delivery API")
public class DeliveryController {
    private final DeliveryService deliveryService;

    // 배송 상태 수정 - 발송 완료 또는 수령 완료
    @PatchMapping("/{deliveryId}/complete")
    @Operation(summary = "배송 상태 완료로 수정", description = "배송 상태를 완료로 수정 시 사용하는 API\n" +
            "- Authorize에 액세스 토큰 값을 넣고 진행해주세요.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = {@Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "발송 완료. 이용해주셔서 감사합니다."))})
    public ResponseEntity<String> completeDelivery(
            @Parameter(description = "상태 수정할 배송 식별번호", example = "14") @PathVariable Long deliveryId,
            @RequestHeader(value = "Authorization", required = false) String token) {
        if(token == null || !token.startsWith("Bearer "))
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        String accessToken = token.split("Bearer ")[1];
        String response = deliveryService.completeDelivery(deliveryId, accessToken);
        return ResponseEntity.ok().body(response);
    }

    // 배송 정보 수정
    @PatchMapping("/{deliveryId}")
    @Operation(summary = "배송 정보 수정", description = "배송 정보 수정 시 사용하는 API\n" +
            "- Authorize에 액세스 토큰 값을 넣고 진행해주세요.")
    @ApiResponse(responseCode = "200", description = "OK",
            content = {@Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "배송 정보 수정을 완료했습니다."))})
    public ResponseEntity<String> modifyDelivery(
            @Parameter(description = "정보 수정할 배송 식별번호", example = "14") @PathVariable Long deliveryId,
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody ModifyDeliveryRequestDto requestDto) {
        if(token == null || !token.startsWith("Bearer "))
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        String accessToken = token.split("Bearer ")[1];
        String response = deliveryService.modifyDelivery(deliveryId, accessToken, requestDto);
        return ResponseEntity.ok().body(response);
    }
}
