package com.carapp.controller;

import com.carapp.entity.User;
import com.carapp.payload.request.UserRequest;
import com.carapp.payload.response.UserResponse;
import com.carapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        System.out.println("CONTROLLER CALISTI");
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

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(userService.getUsers(page, size));
    }




}