package com.laze.ecommerceproject.repository;

import com.laze.ecommerceproject.domain.Order;
import com.laze.ecommerceproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByCreatedAtDesc(User user);
}
