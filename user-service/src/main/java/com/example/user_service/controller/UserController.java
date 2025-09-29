package com.example.user_service.controller;

import com.example.user_service.model.User;
import com.example.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // get all users
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // get user by id
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // create new user
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.deleteById(id);
                    return ResponseEntity.ok("User deleted successfully: " + user.getUsername());
                })
                .orElseGet(() -> ResponseEntity.status(404).body("User not found with id: " + id));
    }

}
