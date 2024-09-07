package com.wanted.gold.order.service;

import com.wanted.gold.exception.BadRequestException;
import com.wanted.gold.exception.ErrorCode;
import com.wanted.gold.exception.NotFoundException;
import com.wanted.gold.order.domain.Delivery;
import com.wanted.gold.order.domain.Order;
import com.wanted.gold.order.domain.OrderType;
import com.wanted.gold.order.domain.Payment;
import com.wanted.gold.order.dto.CreateOrderRequestDto;
import com.wanted.gold.order.repository.DeliveryRepository;
import com.wanted.gold.order.repository.OrderRepository;
import com.wanted.gold.order.repository.PaymentRepository;
import com.wanted.gold.product.domain.Price;
import com.wanted.gold.product.domain.Product;
import com.wanted.gold.product.repository.PriceRepository;
import com.wanted.gold.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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
                .totalPrice(totalPrice)
                .quantity(requestDto.quantity())
                .product(product)
                .build();
        orderRepository.save(order);
        // 배송 생성
        Delivery delivery = Delivery.builder()
                .address(requestDto.address())
                .recipientName(requestDto.recipientName())
                .recipientPhone(requestDto.recipientPhone())
                .order(order)
                .build();
        deliveryRepository.save(delivery);
        // 결제 생성
        Payment payment = Payment.builder()
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
}
