package com.ecommerce.ecommerce_app.config;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

	@Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", System.getenv().getOrDefault("CLOUDINARY_CLOUD_NAME", "dg3lkz3jn"),
                "api_key", System.getenv().getOrDefault("CLOUDINARY_API_KEY", "574765988165147"),
                "api_secret", System.getenv().getOrDefault("CLOUDINARY_API_SECRET", "t8Gj-_RwzNE_MMKv-42ZnW5emkY")
        ));
    }
}
