package com.laze.ecommerceproject.controller.dto;

import com.laze.ecommerceproject.domain.CartItem;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CartResponse {

    private final List<CartItemResponse> items;
    private final Long totalPrice;

    public CartResponse(List<CartItem> cartItems) {
        this.items = cartItems.stream()
                .map(CartItemResponse::new)
                .collect(Collectors.toList());
        this.totalPrice = caculateTotalPrice(cartItems);
    }

    public Long caculateTotalPrice(List<CartItem> cartItems) {
        return cartItems.stream()
                .mapToLong(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    // static 을 사용하는 이유
    // static을 붙이지 않으면 불필요한 바깥 클래스(Outer Class)의 참조를 갖게 되어 메모리 누수와 직렬화 문제를 일으킬 수 있기 때문
    //    일반 내부 클래스 (Non-static Inner Class):
    //    static이 없는 내부 클래스는 항상 자신을 만든 바깥 클래스의 인스턴스에 대한 숨겨진 참조(this)를 가짐
    //    CartItemResponse가 static이 아니라면, new CartItemResponse(...)로 객체를 만들 때마다 보이지 않는 CartResponse의 참조를 함께 들고 있게 됨
    //    문제점:
    //    메모리 누수: CartItemResponse 객체만 따로 사용하고 싶은데도, 바깥의 CartResponse 객체가 계속 메모리에 남아있게 됨
    //            직렬화(Serialization) 오류: 나중에 이 DTO를 JSON으로 변환(직렬화)할 때, Jackson 같은 라이브러리는 숨겨진 바깥 클래스 참조 때문에 변환에 실패하거나 의도치 않은 추가 필드를 만들어낼 수 있음
    //    static 내부 클래스 (Static Nested Class):
    //    static을 붙이면 이 클래스는 바깥 클래스와 독립적인 관계가 됩니다. 더 이상 바깥 클래스의 인스턴스를 참조하지 않음
    //    사실상 이름만 CartResponse 안에 있을 뿐, 완전히 별개의 클래스처럼 동작함
    //            장점:
    //    독립성: 바깥 클래스와의 불필요한 연결이 끊어져 메모리 누수 걱정이 없어짐
    //    명확한 관계 표현: "나는 CartResponse와 논리적으로는 관련이 있지만, 기능적으로는 독립적인 클래스야" 라는 의미를 명확히 함
    @Getter
    public static class CartItemResponse {
        private final Long id;
        private final Long productId;
        private final String productName;
        private final Long price;
        private final int quantity;

        public CartItemResponse(CartItem cartItem) {
            this.id = cartItem.getId();
            this.productId = cartItem.getProduct().getId();
            this.productName = cartItem.getProduct().getProductName();
            this.price = cartItem.getProduct().getPrice();
            this.quantity = cartItem.getQuantity();
        }

    }
}
