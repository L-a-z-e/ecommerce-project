package com.laze.ecommerceproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_items")
@Getter
@NoArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @ManyToOne: 여러 개의 장바구니 아이템(CartItems)이 하나의 회원(Users)에 연결될 수 있음을 나타냅니다.
    // fetch = FetchType.LAZY: **지연 로딩(Lazy Loading) 전략
    //   - CartItems를 조회할 때, 연관된 Users 데이터를 즉시 함께 가져오지 않습니다.
    //   - 실제 users 필드에 접근하는 시점(예: cartItem.getUsers())에 비로소 Users 데이터를 조회합니다.
    //   - 불필요한 조회를 막아 성능을 최적화하는 매우 중요한 설정입니다. (N+1 문제 방지)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    public CartItem(User user, Product product, int quantity) {
        this.user = user;
        this.product = product;
        this.quantity = quantity;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }
}
