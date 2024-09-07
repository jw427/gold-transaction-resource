package com.wanted.gold.product.repository;

import com.wanted.gold.product.domain.GoldType;
import com.wanted.gold.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // 금 종류로 product 찾기
    Optional<Product> findByGoldType(GoldType goldType);
}
