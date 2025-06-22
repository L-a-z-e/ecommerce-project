package com.laze.ecommerceproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@SqlResultSetMapping(
        name = "ProductMapping", // 이 매핑 규칙의 고유한 이름
        entities = @EntityResult(
                entityClass = Product.class, // 결과를 이 엔티티 클래스로 매핑
                fields = {
                        // DB 컬럼명(column)과 엔티티 필드명(name)을 짝지어준다.
                        @FieldResult(name = "id", column = "id"),
                        @FieldResult(name = "productName", column = "product_name"),
                        @FieldResult(name = "price", column = "price"),
                        @FieldResult(name = "stock", column = "stock"),
                        @FieldResult(name = "attributes", column = "attributes")
                }
        )
)
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
