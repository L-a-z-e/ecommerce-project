package com.laze.ecommerceproject.repository;

import com.laze.ecommerceproject.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // 특정 사용자의 모든 장바구니 아이템 조회
    List<CartItem> findByUserId(Long userId);

    // 특정 사용자가 특정 상품을 장바구니에 담았는 지 조회
    Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);
}
