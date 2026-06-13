package com.carapp.payload.response;

import com.carapp.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

    private Long id;
    private String email;
    private Role role;
}