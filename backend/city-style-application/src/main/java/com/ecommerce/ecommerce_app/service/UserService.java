package com.ecommerce.ecommerce_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.ecommerce_app.config.JwtUtil;
import com.ecommerce.ecommerce_app.config.CustomUserDetails;
import com.ecommerce.ecommerce_app.dto.UserResponse;
import com.ecommerce.ecommerce_app.model.User;
import com.ecommerce.ecommerce_app.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtutil;

    public ResponseEntity<String> saveUser(User user) {
        String useremail = user.getEmail();
        Optional<User> optionalUser = userRepository.findByEmail(useremail);

        if (optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with the same email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("Registration Successful");
    }

    public UserResponse loginUser(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        
        UserResponse response= new UserResponse();
        
        if (userOptional.isEmpty()) {
        	response.setMessage("User details not found");
        	System.out.println("email is wrong");
            return response;
            
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
        	response.setMessage("Password Incorrect");
        	System.out.println("password is wrong");
            return response;
        }
        
        String token = jwtutil.generateToken(new CustomUserDetails(user));
        
        response.setMessage("Login Sucess");
        System.out.println("login sucess");
        response.setToken(token);
        System.out.println(token);
        return response;
    }

}
