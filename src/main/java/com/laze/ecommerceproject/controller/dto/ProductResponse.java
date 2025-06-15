package com.laze.ecommerceproject.controller.dto;

import com.laze.ecommerceproject.domain.Product;
import lombok.Getter;

@Getter
public class ProductResponse {
    private final Long id;
    private final String productName;
    private final Long price;
    private final int stock;
    private final String attributes;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.productName =  product.getProductName();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.attributes = product.getAttributes();
    }
}
