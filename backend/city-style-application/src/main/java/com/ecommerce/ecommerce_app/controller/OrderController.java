package com.ecommerce.ecommerce_app.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.ecommerce.ecommerce_app.service.OrderService;
import com.ecommerce.ecommerce_app.model.Order;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    
    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(@RequestBody Map<String, Object> body, Principal principal) {
        Map<String, Object> address = (Map<String, Object>) body.get("address");
        String street = address != null ? (String) address.get("street") : "";
        Order order = orderService.placeOrder(principal.getName(), street, street);
        return ResponseEntity.ok(order);
    }
    
    @PostMapping("/pending")
    public ResponseEntity<Map<String, Long>> createPending(@RequestBody Map<String, Object> body, Principal principal) {
        Map<String, Object> addressMap = (Map<String, Object>) body.get("address");
        List<Map<String, Object>> items = (List<Map<String, Object>>) body.get("items");
        Number amount = (Number) body.get("amount");

        OrderService.AddressDto addressDto = new OrderService.AddressDto();
        if (addressMap != null) {
            addressDto.setStreet((String) addressMap.get("street"));
            addressDto.setCity((String) addressMap.get("city"));
            addressDto.setState((String) addressMap.get("state"));
        }

        List<OrderService.OrderItemDto> itemsDto = new ArrayList<>();
        if (items != null) {
            for (Map<String, Object> it : items) {
                OrderService.OrderItemDto dto = new OrderService.OrderItemDto();
                Object pid = it.get("_id") != null ? it.get("_id") : it.get("productId");
                Long productId = null;
                if (pid instanceof Number) productId = ((Number) pid).longValue();
                else if (pid instanceof String) {
                    try {
                        productId = Long.parseLong((String) pid);
                    } catch (NumberFormatException ex) {
                    	
                    }
                }
                dto.setProductId(productId);
                Number q = (Number) it.get("quantity");
                dto.setQuantity(q != null ? q.intValue() : 1);
                dto.setSize((String) it.get("size"));
                itemsDto.add(dto);
            }
        }

        BigDecimal total = amount != null ? BigDecimal.valueOf(amount.doubleValue()) : BigDecimal.ZERO;

        Long pendingOrderId = orderService.createPendingOrder(principal.getName(), addressDto, itemsDto, total);
        return ResponseEntity.ok(Map.of("pendingOrderId", pendingOrderId));
    }

    @GetMapping("/user")
    public ResponseEntity<List<Order>> getUserOrders(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(orderService.getUserOrders(username));
    }

}
