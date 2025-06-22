package com.laze.ecommerceproject.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.Map;

@Getter
public class ProductCreateRequest {
    @NotBlank
    private String productName;

    @Min(0)
    private Long price;

    @Min(0)
    private int stock;

    // 신발 사이즈, 색상 등을 JSON 형태로 받기 위한 필드
    private Map<String, Object> attributes;
}
