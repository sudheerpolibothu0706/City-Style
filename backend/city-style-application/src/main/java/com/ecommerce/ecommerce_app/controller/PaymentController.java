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
        	System.out.println("payment controller to create session");
            String sessionUrl = paymentService.createSession(request);
            PaymentResponsedto response = new PaymentResponsedto(sessionUrl, null);
            System.out.println("session created sucessful");
            return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        catch (StripeException e) {
            
            PaymentResponsedto errorResponse = new PaymentResponsedto(null, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
