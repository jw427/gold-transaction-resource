package com.wanted.gold.product.controller;

import com.wanted.gold.exception.ErrorCode;
import com.wanted.gold.exception.UnauthorizedException;
import com.wanted.gold.product.dto.CreatePriceRequestDto;
import com.wanted.gold.product.service.PriceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prices")
@RequiredArgsConstructor
@Tag(name = "Price", description = "Price API")
public class PriceController {
    private final PriceService priceService;

    // 가격 등록
    @PostMapping("")
    @Operation(summary = "가격 등록", description = "가격 등록 시 사용하는 API\n" +
            "- Authorize에 액세스 토큰 값을 넣고 진행해주세요.")
    @ApiResponse(responseCode = "201", description = "Created",
            content = {@Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "등록이 완료됐습니다."))})
    public ResponseEntity<String> registerPrice(@RequestHeader(value = "Authorization", required = false) String token, @RequestBody CreatePriceRequestDto requestDto) {
        if(token == null || !token.startsWith("Bearer "))
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        String accessToken = token.split("Bearer ")[1];
        String response = priceService.registerPrice(accessToken, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
