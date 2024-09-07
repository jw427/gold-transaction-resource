package com.wanted.gold.order.service;

import com.wanted.gold.exception.BadRequestException;
import com.wanted.gold.exception.ErrorCode;
import com.wanted.gold.exception.NotFoundException;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final PriceRepository priceRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;

    // 주문 생성
    public String createOrder(CreateOrderRequestDto requestDto) {
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
    public OrderListPaginationResponseDto<OrderListResponseDto> getOrders(LocalDate date, OrderType type, int offset, int limit) {
        // 페이지 번호 offset과 한 페이지당 출력 개수인 limit
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Order> orders = orderRepository.findByCreatedAtDateAndAndOrderType(date, type, pageable);
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
    public OrderDetailResponseDto getOrder(Long orderId) {
        // 주문 식별번호로 Order 객체 찾기
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));
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
                .orElseThrow(() -> new NotFoundException(ErrorCode.PAYMENT_NOT_FOUNT));
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
}
