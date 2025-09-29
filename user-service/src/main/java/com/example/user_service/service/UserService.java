package com.example.user_service.service;

import com.example.user_service.model.User;
import com.example.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import com.example.user_service.service.DummyService;

import java.util.*;

@Service
public class UserService {


    @Autowired
    public DummyService dummyService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Cloudinary cloudinary;

    public User registerUser(String username, String password, MultipartFile image) throws Exception {
        if (userRepository.existsByUsername(username)) {
            throw new Exception("Username already taken");
        }

        User.UserBuilder userBuilder = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password));

        if (image != null && !image.isEmpty()) {
            // Upload to Cloudinary
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(),
                    ObjectUtils.asMap("folder", "user_images"));

            String imageUrl = uploadResult.get("secure_url").toString();
            userBuilder.imageName(image.getOriginalFilename())
                    .imageType(image.getContentType())
                    .imageUrl(imageUrl); // Save only URL in DB
        }

        User user = userBuilder.build();
        dummyService.call(username);
        return userRepository.save(user);
    }

    public User authenticate(String username, String password) throws Exception {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("User not found"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new Exception("Invalid password");
        }
        return user;
    }
}
