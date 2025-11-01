package com.ecommerce.ecommerce_app.service;

import com.ecommerce.ecommerce_app.config.CustomUserDetails;
import com.ecommerce.ecommerce_app.config.JwtUtil;
import com.ecommerce.ecommerce_app.dto.OtpRequest;
import com.ecommerce.ecommerce_app.dto.UserResponse;
import com.ecommerce.ecommerce_app.model.User;
import com.ecommerce.ecommerce_app.repository.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtutil;

    @Autowired
    private JavaMailSender mailSender;

    private static final Pattern PASSWORD_REGEX =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");

    private String generateOtp() {
        int otp = new Random().nextInt(999999);
        return String.format("%06d", otp);
    }

    private void sendEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        message.setFrom("sudheerpolibhotu@gmail.com"); 
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true); 
        mailSender.send(message);
    }
    private boolean isOtpValid(User user, String otp) {
        if (user.getVerificationOtp() == null || !user.getVerificationOtp().equals(otp)) return false;
        if (user.getOtpGeneratedAt() == null) return false;
        return user.getOtpGeneratedAt().isAfter(LocalDateTime.now().minusMinutes(10));
    }

    public ResponseEntity<UserResponse> registerUser(User user) throws MessagingException {
        UserResponse response = new UserResponse();

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            response.setMessage("User with this email already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        if (!PASSWORD_REGEX.matcher(user.getPassword()).matches()) {
            response.setMessage("Password must include uppercase, lowercase, number, special char (min 8 chars)");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        String otp = generateOtp();
        user.setVerificationOtp(otp);
        user.setOtpGeneratedAt(LocalDateTime.now());

        userRepository.save(user);
        sendEmail(user.getEmail(), "Verify your account", "Your OTP is: " + otp);

        response.setMessage(" Check your email for OTP verification.");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<String> verifyEmail(String email, String otp) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

        User user = userOptional.get();

        if (user.isVerified()) return ResponseEntity.badRequest().body("Email already verified");

        if (!isOtpValid(user, otp)) return ResponseEntity.badRequest().body("Invalid or expired OTP");

        user.setVerified(true);
        user.setVerificationOtp(null);
        user.setOtpGeneratedAt(null);
        userRepository.save(user);

        return ResponseEntity.ok("Registred successfully");
    }

    public ResponseEntity<UserResponse> loginUser(String email, String password) {
        UserResponse response = new UserResponse();
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            response.setMessage("User not found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        User user = userOptional.get();

        if (!user.isVerified()) {
            response.setMessage("Please verify your email before logging in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            response.setMessage("Incorrect password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String token = jwtutil.generateToken(new CustomUserDetails(user));
        response.setToken(token);
        response.setMessage("Login successful");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<String> sendOtpForReset(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

        User user = userOptional.get();
        String otp = generateOtp();
        user.setVerificationOtp(otp);
        user.setOtpGeneratedAt(LocalDateTime.now());
        userRepository.save(user);

        try {
			sendEmail(email, "Reset your password", "Your OTP is: " + otp);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return ResponseEntity.ok("OTP sent to your email");
    }

    public ResponseEntity<String> resetPassword(OtpRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

        User user = userOptional.get();

        if (!isOtpValid(user, request.getOtp()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired OTP");

        if (!PASSWORD_REGEX.matcher(request.getNewPassword()).matches())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Password must include uppercase, lowercase, number, special char (min 8 chars)");

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setVerificationOtp(null);
        user.setOtpGeneratedAt(null);
        userRepository.save(user);

        return ResponseEntity.ok("Password reset successful");
    }
}
