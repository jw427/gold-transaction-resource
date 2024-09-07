package com.wanted.gold.product.repository;

import com.wanted.gold.order.domain.OrderType;
import com.wanted.gold.product.domain.Price;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PriceRepository extends JpaRepository<Price, Long> {
    Optional<Price> findTopByOrderTypeAndProduct_ProductIdOrderByCreatedAtDesc(OrderType orderType, Long productId);
}
