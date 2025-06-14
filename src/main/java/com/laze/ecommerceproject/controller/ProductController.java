package com.laze.ecommerceproject.controller;

import com.laze.ecommerceproject.controller.dto.ProductCreateRequest;
import com.laze.ecommerceproject.repository.ProductRepository;
import com.laze.ecommerceproject.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public String createProduct(@Valid @RequestBody ProductCreateRequest request) {
        productService.createProduct(request);
        return "Product created successfully";
    }
}
