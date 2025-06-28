package com.laze.ecommerceproject.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_view_logs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductViewLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사용자와의 관계: '다대일'
    // 비로그인 사용자의 조회 기록도 남기기 위해 'optional = true' (nullable)로 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 상품과의 관계: '다대일'
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private LocalDateTime viewedAt;

    // 생성자
    public ProductViewLog(User user, Product product) {
        this.user = user;
        this.product = product;
        this.viewedAt = LocalDateTime.now();
    }
}
