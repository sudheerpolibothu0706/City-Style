package com.ecommerce.ecommerce_app.controller;

import com.ecommerce.ecommerce_app.dto.OtpRequest;
import com.ecommerce.ecommerce_app.dto.UserResponse;
import com.ecommerce.ecommerce_app.model.User;
import com.ecommerce.ecommerce_app.service.UserService;

import jakarta.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<UserResponse> registerUser(@RequestBody User user) {
        try {
			return userService.registerUser(user);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam String email, @RequestParam String otp) {
        return userService.verifyEmail(email, otp);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(@RequestBody User userReq) {
        return userService.loginUser(userReq.getEmail(), userReq.getPassword());
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody User user) {
        return userService.sendOtpForReset(user.getEmail());
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody OtpRequest otpRequest) {
        return userService.resetPassword(otpRequest);
    }
}
