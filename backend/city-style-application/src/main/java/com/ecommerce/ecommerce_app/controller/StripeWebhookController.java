package com.ecommerce.ecommerce_app.controller;

import com.ecommerce.ecommerce_app.service.OrderService;
import com.ecommerce.ecommerce_app.service.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/v1/webhook")
public class StripeWebhookController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderService orderService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping
    public ResponseEntity<String> handleStripeWebhook(
            @RequestHeader("Stripe-Signature") String sigHeader,
            @RequestBody byte[] payloadBytes) {

        String payload = new String(payloadBytes);
        String endpointSecret = paymentService.getWebhookSecret();

        System.out.println("[Webhook] Received Stripe event");
        System.out.println("[Webhook] Endpoint Secret = " + endpointSecret);
        System.out.println("[Webhook] Signature Header = " + sigHeader);
        System.out.println("[Webhook] Raw Payload = " + payload);

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            System.err.println("[Webhook] Invalid signature: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        } catch (Exception e) {
            System.err.println("[Webhook] Error parsing event: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook error");
        }

        System.out.println("[Webhook] Event Type = " + event.getType());

        if ("checkout.session.completed".equals(event.getType())) {
            try {
                // Parse the session as JSON instead of casting
                JsonNode sessionNode = objectMapper.readTree(event.getDataObject().toJson());

                String orderIdStr = sessionNode.path("metadata").path("orderId").asText(null);
                String paymentId = sessionNode.path("payment_intent").asText(null);

                System.out.println("[Webhook] Parsed session JSON: " + sessionNode.toString());
                System.out.println("[Webhook] Extracted orderId = " + orderIdStr + ", paymentId = " + paymentId);

                if (orderIdStr == null || paymentId == null) {
                    System.err.println("[Webhook] Missing orderId or paymentId in session metadata!");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing orderId or paymentId");
                }

                Long orderId = Long.parseLong(orderIdStr.trim());
                boolean updated = orderService.finalizeOrderFromStripe(orderId, paymentId);

                if (updated) {
                    System.out.println("[Webhook] Order " + orderId + " updated with PaymentIntent " + paymentId);
                } else {
                    System.err.println("[Webhook] Order " + orderId + " not found or not updated!");
                }

            } catch (Exception e) {
                System.err.println("[Webhook] Error processing session: " + e.getMessage());
                e.printStackTrace();
                return ResponseEntity.ok("Webhook received but processing failed. Check logs.");
            }
        }

        return ResponseEntity.ok("Success");
    }
}
