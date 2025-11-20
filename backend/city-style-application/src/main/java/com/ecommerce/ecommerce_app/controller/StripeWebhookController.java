package com.ecommerce.ecommerce_app.controller;

import com.ecommerce.ecommerce_app.service.OrderService;
import com.ecommerce.ecommerce_app.service.PaymentService;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;  
import java.util.stream.Collectors;             

@RestController
@RequestMapping("/api/v1/webhook")
public class StripeWebhookController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderService orderService;  

    @PostMapping
    public ResponseEntity<String> handleStripeWebhook(HttpServletRequest request) {

        String payload;
        try {
            payload = request.getReader().lines().collect(Collectors.joining());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Could not read payload");
        }

        String sigHeader = request.getHeader("Stripe-Signature");
        String endpointSecret = paymentService.getWebhookSecret();

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            System.out.println("[Webhook] Received event: " + event.getType());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }

        if ("checkout.session.completed".equals(event.getType())) {

            Session session = (Session) event.getDataObjectDeserializer()
                    .getObject()
                    .orElse(null);

            if (session == null) {
                System.out.println("[Webhook] ❌ Session is NULL — RAW BODY ISSUE");
                return ResponseEntity.ok("Ignored");
            }

            String orderIdStr = session.getMetadata().get("orderId");
            String paymentId = session.getPaymentIntent();

            System.out.println("[Webhook] Order ID = " + orderIdStr);
            System.out.println("[Webhook] PaymentIntent = " + paymentId);

            orderService.finalizeOrderFro
