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
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false)
    private Long paymentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ColumnDefault("'PENDING'")
    private PaymentStatus paymentStatus; // 기본값 = 결제 전

    private LocalDateTime paymentAt;

    @Column(nullable = false)
    private Long paymentAmount;

    @Column(nullable = false)
    private String bankName;

    @Column(nullable = false)
    private String bankAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    public Payment updatePaymentStatusAndTime(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
        this.paymentAt = LocalDateTime.now();
        return this;
    }
}
