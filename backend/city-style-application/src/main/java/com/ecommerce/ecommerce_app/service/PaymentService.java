package com.ecommerce.ecommerce_app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ecommerce.ecommerce_app.dto.PaymentRequestdto;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.annotation.PostConstruct;

@Service
public class PaymentService {
	
	@Value("${stripe.success.url}")
	private String successUrl;

	@Value("${stripe.cancel.url}")
	private String cancelUrl;
	
	@Value("${stripe.secret.key}")
	private String stripeApiKey;

	@PostConstruct
	public void init() {
	    Stripe.apiKey = stripeApiKey;
	}
	
	 public String createSession(PaymentRequestdto request) throws StripeException {
         SessionCreateParams params = SessionCreateParams.builder()
                 .setMode(SessionCreateParams.Mode.PAYMENT)
                 .setSuccessUrl(successUrl)
                 .setCancelUrl(cancelUrl)
                 .addLineItem(
                         SessionCreateParams.LineItem.builder()
                                 .setQuantity(request.getQuantity())
                                 .setPriceData(
                                         SessionCreateParams.LineItem.PriceData.builder()
                                                 .setCurrency(request.getCurrency().toLowerCase())
                                                 .setUnitAmount(request.getAmount())
                                                 .setProductData(
                                                         SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                 .setName(request.getProductName())
                                                                 .build()
                                                 )
                                                 .build()
                                 )
                                 .build()
                 )
                 .build();

         Session session = Session.create(params);
         return session.getUrl(); // Only return the session URL
     }
 }

