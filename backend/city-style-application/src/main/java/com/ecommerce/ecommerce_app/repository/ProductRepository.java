package com.ecommerce.ecommerce_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.ecommerce_app.model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Enables search functionality based on name or description (important for e-commerce)
    List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
}
