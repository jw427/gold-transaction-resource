package com.wanted.gold.order.repository;

import com.wanted.gold.order.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
