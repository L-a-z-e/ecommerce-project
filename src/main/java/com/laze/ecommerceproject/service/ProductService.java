package com.laze.ecommerceproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.laze.ecommerceproject.controller.dto.ProductCreateRequest;
import com.laze.ecommerceproject.controller.dto.ProductResponse;
import com.laze.ecommerceproject.domain.Product;
import com.laze.ecommerceproject.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void createProduct(ProductCreateRequest request) {
        String attributesJson = convertMapToJson(request.getAttributes());

        Product product = new Product(
                request.getProductName(),
                request.getPrice(),
                request.getStock(),
                attributesJson
        );
        productRepository.save(product);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found id: " + id));
        return new ProductResponse(product);
    }

    private String convertMapToJson(Map<String, Object> attributes) {
        try {
            return objectMapper.writeValueAsString(attributes);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting attributes to JSON", e);
        }
    }

    public List<ProductResponse> getProductsWithFilters(String brand, Long minPrice, Long maxPrice) {
        return productRepository.findWithFilters(brand, minPrice, maxPrice).stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }
}
