package com.ecommerce.ecommerce_app.controller;

import com.ecommerce.ecommerce_app.model.Order;
import com.ecommerce.ecommerce_app.service.OrderService;
import com.ecommerce.ecommerce_app.service.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    System.out.println("[Webhook] livemode=" + event.getLivemode() +
                       ", pending_webhooks=" + event.getPendingWebhooks());

    if ("checkout.session.completed".equals(event.getType())) {
        try {
            JsonNode sessionNode = new ObjectMapper().readTree(event.getData().getObject().toJson());
            String orderIdStr = sessionNode.path("metadata").path("orderId").asText(null);
            String paymentId = sessionNode.path("payment_intent").asText(null);

            if (orderIdStr == null || paymentId == null) {
                System.err.println("[Webhook] Missing orderId or paymentId in session metadata!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing orderId or paymentId");
            }

            Long orderId = Long.parseLong(orderIdStr.trim());
            orderService.finalizeOrderFromStripe(orderId, paymentId);
            System.out.println("[Webhook] Order " + orderId + " finalized with PaymentIntent " + paymentId);

        } catch (Exception e) {
            System.err.println("[Webhook] Error processing session: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing checkout.session.completed");
        }
    }

    return ResponseEntity.ok("Success");
}

}
