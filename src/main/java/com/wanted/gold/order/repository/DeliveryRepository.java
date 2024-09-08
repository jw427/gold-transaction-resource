package com.wanted.gold.order.repository;

import com.wanted.gold.order.domain.Delivery;
import com.wanted.gold.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    // 주문 식별번호로 주문에 해당하는 배송 찾기 - 해당하는 배송 여러개일 경우 deliveryId 큰 것
    Optional<Delivery> findTopByOrder_OrderIdOrderByDeliveryIdDesc(Long orderId);

    // 주문에 해당하는 배송 정보 삭제
    void deleteAllByOrder(Order order);

    // 배송 식별번호로 배송 찾기
    Optional<Delivery> findByDeliveryId(Long deliveryId);
}
