package com.laze.ecommerceproject.controller;

import com.laze.ecommerceproject.controller.dto.ProductCreateRequest;
import com.laze.ecommerceproject.controller.dto.ProductResponse;
import com.laze.ecommerceproject.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public String createProduct(@Valid @RequestBody ProductCreateRequest request) throws Exception {
        productService.createProduct(request);
        return "Product created successfully";
    }

//    @GetMapping()
//    public List<ProductResponse> getAllProducts() {
//        return productService.getAllProducts();
//    }

    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping
    public List<ProductResponse> getProducts(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Long minPrice,
            @RequestParam(required = false) Long maxPrice
    ) {
        return productService.getProductsWithFilters(brand, minPrice, maxPrice);
    }
}
