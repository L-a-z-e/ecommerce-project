package com.laze.ecommerceproject.repository;

import com.laze.ecommerceproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// JpaRepository<관리할 엔티티, 엔티티 ID 타입>
public interface UserRepository extends JpaRepository<User, Long> {

    // Query Method
    // Spring Data JPA는 메소드 이름을 분석해서 자동으로 JPQL 쿼리 생성 해줌
    // 'findByEmail' => email 필드를 조건으로 User를 조회하는 쿼리를 만들어 냄
    // 반환 타입 Optional<User> => 이메일에 해당하는 유저가 존재하지 않을 수도 있기 때문
    // select * from users where email = ? 생성해줌
    Optional<User> findByEmail(String email);
}
