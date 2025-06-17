package com.laze.ecommerceproject.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class OrderRequest {
    @NotBlank(message = "배송지 주소는 필수입니다.")
    private String address;
}
