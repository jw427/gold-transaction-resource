package com.wanted.gold.order.repository;

import com.wanted.gold.order.domain.Order;
import com.wanted.gold.order.domain.OrderType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE DATE(o.createdAt) = :date AND o.orderType = :type")
    Page<Order> findByCreatedAtDateAndAndOrderType(@Param("date") LocalDate date,
                                                   @Param("type") OrderType type,
                                                   Pageable pageable);
}
