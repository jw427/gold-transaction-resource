package com.wanted.gold.product.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GoldType goldType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal stock;

    public Product deductProductStock(BigDecimal quantity) {
        this.stock = this.stock.subtract(quantity);
        return this;
    }
}
