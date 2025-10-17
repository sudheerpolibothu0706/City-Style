package com.ecommerce.ecommerce_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.ecommerce_app.model.Order;
import com.ecommerce.ecommerce_app.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders") // Matches your frontend route
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Data Transfer Object for checkout (simplified request body)
    public record CheckoutRequest(String shippingAddress, String billingAddress) {
    	
    }

    // 1. PLACE ORDER (CHECKOUT): POST /api/orders
    // Requires Authentication
    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody CheckoutRequest request, Authentication authentication) {
        String username = authentication.getName();
        Order newOrder = orderService.placeOrder(
            username, 
            request.shippingAddress(), 
            request.billingAddress()
        );
        return ResponseEntity.ok(newOrder);
    }

    // 2. GET ORDER HISTORY: GET /api/orders
    // Requires Authentication
    @GetMapping
    public ResponseEntity<List<Order>> getOrderHistory(Authentication authentication) {
        String username = authentication.getName();
        List<Order> orders = orderService.getUserOrders(username);
        return ResponseEntity.ok(orders);
    }

    // 3. GET SINGLE ORDER: GET /api/orders/{id}
    // Requires Authentication (and check if the order belongs to the user)
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        // You would add logic here to fetch the specific order
        // and verify the current user owns it (Crucial security step!)
        Order order = orderService.orderRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Order not found."));
        return ResponseEntity.ok(order);
    }
}
