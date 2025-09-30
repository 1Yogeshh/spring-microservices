package com.example.user_service.service;

import com.example.user_service.model.User;
import com.example.user_service.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import com.example.user_service.service.DummyService;
import com.netflix.discovery.converters.Auto;
import com.example.user_service.filter.UserSpecification;
import com.example.user_service.repository.RoleRepository;
import com.example.user_service.model.Role;

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

    @Autowired
    private RoleRepository roleRepository;

    public User registerUser( String name, String username, String email, String password, MultipartFile image, String roleName)
            throws Exception {

        if (username == null || username.isEmpty()) {
            throw new Exception("Username is required");
        }

        if (name == null || name.isEmpty()) {
            throw new Exception("Name is required");
        }

        // Password required
        if (password == null || password.isEmpty()) {
            throw new Exception("Password is required");
        }

        // Email required
        if (email == null || email.isEmpty()) {
            throw new Exception("Email is required");
        }
        if (userRepository.existsByUsername(username)) {
            throw new Exception("Username already taken");
        }

        if (userRepository.existsByEmail(email)) {
            throw new Exception("Email already registered");
        }

        // Default role = USER
        if (roleName == null || roleName.isEmpty()) {
            roleName = "USER";
        }

        Role role = roleRepository.findByName(roleName.toUpperCase());
        if (role == null) {
            // Role DB me nahi hai, create new
            role = Role.builder().name(roleName.toUpperCase()).build();
            role = roleRepository.save(role);
        }
        User.UserBuilder userBuilder = User.builder()
                .username(username)
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(role);

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

    public Page<User> getallUsers(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return userRepository.findAll(pageable);
    }

    public List<User> searchUser(String username) {
        return userRepository.findAll(
                Specification.where(UserSpecification.hasUername(username)));
    }
}
