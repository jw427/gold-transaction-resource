package com.wanted.gold.order.service;

import com.wanted.gold.client.AuthGrpcClient;
import com.wanted.gold.client.dto.UserResponseDto;
import com.wanted.gold.exception.*;
import com.wanted.gold.order.domain.*;
import com.wanted.gold.order.dto.*;
import com.wanted.gold.order.repository.DeliveryRepository;
import com.wanted.gold.order.repository.OrderRepository;
import com.wanted.gold.order.repository.PaymentRepository;
import com.wanted.gold.product.domain.Price;
import com.wanted.gold.product.domain.Product;
import com.wanted.gold.product.repository.PriceRepository;
import com.wanted.gold.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final PriceRepository priceRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final AuthGrpcClient authGrpcClient;
    private final OrderValidator orderValidator;

    // 주문 생성
    @Transactional
    public String createOrder(String accessToken, CreateOrderRequestDto requestDto) {
        // 수량 소수점 검증하기
        orderValidator.validateQuantity(requestDto.quantity());
        // 액세스토큰으로 회원 정보 가져오기
        UserResponseDto userResponseDto = authGrpcClient.getUserIdAndRole(accessToken);
        // 회원 식별번호 가져오기
        String userIdStr = userResponseDto.userId();
        // String -> UUID 변환
        UUID userId = UUID.fromString(userIdStr);
        // 입력받은 금 종류로 product 찾기
        Product product = productRepository.findByGoldType(requestDto.goldType())
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
        // OrderType = PURCHASE(구매)의 경우 주문 수량이 재고보다 클 경우 error
        if(requestDto.orderType() == OrderType.PURCHASE && (requestDto.quantity().compareTo(product.getStock()) > 0))
            throw new BadRequestException(ErrorCode.QUANTITY_TOO_MANY);
        // OrderType = PURCHASE(구매)의 경우 주문 수량이 재고보다 같거나 작을 경우 상품 재고 차감
        if(requestDto.orderType() == OrderType.PURCHASE) {
            product.deductProductStock(requestDto.quantity());
        }
        // product와 주문 유형으로 product 가격 찾기 - 가장 최근에 등록된 값
        Price price = priceRepository.findTopByOrderTypeAndProduct_ProductIdOrderByCreatedAtDesc(requestDto.orderType(), product.getProductId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRICE_NOT_FOUND));
        // Price의 그램 당 가격과 입력받은 수량으로 총 주문가격 계산
        Long totalPrice = calculateTotalPrice(requestDto.quantity(), price.getPricePerGram());
        // 주문 생성
        Order order = Order.builder()
                .orderType(requestDto.orderType())
                .orderStatus(OrderStatus.ORDER_COMPLETED)
                .totalPrice(totalPrice)
                .quantity(requestDto.quantity())
                .createdAt(LocalDateTime.now())
                .userId(userId)
                .product(product)
                .build();
        orderRepository.save(order);
        // 배송 생성
        Delivery delivery = Delivery.builder()
                .deliveryStatus(DeliveryStatus.PENDING)
                .address(requestDto.address())
                .recipientName(requestDto.recipientName())
                .recipientPhone(requestDto.recipientPhone())
                .order(order)
                .build();
        deliveryRepository.save(delivery);
        // 결제 생성
        Payment payment = Payment.builder()
                .paymentStatus(PaymentStatus.PENDING)
                .paymentAmount(totalPrice)
                .bankName(requestDto.bankName())
                .bankAccount(requestDto.bankAccount())
                .order(order)
                .build();
        paymentRepository.save(payment);
        // 주문 성공 메시지 반환
        return "주문에 성공했습니다.";
    }

    // 수량과 그램 당 가격으로 총 주문 가격 계산
    public Long calculateTotalPrice(BigDecimal quantity, Long pricePerGram) {
        BigDecimal priceDecimal = BigDecimal.valueOf(pricePerGram);
        BigDecimal totalPriceDecimal = quantity.multiply(priceDecimal);
        // 소수점 아래 버리고 Long 타입으로 변환
        return totalPriceDecimal.longValue();
    }

    // 주문 전체 목록 조회
    @Transactional(readOnly = true)
    public OrderListPaginationResponseDto<OrderListResponseDto> getOrders(LocalDate date, OrderType type, int offset, int limit, String accessToken) {
        // 액세스토큰으로 회원 정보 가져오기
        UserResponseDto userResponseDto = authGrpcClient.getUserIdAndRole(accessToken);
        Page<Order> orders;
        // 페이지 번호 offset과 한 페이지당 출력 개수인 limit
        Pageable pageable = PageRequest.of(offset, limit);
        // 관리자인 경우 모든 주문 목록 조회
        if(userResponseDto.role().equals("ROLE_ADMIN")) {
            orders = orderRepository.findByCreatedAtDateAndOrderType(date, type, pageable);
            // 일반 회원인 경우 해당 회원의 주문 목록만 조회
        } else {
            UUID userId = UUID.fromString(userResponseDto.userId());
            orders = orderRepository.findByCreatedAtAndOrderTypeAndUserId(date, type, userId, pageable);
        }
        // dto 변환
        List<OrderListResponseDto> orderListResponseDtoList = orders.stream()
                .map(list -> new OrderListResponseDto(list.getOrderStatus(), list.getTotalPrice(), list.getQuantity(), list.getUpdatedAt(), list.getProduct().getGoldType()))
                .collect(Collectors.toList());
        // 페이지 링크 추가한 response로 변환
        OrderListPaginationResponseDto<OrderListResponseDto> paginationResponseDto = new OrderListPaginationResponseDto<>(
                true,
                "Success to search orders",
                orderListResponseDtoList,
                getPaginationLinks(orders, date, type)
        );
        return paginationResponseDto;
    }

    public Map<String, String> getPaginationLinks(Page<Order> page, LocalDate date, OrderType type) {
        Map<String, String> links = new HashMap<>();
        // 현재 페이지 번호와 페이지 크기
        int currentPage = page.getNumber();
        int pageSize = page.getSize();

        // 다음 페이지 링크
        if(page.hasNext()) {
            links.put("next", String.format("/orders?date=%s&type=%s&offset=%d&limit=%d",
                    date, type, currentPage + 1, pageSize));
        }
        // 이전 페이지 링크
        if(page.hasPrevious()) {
            links.put("prev", String.format("/orders?date=%s&type=%s&offset=%d&limit=%d",
                    date, type, currentPage - 1, pageSize));
        }

        return links;
    }

    // 주문 상세 조회
    public OrderDetailResponseDto getOrder(Long orderId, String accessToken) {
        // 액세스토큰으로 회원 정보 가져오기
        UserResponseDto userResponseDto = authGrpcClient.getUserIdAndRole(accessToken);
        // 주문 식별번호로 Order 객체 찾기
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));
        // 일반 회원의 경우 본인 주문이 아닐 경우 조회 불가
        if(userResponseDto.role().equals("ROLE_MEMBER") && !UUID.fromString(userResponseDto.userId()).equals(order.getUserId()))
            throw new ForbiddenException(ErrorCode.FORBIDDEN);
        // 주문 식별번호로 Delivery 객체 찾기
        Delivery delivery = deliveryRepository.findTopByOrder_OrderIdOrderByDeliveryIdDesc(orderId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DELIVERY_NOT_FOUND));
        // Delivery -> DeliveryResponseDto로 변환
        DeliveryResponseDto deliveryResponseDto = new DeliveryResponseDto(
                delivery.getDeliveryStatus(),
                delivery.getDeliveryAt(),
                delivery.getAddress(),
                delivery.getRecipientName(),
                delivery.getRecipientPhone()
        );
        // 주문 식별번호로 Payment 객체 찾기
        Payment payment = paymentRepository.findTopByOrder_OrderIdOrderByPaymentIdDesc(orderId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PAYMENT_NOT_FOUND));
        // Payment -> PaymentResponseDto로 변환
        PaymentResponseDto paymentResponseDto = new PaymentResponseDto(
                payment.getPaymentStatus(),
                payment.getPaymentAt(),
                payment.getPaymentAmount(),
                payment.getBankName(),
                payment.getBankAccount()
        );
        // OrderDetailResponseDto로 변환
        OrderDetailResponseDto orderDetailResponseDto = new OrderDetailResponseDto(
                order.getOrderType(),
                order.getOrderStatus(),
                order.getTotalPrice(),
                order.getQuantity(),
                order.getCreatedAt(),
                order.getUpdatedAt(),
                order.getProduct().getGoldType(),
                deliveryResponseDto,
                paymentResponseDto
        );
        // 결과값 반환
        return orderDetailResponseDto;
    }

    // 주문 삭제
    @Transactional
    public void deleteOrder(Long orderId, String accessToken) {
        // 액세스토큰으로 회원 정보 가져오기
        UserResponseDto userResponseDto = authGrpcClient.getUserIdAndRole(accessToken);
        // 주문 식별번호로 Order 객체 찾기
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));
        // 본인 주문이 아닐 경우 삭제 불가
        if(!UUID.fromString(userResponseDto.userId()).equals(order.getUserId()))
            throw new ForbiddenException(ErrorCode.FORBIDDEN);
        // 주문 상태가 입금 또는 송금이 완료된 상태일 경우 삭제 불가 - 주문만 한 상태이거나 배송까지 완료됐을 때는 삭제 가능
        if(order.getOrderStatus() == OrderStatus.PAYMENT_COMPLETED || order.getOrderStatus() == OrderStatus.PAYMENT_SENT)
            throw new BadRequestException(ErrorCode.ORDER_DELETE_FAILED);
        // 주문 상태가 주문 완료(주문만 한 상태)이고, OrderType = PURCHASE(구매)인 경우
        if(order.getOrderStatus() == OrderStatus.ORDER_COMPLETED && order.getOrderType() == OrderType.PURCHASE) {
            // order의 금 종류로 product 찾기
            Product product = productRepository.findByGoldType(order.getProduct().getGoldType())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
            // 상품 재고 증가
            product.increaseProductStock(order.getQuantity());
        }

        // 연관된 배송 삭제
        deliveryRepository.deleteAllByOrder(order);
        // 연관된 결제 삭제
        paymentRepository.deleteAllByOrder(order);
        // 주문 삭제
        orderRepository.delete(order);
    }
}
