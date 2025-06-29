package com.laze.ecommerceproject.repository;

import com.laze.ecommerceproject.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // JPQL 쿼리 사용
    // function('jsonb_extract_path_text', p.attributes, 'brand') -> function : JPQL 함수가 아닌 데이터베이스의 네이티브 함수 호출,
    // jsonb_extract_path PostgreSQL 내장 함수 첫번째 인자로 받은 JSON 데이터에서 두번째 인자로 주어진 키에 해당하는 값을 텍스트로 추출
    // error fix => hibernate 에서 function()을 처리할 방법을 적용하면 postgreSQL에 의존하게되므로 native Query로 변경 필요
    @Query(value = "SELECT p.id, p.product_name, p.price, p.stock, p.attributes FROM products p WHERE " +
    "(:brand is null OR p.attributes ->> 'brand' = :brand) AND "
    + "(:minPrice is null OR p.price >= :minPrice) AND "
    + "(:maxPrice is null OR p.price <= :maxPrice)",
    nativeQuery = true)
    List<Product> findWithFilters(@Param("brand") String brand, @Param("minPrice") Long minPrice, @Param("maxPrice") Long maxPrice);

    @Query(value =
        "SELECT p.* FROM products p WHERE p.id IN ( " +
        " SELECT pvl2.product_id " +
        " FROM product_view_logs pvl1 " +
        " JOIN product_view_logs pvl2 ON pvl1.user_id = pvl2.user_id " +
        " AND pvl1.user_id IS NOT NULL " +
        " AND pvl2.product_id != :productId " +
        " GROUP BY pvl2.product_id " +
        " ORDER BY COUNT(pvl2.product_id) DESC, pvl2.product_id ASC " +
        " LIMIT :limit " +
        ")",
        nativeQuery = true
    )
    List<Product> findRecommendations(@Param("productId") Long productId, @Param("limit") int limit);
}
