package com.oliwier.insyrest.controller;

import com.oliwier.insyrest.entity.User;
import com.oliwier.insyrest.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/pending-users")
    public ResponseEntity<List<User>> getPendingUsers() {
        return ResponseEntity.ok(userRepository.findByEnabledFalse());
    }

    @GetMapping("/pending-users/count")
    public ResponseEntity<Map<String, Integer>> getPendingCount() {
        int count = userRepository.findByEnabledFalse().size();
        return ResponseEntity.ok(Map.of("count", count));
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<?> approveUser(@PathVariable Integer id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setEnabled(true);
                    userRepository.save(user);
                    return ResponseEntity.ok().body(Map.of("message", "User approved"));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> declineUser(@PathVariable Integer id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.ok().body(Map.of("message", "User deleted"));
        }
        return ResponseEntity.notFound().build();
    }
}
