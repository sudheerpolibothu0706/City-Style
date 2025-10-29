package com.ecommerce.ecommerce_app.service;

import jakarta.transaction.Transactional; 
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
	
    @Transactional
    public Order placeOrder(String username, String shippingAddress, String billingAddress) {
        
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found."));

        Cart cart = cartService.getOrCreateCart(username);
        List<CartItem> cartItems = cart.getCartItems();

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cannot place order: Cart is empty.");
        }
        Order newOrder = new Order();
        newOrder.setUser(user); 
        newOrder.setShippingAddress(shippingAddress);
        newOrder.setBillingAddress(billingAddress);

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            
            // Check inventory (critical business logic)
            Product product = cartItem.getProduct();
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                 throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(newOrder);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(product.getPrice());

            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product); // Save updated product stock
            return orderItem;
            
        }).collect(Collectors.toList());

        newOrder.setOrderItems(orderItems);
        newOrder.setTotalAmount(total);

        Order savedOrder = orderRepository.save(newOrder);

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
