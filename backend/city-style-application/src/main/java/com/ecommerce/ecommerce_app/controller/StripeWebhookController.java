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

@RestController
@RequestMapping("/api/v1/webhook")
public class StripeWebhookController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderService orderService;  

    @PostMapping
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ) {

        String endpointSecret = paymentService.getWebhookSecret();
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            System.out.println("[Webhook] Received event: " + event.getType());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }

        try {
            if ("checkout.session.completed".equals(event.getType())) {

                // Extract session object
                Session session = (Session) event.getDataObjectDeserializer()
                        .getObject()
                        .orElse(null);

                if (session == null) {
                    System.out.println("[Webhook] Null session object");
                    return ResponseEntity.ok("Ignored");
                }

                String orderIdStr = session.getMetadata().get("orderId");   
                String paymentId = session.getPaymentIntent();              

                System.out.println("[Webhook] Order ID: " + orderIdStr);
                System.out.println("[Webhook] PaymentIntent: " + paymentId);

                orderService.finalizeOrderFromStripe(Long.parseLong(orderIdStr), paymentId);

                System.out.println("[Webhook] Order updated successfully");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Webhook processing error");
        }

        return ResponseEntity.ok("Success");
    }
}
