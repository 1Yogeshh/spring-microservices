package com.example.user_service.controller;

import com.example.user_service.model.User;
import com.example.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.user_service.service.UserService;
import com.example.user_service.projection.UserSummary;


import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    // get all users
    @GetMapping
    public Page<User> getUsers(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "id") String sortBy) {
        return userService.getallUsers(page, size, sortBy);
        // return userRepository.findAll();

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

    @GetMapping("/search")
    public List<User> getSearchuser(@RequestParam(required = false) String username) {
        return userService.searchUser(username);
    }

    @GetMapping("/summary")
    public List<UserSummary> getUserSummaries() {
        return userRepository.findAllUserSummeries();
    }

}
