package com.laze.ecommerceproject.service;

import com.laze.ecommerceproject.controller.dto.OrderResponse;
import com.laze.ecommerceproject.domain.*;
import com.laze.ecommerceproject.repository.CartItemRepository;
import com.laze.ecommerceproject.repository.OrderRepository;
import com.laze.ecommerceproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public void createOrderFromCart(String email, String address) {
        // 1. 사용자 및 장바구니 정보 조회
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<CartItem> cartItems =cartItemRepository.findByUserId(user.getId());

        if (cartItems.isEmpty()) {
            throw new IllegalStateException("No cart items found for user " + user.getEmail());
        }

        // 2. 주문 엔티티 생성
        Order order = new Order(
                user,
                address,
                "PENDING"
        );

        // 3. 장바구니 아이템 -> 주문 아이템 변환
        Long totalPrice = 0L;
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            // 3-1.재고 확인 및 감소
            product.removeStock(cartItem.getQuantity());

            // 3-2. 주문 아이템 생성
            OrderItem orderItem = new OrderItem(product, cartItem.getQuantity(), cartItem.getProduct().getPrice());
            order.addOrderItem(orderItem);
            totalPrice += orderItem.getOrderPrice() * orderItem.getQuantity();
        }

        order.setTotalPrice(totalPrice);

        // 4. 포인트 차감 (결제 시뮬레이션)
        user.removePoint(totalPrice);

        // 5. 주문 저장 (Cascade 설정으로 OrderItem도 함께 저장됨)
        orderRepository.save(order);

        // 6. 장바구니 비우기
        cartItemRepository.deleteAll(cartItems);
    }

    public List<OrderResponse> getMyOrders(String email) {
        // 1. 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 2. 해당 사용자의 모든 주문을 생성일 내림차순으로 조회
        List<Order> orders = orderRepository.findByUserOrderByCreatedAtDesc(user);

        //3 . 조회된 Order 리스트를 OrderResponse DTO로 변환
        return orders.stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }
}
