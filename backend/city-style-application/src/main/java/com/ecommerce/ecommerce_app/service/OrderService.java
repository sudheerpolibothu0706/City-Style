package com.ecommerce.ecommerce_app.service;

import jakarta.transaction.Transactional; // Crucial for multi-step database operations
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.ecommerce_app.model.Cart;
import com.ecommerce.ecommerce_app.model.CartItem;
import com.ecommerce.ecommerce_app.model.Order;
import com.ecommerce.ecommerce_app.model.OrderItem;
import com.ecommerce.ecommerce_app.model.Product;
import com.ecommerce.ecommerce_app.model.User;
import com.ecommerce.ecommerce_app.repository.CartRepository;
import com.ecommerce.ecommerce_app.repository.OrderRepository;
import com.ecommerce.ecommerce_app.repository.ProductRepository;
import com.ecommerce.ecommerce_app.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
	public OrderRepository orderRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CartService cartService;
    @Autowired private ProductRepository productRepository;
    @Autowired private CartRepository cartRepository;

    // This method must succeed entirely or fail entirely (ACID properties)
    @Transactional
    public Order placeOrder(String username, String shippingAddress, String billingAddress) {
        
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found."));

        Cart cart = cartService.getOrCreateCart(username);
        List<CartItem> cartItems = cart.getCartItems();

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cannot place order: Cart is empty.");
        }
        
        // 1. Create Order object
        Order newOrder = new Order();
        newOrder.setUser(user);
        //newOrder.setStatus(OrderStatus.PENDING); 
        newOrder.setShippingAddress(shippingAddress);
        newOrder.setBillingAddress(billingAddress);

        // 2. Map CartItems to OrderItems and calculate total
        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            
            // Check inventory (critical business logic)
            Product product = cartItem.getProduct();
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                 throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            // Create OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(newOrder);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(product.getPrice());
            
            // Update inventory (DECREASE STOCK)
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product); // Save updated product stock
            
           // total = total.add(product.getPrice().multiply(new BigDecimal(orderItem.getQuantity())));
            return orderItem;
            
        }).collect(Collectors.toList());

        newOrder.setOrderItems(orderItems);
        newOrder.setTotalAmount(total);

        // 3. Save the new Order (this saves all OrderItems due to CascadeType.ALL)
        Order savedOrder = orderRepository.save(newOrder);

        // 4. Clear the User's Cart (critical for cleanup)
        cartItems.clear();
        cart.setCartItems(cartItems);
        cartRepository.save(cart);

        return savedOrder;
    }
    
    public List<Order> getUserOrders(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found."));
        return orderRepository.findByUser(user);
    }
}