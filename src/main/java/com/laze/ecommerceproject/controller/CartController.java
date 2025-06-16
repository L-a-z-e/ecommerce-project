package com.laze.ecommerceproject.controller;

import com.laze.ecommerceproject.controller.dto.CartItemAddRequest;
import com.laze.ecommerceproject.controller.dto.CartResponse;
import com.laze.ecommerceproject.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public CartResponse getMyCart(@AuthenticationPrincipal String email) {
        return cartService.getCartItems(email);
    }
}
