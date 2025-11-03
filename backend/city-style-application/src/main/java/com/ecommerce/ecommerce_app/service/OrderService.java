package com.ecommerce.ecommerce_app.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.ecommerce_app.dto.AddressDto;
import com.ecommerce.ecommerce_app.dto.OrderItemDto;
import com.ecommerce.ecommerce_app.dto.OrderStatus;
import com.ecommerce.ecommerce_app.model.*;
import com.ecommerce.ecommerce_app.repository.*;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    public Order placeOrder(String username, String shippingAddress) {

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found."));

        Cart cart = cartService.getOrCreateCart(username);
        List<CartItem> cartItems = cart.getCartItems();

        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("Cannot place order: Cart is empty.");
        }

        Order newOrder = new Order();
        newOrder.setUser(user);
        newOrder.setStatus(OrderStatus.PENDING); 
        newOrder.setShippingAddress(shippingAddress);

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
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

            BigDecimal line = product.getPrice().multiply(new BigDecimal(cartItem.getQuantity()));
            total = total.add(line);

            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            orderItems.add(orderItem);
        }

        newOrder.setOrderItems(orderItems);
        newOrder.setTotalAmount(total);

        Order savedOrder = orderRepository.save(newOrder);

        cartItems.clear();
        cart.setCartItems(cartItems);
        cartRepository.save(cart);

        return savedOrder;
    }
    @Transactional
    public Long createPendingOrder(String username, AddressDto address, List<OrderItemDto> itemsDto, BigDecimal total) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found."));

        if (itemsDto == null || itemsDto.isEmpty()) {
            throw new RuntimeException("Cannot create pending order: no items provided.");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setShippingAddress(address.getStreet() + ", " + address.getCity() + ", " + address.getState());
        order.setTotalAmount(total);

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDto dto : itemsDto) {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + dto.getProductId()));

            if (product.getStockQuantity() < dto.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setQuantity(dto.getQuantity());
            orderItem.setPriceAtPurchase(product.getPrice());
            orderItems.add(orderItem);

        }

        order.setOrderItems(orderItems);
        Order saved = orderRepository.save(order);
        return saved.getId();
    }

    @Transactional
    public Order finalizeOrderFromStripe(Long pendingOrderId, String stripePaymentId) {
        Order order = orderRepository.findById(pendingOrderId)
                .orElseThrow(() -> new RuntimeException("Pending order not found."));

        if (order.getStatus() == OrderStatus.CONFIRMED) {
            return order;
        }

        for (OrderItem orderItem : order.getOrderItems()) {
            Product product = productRepository.findById(orderItem.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + orderItem.getProductId()));
            if (product.getStockQuantity() < orderItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product during finalize: " + product.getName());
            }
            product.setStockQuantity(product.getStockQuantity() - orderItem.getQuantity());
            productRepository.save(product);
        }

        order.setStatus(OrderStatus.CONFIRMED);
        order.setPaymentReference(stripePaymentId);

        Order savedOrder = orderRepository.save(order);
        Cart cart = cartService.getOrCreateCart(order.getUser().getEmail());
        if (cart != null && cart.getCartItems() != null) {
            cart.getCartItems().clear();
            cartRepository.save(cart);
        }

        return savedOrder;
    }

    public List<Order> getUserOrders(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found."));
        return orderRepository.findByUser(user);
    }


}
