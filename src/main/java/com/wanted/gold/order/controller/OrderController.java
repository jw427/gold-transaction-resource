package com.wanted.gold.order.controller;

import com.wanted.gold.exception.ErrorCode;
import com.wanted.gold.exception.UnauthorizedException;
import com.wanted.gold.order.domain.Order;
import com.wanted.gold.order.domain.OrderType;
import com.wanted.gold.order.dto.CreateOrderRequestDto;
import com.wanted.gold.order.dto.OrderDetailResponseDto;
import com.wanted.gold.order.dto.OrderListPaginationResponseDto;
import com.wanted.gold.order.dto.OrderListResponseDto;
import com.wanted.gold.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Order API")
public class OrderController {
    private final OrderService orderService;

    // 주문 생성
    @PostMapping("")
    @Operation(summary = "주문 생성", description = "주문 생성 시 사용하는 API\n" +
            "- Authorize에 액세스 토큰 값을 넣고 진행해주세요.")
    @ApiResponse(responseCode = "201", description = "Created",
            content = {@Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "주문에 성공했습니다."))})
    public ResponseEntity<String> createOrder(@RequestHeader(value = "Authorization", required = false) String token, @Valid @RequestBody CreateOrderRequestDto requestDto) {
        if(token == null || !token.startsWith("Bearer "))
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        String accessToken = token.split("Bearer ")[1];
        String response = orderService.createOrder(accessToken, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 주문 전체 목록 조회
    @GetMapping("")
    @Operation(summary = "주문 목록 조회", description = "주문 목록 조회 시 사용하는 API\n" +
            "- Authorize에 액세스 토큰 값을 넣고 진행해주세요.")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<OrderListPaginationResponseDto<OrderListResponseDto>> orderList(
            @Parameter(description = "조회할 날짜", example = "2024-09-11") @RequestParam(required = true) LocalDate date,
            @Parameter(description = "주문 유형") @RequestParam(required = true) OrderType type,
            @Parameter(description = "현재 페이지 번호 (기본값 0)") @RequestParam(defaultValue = "0") int offset,
            @Parameter(description = "페이지 당 주문 조회 개수 (기본값 10)") @RequestParam(defaultValue = "10") int limit,
            @RequestHeader(value = "Authorization", required = false) String token) {
        if(token == null || !token.startsWith("Bearer "))
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        String accessToken = token.split("Bearer ")[1];
        OrderListPaginationResponseDto<OrderListResponseDto> orderPage = orderService.getOrders(date, type, offset, limit, accessToken);
        // 주문이 하나도 없을 경우
        if(orderPage.data().size() == 0)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return ResponseEntity.ok().body(orderPage);
    }

    // 주문 상세 조회
    @GetMapping("/{orderId}")
    @Operation(summary = "주문 상세 조회", description = "주문 상세 조회 시 사용하는 API\n" +
            "- Authorize에 액세스 토큰 값을 넣고 진행해주세요.")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<OrderDetailResponseDto> getOrder(
            @Parameter(description = "조회할 주문 식별번호", example = "14") @PathVariable Long orderId,
            @RequestHeader(value = "Authorization", required = false) String token) {
        if(token == null || !token.startsWith("Bearer "))
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        String accessToken = token.split("Bearer ")[1];
        OrderDetailResponseDto responseDto = orderService.getOrder(orderId, accessToken);
        return ResponseEntity.ok().body(responseDto);
    }

    // 주문 삭제
    @DeleteMapping("/{orderId}")
    @Operation(summary = "주문 삭제", description = "주문 삭제 시 사용하는 API\n" +
            "- Authorize에 액세스 토큰 값을 넣고 진행해주세요.")
    @ApiResponse(responseCode = "204", description = "No Content")
    public ResponseEntity<Void> deleteOrder(
            @Parameter(description = "삭제할 주문 식별번호", example = "14") @PathVariable Long orderId,
            @RequestHeader(value = "Authorization", required = false) String token) {
        if(token == null || !token.startsWith("Bearer "))
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        String accessToken = token.split("Bearer ")[1];
        orderService.deleteOrder(orderId, accessToken);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
