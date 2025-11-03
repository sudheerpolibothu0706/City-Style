package com.ecommerce.ecommerce_app.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.ecommerce.ecommerce_app.service.PaymentService;
import com.ecommerce.ecommerce_app.service.OrderService;

@RestController
@RequestMapping("/api/v1/webhook")
public class StripeWebhookController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderService orderService;

    @PostMapping
    public String handleStripeWebhook(@RequestBody String payload,
                                      @RequestHeader("Stripe-Signature") String sigHeader) {
        String endpointSecret = paymentService.getWebhookSecret();
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

            if ("checkout.session.completed".equals(event.getType())) {
                Session session = (Session) event.getDataObjectDeserializer()
                        .getObject().orElseThrow(() -> new RuntimeException("Cannot deserialize session"));

                // ✅ Retrieve pending orderId from metadata P@Vitra121
                Long pendingOrderId = Long.valueOf(session.getMetadata().get("orderId"));

                // Finalize order
                orderService.finalizeOrderFromStripe(pendingOrderId, session.getPaymentIntent());
            }

        } catch (Exception e) {
            return "Webhook error: " + e.getMessage();
        }

        return "Success";
    }
}
