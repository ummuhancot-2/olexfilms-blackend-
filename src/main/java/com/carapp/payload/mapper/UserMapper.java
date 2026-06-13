package com.carapp.payload.mapper;


import com.carapp.entity.User;
import com.carapp.payload.request.UserRequest;
import com.carapp.payload.response.UserResponse;

public class UserMapper {

    public static User toEntity(UserRequest request) {
        return User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .role(request.getRole())
                .build();
    }

    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}