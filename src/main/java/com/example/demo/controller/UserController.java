package com.example.demo.controller;

import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService _userService;

    @PostMapping("")
    ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setResult(_userService.createRequest(request));
        return apiResponse;
    }

    @GetMapping("")
    ResponseEntity<?> getUsers() {
        try {
            return ResponseEntity.ok(_userService.getUsers());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    ResponseEntity<?> getUser(@PathVariable String userId) {
        return ResponseEntity.ok(_userService.getUser(userId));
    }

    @DeleteMapping("/{userId}")
    ResponseEntity<?> deleteUser(@PathVariable String userId) {
        return ResponseEntity.ok(_userService.removeUser(userId));
    }
}
