package com.laze.ecommerceproject.service;

import com.laze.ecommerceproject.controller.dto.CartItemAddRequest;
import com.laze.ecommerceproject.controller.dto.CartResponse;
import com.laze.ecommerceproject.domain.CartItem;
import com.laze.ecommerceproject.domain.Product;
import com.laze.ecommerceproject.domain.User;
import com.laze.ecommerceproject.repository.CartItemRepository;
import com.laze.ecommerceproject.repository.ProductRepository;
import com.laze.ecommerceproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public void addCartItem(String email, CartItemAddRequest request) {

        // 1. 유저 존재 여부 확인
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found email: " + email));

        // 2. 상품 존재 여부 확인
        Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new IllegalArgumentException("Product not found id: " + request.getProductId()));

        // 3. 사용자 장바구니에 이미 해당 상품이 있는 지 확인
        Optional<CartItem> existingCartItem = cartItemRepository.findByUserIdAndProductId(user.getId(), product.getId());

        // Dirty Checking -> 이미 있는 경우는 JPA에서 관리하기때문에 변경 일어나면 Transaction 완료시 반영
        // 없는 경우에는 JPA에서 관리하지 않기 때문에 save 메서드 호출해서 저장
        // 4. 사용자 장바구니에 해당 상품이 이미 있는 경우
        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.addQuantity(request.getQuantity());
        }
        // 5. 사용자 장바구니에 해당 상품이 없는 경우
        else {
            CartItem cartItem = new CartItem(user, product, request.getQuantity());
            cartItemRepository.save(cartItem);
        }
    }

    public CartResponse getCartItems(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<CartItem> cartItems = cartItemRepository.findByUserId(user.getId());

        return new CartResponse(cartItems);
    }

}
