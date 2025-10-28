package com.ecommerce.ecommerce_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.ecommerce_app.model.Cart;
import com.ecommerce.ecommerce_app.service.CartService;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    
    public record CartRequest(String productId, int quantity) {} 

    
    @GetMapping
    public ResponseEntity<Cart> getCart(Authentication authentication) {
        String username = authentication.getName();
        Cart cart = cartService.getOrCreateCart(username);
        return ResponseEntity.ok(cart);
    }

    
    @PostMapping("/add")
    public ResponseEntity<Cart> addItem(@RequestBody CartRequest request, Authentication authentication) {
        String username = authentication.getName();
        Cart updatedCart = cartService.addItemToCart(
            username,
            request.productId(), 
            request.quantity()
        );
        return ResponseEntity.ok(updatedCart);
    }

    
    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<Cart> removeItem(@PathVariable Long cartItemId, Authentication authentication) {
    
        return ResponseEntity.noContent().build();
    }

    
    @DeleteMapping
    public ResponseEntity<Void> clearCart(Authentication authentication) {
        
        return ResponseEntity.noContent().build();
    }
}
