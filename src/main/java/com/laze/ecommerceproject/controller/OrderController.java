package com.laze.ecommerceproject.controller;

import com.laze.ecommerceproject.controller.dto.OrderRequest;
import com.laze.ecommerceproject.controller.dto.OrderResponse;
import com.laze.ecommerceproject.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public List<OrderResponse> getMyOrders(@AuthenticationPrincipal String email) {
        return orderService.getMyOrders(email);
    }
}
