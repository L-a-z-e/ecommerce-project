package com.laze.ecommerceproject.controller.dto;

import com.laze.ecommerceproject.domain.Order;
import com.laze.ecommerceproject.domain.OrderItem;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderResponse {
    private final Long orderId;
    private final String orderAddress;
    private final Long totalPrice;
    private final String status;
    private final LocalDateTime orderDate;
    private final List<OrderItemResponse> orderItems;

    public OrderResponse(Order order) {
        this.orderId = order.getId();
        this.orderAddress = order.getOrderAddress();
        this.totalPrice = order.getTotalPrice();
        this.status = order.getStatus();
        this.orderDate = order.getCreatedAt();
        this.orderItems = order.getOrderItems().stream()
                .map(OrderItemResponse::new)
                .collect(Collectors.toList());
    }

    @Getter
    public static class OrderItemResponse {
        private final String productName;
        private final int quantity;
        private final Long orderPrice;

        public OrderItemResponse(OrderItem orderItem) {
            this.productName = orderItem.getProduct().getProductName();
            this.quantity = orderItem.getQuantity();
            this.orderPrice = orderItem.getOrderPrice();
        }
    }
}
