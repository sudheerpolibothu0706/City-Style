package com.ecommerce.ecommerce_app.controller;

import com.ecommerce.ecommerce_app.service.OrderService;
import com.ecommerce.ecommerce_app.service.PaymentService;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

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

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            System.out.println("[Webhook] Received event: " + event.getType());
        } catch (Exception e) {
            System.err.println("[Webhook] Invalid signature: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }

        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
            if (session != null) {
                String orderIdStr = session.getMetadata().get("orderId");
                String paymentId = session.getPaymentIntent();

                System.out.println("[Webhook] Order ID = " + orderIdStr);
                System.out.println("[Webhook] PaymentIntent = " + paymentId);

                orderService.finalizeOrderFromStripe(Long.parseLong(orderIdStr), paymentId);
            }
        }

        return ResponseEntity.ok("Success");
    }
}

