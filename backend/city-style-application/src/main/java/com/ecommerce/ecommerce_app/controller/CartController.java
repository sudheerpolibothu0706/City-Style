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

    // DTO for adding items
    public record CartRequest(String productId, int quantity) {} // <-- frontend string ID

    // 1. GET CART
    @GetMapping
    public ResponseEntity<Cart> getCart(Authentication authentication) {
        String username = authentication.getName();
        Cart cart = cartService.getOrCreateCart(username);
        return ResponseEntity.ok(cart);
    }

    // 2. ADD ITEM using frontend string ID
    @PostMapping("/add")
    public ResponseEntity<Cart> addItem(@RequestBody CartRequest request, Authentication authentication) {
        String username = authentication.getName();
        Cart updatedCart = cartService.addItemToCart(
            username,
            request.productId(), // frontend string ID
            request.quantity()
        );
        return ResponseEntity.ok(updatedCart);
    }

    // 3. REMOVE ITEM
    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<Cart> removeItem(@PathVariable Long cartItemId, Authentication authentication) {
        // implement remove logic in CartService if needed
        return ResponseEntity.noContent().build();
    }

    // 4. CLEAR CART
    @DeleteMapping
    public ResponseEntity<Void> clearCart(Authentication authentication) {
        // implement clear logic in CartService if needed
        return ResponseEntity.noContent().build();
    }
}
