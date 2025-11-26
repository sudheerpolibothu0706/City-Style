package com.ecommerce.ecommerce_app.controller;

import com.ecommerce.ecommerce_app.service.OrderService;
import com.ecommerce.ecommerce_app.service.PaymentService;
import com.ecommerce.ecommerce_app.model.Order;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;

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
            Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);

            if (session == null) {
                System.err.println("[Webhook] Session deserialization failed!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Session null");
            }

            String orderIdStr = session.getMetadata().get("orderId");
            String paymentId = session.getPaymentIntent();

            System.out.println("[Webhook] Session ID = " + session.getId());
            System.out.println("[Webhook] PaymentIntent = " + paymentId);
            System.out.println("[Webhook] Metadata = " + session.getMetadata());

            if (orderIdStr == null || paymentId == null) {
                System.err.println("[Webhook] Missing orderId or paymentId in session metadata!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing orderId or paymentId");
            }

            try {
                Long orderId = Long.parseLong(orderIdStr.trim());
                Order updatedOrder = orderService.finalizeOrderFromStripe(orderId, paymentId);

                if (updatedOrder != null) {
                    System.out.println("[Webhook] Order " + orderId + " updated with PaymentIntent " + paymentId);
                } else {
                    System.err.println("[Webhook] Order " + orderId + " not updated!");
                }
            } catch (NumberFormatException e) {
                System.err.println("[Webhook] Invalid orderId format: " + orderIdStr);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid orderId format");
            } catch (Exception e) {
                System.err.println("[Webhook] Error updating order: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating order");
            }
        }

        return ResponseEntity.ok("Success");
    }
}
