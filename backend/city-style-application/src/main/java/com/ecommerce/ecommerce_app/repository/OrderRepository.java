package com.ecommerce.ecommerce_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.ecommerce_app.model.Order;
import com.ecommerce.ecommerce_app.model.User;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Allows users to view their order history
    List<Order> findByUser(User user);
}