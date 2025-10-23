package com.ecommerce.ecommerce_app.controller;

import com.ecommerce.ecommerce_app.service.ImageUploadService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final ImageUploadService imageUploadService;

    public AdminController(ImageUploadService imageUploadService) {
        this.imageUploadService = imageUploadService;
    }
    
    @PostMapping("/upload-images")
    public ResponseEntity<?> uploadImages(@RequestParam("files") List<MultipartFile> files) {
        try {
        	System.out.println("File received: ");
            List<String> imageUrls = imageUploadService.uploadImages(files);
            System.out.println("image url"+ imageUrls);
            return ResponseEntity.ok(imageUrls);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Upload failed: " + e.getMessage());
        }
    }

    
}
