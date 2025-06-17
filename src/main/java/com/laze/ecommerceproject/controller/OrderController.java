package com.laze.ecommerceproject.controller;

import com.laze.ecommerceproject.controller.dto.OrderRequest;
import com.laze.ecommerceproject.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public String createOrder(@AuthenticationPrincipal String email, @RequestBody OrderRequest request) {
        orderService.createOrderFromCart(email, request.getAddress());
        return "주문이 성공적으로 생성되었습니다.";
    }
}
