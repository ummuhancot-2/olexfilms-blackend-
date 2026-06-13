package com.carapp.payload.request;


import com.carapp.entity.Role;
import lombok.Data;

@Data
public class UserRequest {

    private String email;
    private String password;
    private Role role;
}
