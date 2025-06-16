package com.laze.ecommerceproject.controller.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class CartItemUpdateRequest {

    @Min(value = 1, message = "수량은 1 이상이어야 합니다.")
    private int quantity;
}
