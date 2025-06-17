package com.laze.ecommerceproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String orderAddress;

    @Column(nullable = false)
    @Setter
    private Long totalPrice;

    @Column(nullable = false, length = 20)
    private String status;

    // 일대다 관계 매핑
    // mappedBy = "order" : OrderItem 엔티티의 'order' 필드에 의해 매핑되었음을 의미
    // cascade = CascadeType.ALL : Order가 저장/삭제될 때 OrderItem도 함께 저장/삭제됨
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime createdAt;

    public Order(User user, String orderAddress, String status) {
        this.user = user;
        this.orderAddress = orderAddress;
        this.status = status;
    }

    @PrePersist // 엔티티가 저장되기 전에 자동으로 호출됨
    public void onPrePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // 연관관계 편의 메서드
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

}
