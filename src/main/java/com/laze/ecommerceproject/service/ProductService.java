package com.laze.ecommerceproject.service;

import com.laze.ecommerceproject.controller.dto.ProductCreateRequest;
import com.laze.ecommerceproject.domain.Product;
import com.laze.ecommerceproject.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public void createProduct(ProductCreateRequest request) {
        Product product = new Product(
                request.getProductName(),
                request.getPrice(),
                request.getStock(),
                request.getAttributes()
        );
        productRepository.save(product);
    }
}
