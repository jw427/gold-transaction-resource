package com.wanted.gold.order.repository;

import com.wanted.gold.order.domain.Order;
import com.wanted.gold.order.domain.OrderType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // 날짜와 주문 유형으로 페이지네이션 활용해 주문 조회
    @Query("SELECT o FROM Order o WHERE DATE(o.createdAt) = :date AND o.orderType = :type")
    Page<Order> findByCreatedAtDateAndOrderType(@Param("date") LocalDate date,
                                                   @Param("type") OrderType type,
                                                   Pageable pageable);

    // 날짜와 주문 유형, 회원 식별번호로 주문 조회
    @Query("SELECT o FROM Order o WHERE DATE(o.createdAt) = :date AND o.orderType = :type AND o.userId = :userId")
    Page<Order> findByCreatedAtAndOrderTypeAndUserId(@Param("date") LocalDate date,
                                                     @Param("type") OrderType type,
                                                     @Param("userId") UUID userId,
                                                     Pageable pageable);

    // 주문 식별번호로 해당하는 주문 조회
    Optional<Order> findByOrderId(Long orderId);
}
