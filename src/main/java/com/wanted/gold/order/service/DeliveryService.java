package com.wanted.gold.order.service;

import com.wanted.gold.exception.ConflictException;
import com.wanted.gold.exception.ErrorCode;
import com.wanted.gold.exception.NotFoundException;
import com.wanted.gold.order.domain.*;
import com.wanted.gold.order.repository.DeliveryRepository;
import com.wanted.gold.product.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;

    @Transactional
    public String completeDelivery(Long deliveryId) {
        // 출력 메시지
        String message;
        // 배송 식별번호로 delivery 객체 찾기
        Delivery delivery = deliveryRepository.findByDeliveryId(deliveryId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DELIVERY_NOT_FOUND));
        // 배송과 연관된 order 객체
        Order order = delivery.getOrder();
        // 주문 유형이 구매고 주문 상태가 입금 완료일 때
        if(order.getOrderType() == OrderType.PURCHASE && order.getOrderStatus() == OrderStatus.PAYMENT_COMPLETED) {
            // 배송 상태 발송 완료로 변경
            delivery.updateDeliveryStatusAndTime(DeliveryStatus.SHIPPED);
            // 주문 상태 발송 완료로 변경
            order.updateOrderStatusAndTime(OrderStatus.SHIPPED);
            // 출력 메시지 수정
            message = "발송 완료. 이용해주셔서 감사합니다.";
            // 주문 유형이 판매고 주문 상태가 송금 완료일 때
        } else if(order.getOrderType() == OrderType.SALE && order.getOrderStatus() == OrderStatus.PAYMENT_SENT) {
            // 배송 상태 수령 완료로 변경
            delivery.updateDeliveryStatusAndTime(DeliveryStatus.RECEIVED);
            // 주문 상태 수령 완료로 변경
            order.updateOrderStatusAndTime(OrderStatus.RECEIVED);
            // 주문한 상품 종류 찾기
            Product product = order.getProduct();
            // 해당 상품 재고 증가
            product.increaseProductStock(order.getQuantity());
            // 출력 메시지 수정
            message = "수령 완료. 이용해주셔서 감사합니다.";
            // 이외의 상황에서는 상태 변경 불가
        } else {
            throw new ConflictException(ErrorCode.DELIVERY_FAILED);
        }
        return message;
    }
}
