package com.wanted.gold.order.repository;

import com.wanted.gold.order.domain.Order;
import com.wanted.gold.order.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // 주문 식별번호로 주문에 해당하는 결제 찾기 - 해당하는 결제 여러개일 경우 paymentId 큰 것
    Optional<Payment> findTopByOrder_OrderIdOrderByPaymentIdDesc(Long orderId);

    // 주문에 해당하는 결제 정보 삭제
    void deleteAllByOrder(Order order);

    // 결제 식별번호로 결제 찾기
    Optional<Payment> findByPaymentId(Long paymentId);
}
