package com.laze.ecommerceproject.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CartItemAddRequest {
    @NotNull(message = "상품 ID는 필수입니다.")
    private Long productId;

    @Min(value = 1, message = "상품 수량은 최소 1이상 이어야합니다.")
    private int quantity;
}
