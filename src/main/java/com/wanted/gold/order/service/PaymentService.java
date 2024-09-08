package com.wanted.gold.order.service;

import com.wanted.gold.exception.ConflictException;
import com.wanted.gold.exception.ErrorCode;
import com.wanted.gold.exception.NotFoundException;
import com.wanted.gold.order.domain.*;
import com.wanted.gold.order.dto.ModifyPaymentRequestDto;
import com.wanted.gold.order.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @Transactional
    public String completePayment(Long paymentId) {
        // 출력 메시지
        String message;
        // 결제 식별번호로 payment 객체 찾기
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PAYMENT_NOT_FOUND));
        // 결제와 연관된 order 객체
        Order order = payment.getOrder();
        // 주문 유형이 구매고 주문 상태가 주문 완료일 때
        if(order.getOrderType() == OrderType.PURCHASE && order.getOrderStatus() == OrderStatus.ORDER_COMPLETED) {
            // 결제 상태 입금 완료로 변경
            payment.updatePaymentStatusAndTime(PaymentStatus.PAYMENT_COMPLETED);
            // 주문 상태 입금 완료로 변경
            order.updateOrderStatusAndTime(OrderStatus.PAYMENT_COMPLETED);
            // 출력 메시지 수정
            message = "입금이 완료됐습니다.";
            // 주문 유형이 판매고 주문 상태가 주문 완료일 때
        } else if(order.getOrderType() == OrderType.SALE && order.getOrderStatus() == OrderStatus.ORDER_COMPLETED) {
            // 결제 상태 송금 완료로 변경
            payment.updatePaymentStatusAndTime(PaymentStatus.PAYMENT_SENT);
            // 주문 상태 송금 완료로 변경
            order.updateOrderStatusAndTime(OrderStatus.PAYMENT_SENT);
            // 출력 메시지 수정
            message = "송금이 완료됐습니다.";
            // 이외의 상황에서는 상태 변경 불가
        } else {
            throw new ConflictException(ErrorCode.PAYMENT_FAILED);
        }
        return message;
    }

    @Transactional
    public String modifyPayment(Long paymentId, ModifyPaymentRequestDto requestDto) {
        // 결제 식별번호로 payment 객체 찾기
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PAYMENT_NOT_FOUND));
        // 결제가 완료된 상태에서는 수정 불가
        if(payment.getPaymentStatus() != PaymentStatus.PENDING)
            throw  new ConflictException(ErrorCode.PAYMENT_MODIFY_FAILED);
        payment.modifyPayment(
                // 입력받은 은행 이름이 없을 경우 기존의 은행 이름
                requestDto.bankName() != null ? requestDto.bankName() : payment.getBankName(),
                // 입력받은 계좌번호가 없을 경우 기존의 계좌번호
                requestDto.bankAccount() != null ? requestDto.bankAccount() : payment.getBankAccount()
        );
        // 결제 정보 수정 성공 메시지 반환
        return "결제 정보 수정을 완료했습니다.";
    }
}
