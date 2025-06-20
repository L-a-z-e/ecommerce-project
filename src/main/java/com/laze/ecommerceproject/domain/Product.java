package com.laze.ecommerceproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String productName;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private int stock;

    // JPA 표준에는 JSONB 타입이 없기 때문에 설정
    @JdbcTypeCode(SqlTypes.JSON)
    // DDL 생성 시 컬럼 타입 명시적으로 jsonb로 지정
    @Column(columnDefinition = "jsonb")
    private String attributes;

    public Product(String productName, Long price, int stock, String attributes) {
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.attributes = attributes;
    }

    public void removeStock(int quantity) {
        int restStock = this.stock - quantity;

        if (restStock < 0) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }

        this.stock = restStock;
    }
}
