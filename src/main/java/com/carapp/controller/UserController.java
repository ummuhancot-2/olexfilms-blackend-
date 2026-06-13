package com.carapp.controller;

import com.carapp.entity.User;
import com.carapp.payload.request.UserRequest;
import com.carapp.payload.response.UserResponse;
import com.carapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponse createUser(
            @RequestBody UserRequest request) {

        return userService.createUser(request);
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable Long id,
                                   @RequestBody UserRequest request) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}