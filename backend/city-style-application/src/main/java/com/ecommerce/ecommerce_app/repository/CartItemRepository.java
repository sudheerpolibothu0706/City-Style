package com.ecommerce.ecommerce_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.ecommerce_app.model.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // No custom methods needed immediately, JpaRepository provides all we need.
}