package com.laze.ecommerceproject.controller;

import com.laze.ecommerceproject.controller.dto.CartItemAddRequest;
import com.laze.ecommerceproject.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/items")
    public String addProductsToCart(@AuthenticationPrincipal String email, @Valid @RequestBody CartItemAddRequest request) {
        cartService.addCartItem(email, request);
        return "addProductsToCart success!";
    }
}
