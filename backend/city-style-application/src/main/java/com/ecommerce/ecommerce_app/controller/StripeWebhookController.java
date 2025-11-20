package com.ecommerce.ecommerce_app.controller;

import org.springframework.web.bind.annotation.*;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import com.ecommerce.ecommerce_app.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/v1/webhook")
public class StripeWebhookController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload,
                                                      @RequestHeader("Stripe-Signature") String sigHeader) {
        String endpointSecret = paymentService.getWebhookSecret();

        try {
            Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

            System.out.println("[Webhook] Event received: " + event.getType());
            System.out.println("[Webhook] Raw payload: " + payload);

            if (event.getData().getObject() != null) {
                System.out.println("[Webhook] Event data: " + event.getData().getObject().toJson());
            }
            orderService.finalizeOrderFromStripe(Long.parseLong(orderIdStr), paymentId);

            System.out.println("[Webhook] Order updated successfully!");
            
        } catch (Exception e) {
            System.err.println("[Webhook] Exception: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook error: " + e.getMessage());
        }

        return ResponseEntity.ok("Webhook received successfully");
    }
}
