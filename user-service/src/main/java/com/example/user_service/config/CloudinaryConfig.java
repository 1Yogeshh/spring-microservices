package com.example.user_service.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dom60njrq",
                "api_key", "493279729848724",
                "api_secret", "gfeZHbcSp-RGx2trg0bCPkl0rl0"
        ));
    }
}
