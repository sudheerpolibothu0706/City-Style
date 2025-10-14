package com.ecommerce.ecommerce_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.ecommerce_app.dto.UserReqdto;
import com.ecommerce.ecommerce_app.dto.UserResponse;
import com.ecommerce.ecommerce_app.model.User;
import com.ecommerce.ecommerce_app.service.UserService;




@RestController
@RequestMapping("/api/v1/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/registration")
	public ResponseEntity<String> saveUser(@RequestBody User user) {
		
		return userService.saveUser(user);
	}
	
	@PostMapping("/login")
	public ResponseEntity<UserResponse> loginUser(@RequestBody UserReqdto userReq) {
	    UserResponse response = userService.loginUser(userReq.getEmail(), userReq.getPassword());
	    
	    if (response.getToken() == null) {
	    	System.out.println("Token is null for user login");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response); 
	    }
	    return ResponseEntity.ok(response);
	    
	}
}
	
	