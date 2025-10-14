package com.ecommerce.ecommerce_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.ecommerce_app.model.Cart;

import com.ecommerce.ecommerce_app.model.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    // Custom query to find a cart by the owning user (essential for the API)
    Optional<Cart> findByUser(User user);
}