package com.ecommerce.ecommerce_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ecommerce.ecommerce_app.dto.PaymentRequestdto;
import com.ecommerce.ecommerce_app.dto.PaymentResponsedto;
import com.ecommerce.ecommerce_app.service.PaymentService;
import com.stripe.exception.StripeException;


@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-session")
    public ResponseEntity<PaymentResponsedto> checkOut(@RequestBody PaymentRequestdto request) {
        try {
            String sessionUrl = paymentService.createSession(request, request.getOrderId());
            return ResponseEntity.ok(new PaymentResponsedto(sessionUrl, null));
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new PaymentResponsedto(null, e.getMessage()));
        }
    }

}
