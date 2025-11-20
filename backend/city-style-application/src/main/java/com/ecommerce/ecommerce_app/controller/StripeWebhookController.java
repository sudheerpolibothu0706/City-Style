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
            System.out.println("[Webhook] Event received: " + event.getType());

            if ("checkout.session.completed".equals(event.getType())) {
                var deserializer = event.getDataObjectDeserializer();
                if (!deserializer.getObject().isPresent()) {
                    System.err.println("[Webhook] Failed to deserialize session");
                    return "Webhook error: cannot deserialize session";
                }

                Session session = (Session) deserializer.getObject().get();
                System.out.println("[Webhook] Session metadata: " + session.getMetadata());
                System.out.println("[Webhook] PaymentIntent: " + session.getPaymentIntent());

                String orderIdStr = session.getMetadata() != null ? session.getMetadata().get("orderId") : null;
                if (orderIdStr == null) {
                    System.err.println("[Webhook] orderId metadata missing in session");
                    return "Webhook error: orderId metadata missing";
                }

                Long pendingOrderId;
                try {
                    pendingOrderId = Long.valueOf(orderIdStr);
                } catch (NumberFormatException ex) {
                    System.err.println("[Webhook] Invalid pendingOrderId: " + orderIdStr);
                    return "Webhook error: invalid pendingOrderId";
                }

                String paymentIntentId = session.getPaymentIntent();
                if (paymentIntentId == null) {
                    System.err.println("[Webhook] PaymentIntent is null for pendingOrderId: " + pendingOrderId);
                    return "Webhook error: paymentIntent missing";
                }

                orderService.finalizeOrderFromStripe(pendingOrderId, paymentIntentId);
                System.out.println("[Webhook] Order finalized for pendingOrderId: " + pendingOrderId);
            }

        } catch (Exception e) {
            System.err.println("[Webhook] Exception: " + e.getMessage());
            e.printStackTrace();
            return "Webhook error: " + e.getMessage();
        }

        return "Success";
    }
}
