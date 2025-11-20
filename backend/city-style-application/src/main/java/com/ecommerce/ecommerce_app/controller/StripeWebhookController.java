package com.ecommerce.ecommerce_app.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.ecommerce.ecommerce_app.service.PaymentService;
import com.ecommerce.ecommerce_app.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/api/v1/webhook")
public class StripeWebhookController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload,
                                                      @RequestHeader("Stripe-Signature") String sigHeader) {
        String endpointSecret = paymentService.getWebhookSecret();
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            System.out.println("[Webhook] Event received: " + event.getType());
            System.out.println("[Webhook] Raw payload: " + payload);

            if ("checkout.session.completed".equals(event.getType())) {

                var deserializer = event.getDataObjectDeserializer();
                if (!deserializer.getObject().isPresent()) {
                    System.err.println("[Webhook] Failed to deserialize session. Trying raw data...");
                    System.err.println("[Webhook] Event data: " + event.getData().getObject().toJson());
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot deserialize session");
                }

                Session session = (Session) deserializer.getObject().get();

                System.out.println("[Webhook] Session ID: " + session.getId());
                System.out.println("[Webhook] PaymentIntent: " + session.getPaymentIntent());
                System.out.println("[Webhook] Metadata: " + session.getMetadata());

                String orderIdStr = session.getMetadata() != null ? session.getMetadata().get("orderId") : null;
                if (orderIdStr == null) {
                    System.err.println("[Webhook] orderId metadata missing in session");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("orderId metadata missing");
                }

                Long pendingOrderId;
                try {
                    pendingOrderId = Long.valueOf(orderIdStr);
                } catch (NumberFormatException ex) {
                    System.err.println("[Webhook] Invalid pendingOrderId: " + orderIdStr);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid orderId");
                }

                String paymentIntentId = session.getPaymentIntent();
                if (paymentIntentId == null) {
                    System.err.println("[Webhook] PaymentIntent is null for pendingOrderId: " + pendingOrderId);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("PaymentIntent missing");
                }
                orderService.finalizeOrderFromStripe(pendingOrderId, paymentIntentId);
                System.out.println("[Webhook] Order finalized for pendingOrderId: " + pendingOrderId);
            }

        } catch (Exception e) {
            System.err.println("[Webhook] Exception: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook error: " + e.getMessage());
        }

        return ResponseEntity.ok("Success");
    }
}

