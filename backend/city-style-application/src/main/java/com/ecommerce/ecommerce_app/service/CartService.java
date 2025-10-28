package com.ecommerce.ecommerce_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.ecommerce_app.model.Cart;
import com.ecommerce.ecommerce_app.model.CartItem;
import com.ecommerce.ecommerce_app.model.Product;
import com.ecommerce.ecommerce_app.model.User;
import com.ecommerce.ecommerce_app.repository.CartItemRepository;
import com.ecommerce.ecommerce_app.repository.CartRepository;
import com.ecommerce.ecommerce_app.repository.ProductRepository;
import com.ecommerce.ecommerce_app.repository.UserRepository;

import jakarta.transaction.Transactional;
import java.util.*;

@Transactional
@Service
public class CartService {

    @Autowired private CartRepository cartRepository;
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProductRepository productRepository;

    
    private static final Map<String, Long> frontendIdToBackendId = Map.of(
            "aaaaa", 101L,
            "bbbbb", 102L
            // add more mappings as needed
    );

    // Utility method to get or create a user's cart
    @Transactional
    public Cart getOrCreateCart(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    // Add item using frontend string ID
    public Cart addItemToCart(String username, String frontendProductId, int quantity) {
        Long productId = frontendIdToBackendId.get(frontendProductId);

        if (productId == null) {
            throw new RuntimeException("Invalid product ID from frontend: " + frontendProductId);
        }

        Cart cart = getOrCreateCart(username);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        
        if (cart.getCartItems() == null) {
            cart.setCartItems(new ArrayList<>());
        }

        
        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);

            cartItemRepository.save(newItem);
            cart.getCartItems().add(newItem);
        }

        return cartRepository.save(cart);
    }
}
