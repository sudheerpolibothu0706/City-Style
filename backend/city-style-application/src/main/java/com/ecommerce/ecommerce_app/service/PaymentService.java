package com.ecommerce.ecommerce_app.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.exception.StripeException;
import com.ecommerce.ecommerce_app.dto.PaymentRequestdto;
import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;

@Service
public class PaymentService {

    @Value("${stripe.secret.key}")
    private String stripeApiKey;

    @Value("${stripe.success.url}")
    private String successUrl;

    @Value("${stripe.cancel.url}")
    private String cancelUrl;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    public String getWebhookSecret() {
        return webhookSecret;
    }

    /**
     * Create a Stripe checkout session and attach pendingOrderId in metadata
     */
    public String createSession(PaymentRequestdto request, Long pendingOrderId) throws StripeException {

        SessionCreateParams.LineItem.PriceData.ProductData productData =
            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(request.getProductName())
                .build();

        SessionCreateParams.LineItem.PriceData priceData =
            SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency(request.getCurrency().toLowerCase())
                .setUnitAmount(request.getAmount())
                .setProductData(productData)
                .build();

        SessionCreateParams.LineItem lineItem =
            SessionCreateParams.LineItem.builder()
                .setQuantity(Long.valueOf(request.getQuantity()))
                .setPriceData(priceData)
                .build();

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .addLineItem(lineItem)
                .putMetadata("orderId", String.valueOf(pendingOrderId))
                .build();

        Session session = Session.create(params);
        return session.getUrl();
    }
}
