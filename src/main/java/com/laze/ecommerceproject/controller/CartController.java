package com.laze.ecommerceproject.controller;

import com.laze.ecommerceproject.controller.dto.CartItemAddRequest;
import com.laze.ecommerceproject.controller.dto.CartItemUpdateRequest;
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

    @PatchMapping("/items/{cartItemId}")
    public String updateCartItemQuantity(@AuthenticationPrincipal String email, @PathVariable Long cartItemId, @Valid @RequestBody CartItemUpdateRequest request) {
        cartService.updateCartItemQuantity(email, cartItemId, request.getQuantity());
        return "장바구니 상품 수량이 변경되었습니다.";

    }

    @DeleteMapping("/items/{cartItemId}")
    public String deleteCartItem(@AuthenticationPrincipal String email, @PathVariable Long cartItemId) {
        cartService.deleteCartItem(email, cartItemId);
        return "장바구니 상품이 삭제되었습니다.";
    }
}
