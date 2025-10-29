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

    @Transactional
    public Cart getOrCreateCart(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setCartItems(new ArrayList<>()); // Initialize list for safety
                    return cartRepository.save(newCart);
                });
    }

    public Cart addItemToCart(String username, Long productId, int quantity, String size) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setCartItems(new ArrayList<>()); // Initialize list for safety
            return cartRepository.save(newCart);
        });

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        if (cart.getCartItems() == null) {
            cart.setCartItems(new ArrayList<>());
        }
        
        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId) && Objects.equals(item.getSize(), size))
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
            newItem.setSize(size); // Set size here
            cartItemRepository.save(newItem);
            cart.getCartItems().add(newItem);
        }

        return cartRepository.save(cart);
    }
    
    @Transactional
    public Cart removeItemFromCart(String username, Long cartItemId) {
        Cart cart = getOrCreateCart(username);

        boolean removed = cart.getCartItems().removeIf(item -> 
            item.getId() != null && item.getId().equals(cartItemId)
        );

        if (removed) {
             return cartRepository.save(cart); 
        } else {
             throw new RuntimeException("Cart item not found with ID: " + cartItemId);
        }
    }

    @Transactional
    public void clearCart(String username) {
        Cart cart = getOrCreateCart(username);
        
        if (cart.getCartItems() != null) {
            cart.getCartItems().clear(); 
            cartRepository.save(cart);
        }
    }
}
