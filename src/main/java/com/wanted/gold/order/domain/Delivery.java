package com.wanted.gold.order.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "deliveries")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id", nullable = false)
    private Long deliveryId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'PENDING'")
    private DeliveryStatus deliveryStatus; // 기본값 = 발송 전

    private LocalDateTime deliveryAt;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String recipientName;

    @Column(nullable = false)
    private String recipientPhone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public Delivery updateDeliveryStatusAndTime(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
        this.deliveryAt = LocalDateTime.now();
        return this;
    }
}
