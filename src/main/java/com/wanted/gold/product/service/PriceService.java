package com.wanted.gold.product.service;

import com.wanted.gold.exception.ErrorCode;
import com.wanted.gold.exception.NotFoundException;
import com.wanted.gold.product.domain.Price;
import com.wanted.gold.product.domain.Product;
import com.wanted.gold.product.dto.CreatePriceRequestDto;
import com.wanted.gold.product.repository.PriceRepository;
import com.wanted.gold.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PriceService {
    private final PriceRepository priceRepository;
    private final ProductRepository productRepository;

    // 가격 등록
    @Transactional
    public String registerPrice(CreatePriceRequestDto requestDto) {
        // 입력받은 금 종류로 product 찾기
        Product product = productRepository.findByGoldType(requestDto.goldType())
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
        // 가격 생성
        Price price = Price.builder()
                .orderType(requestDto.orderType())
                .pricePerGram(requestDto.pricePerGram())
                .content(requestDto.content())
                .createdAt(LocalDateTime.now())
                .product(product)
                .build();
        priceRepository.save(price);
        // 가격 등록 성공 메시지 반환
        return "등록이 완료됐습니다.";
    }
}
