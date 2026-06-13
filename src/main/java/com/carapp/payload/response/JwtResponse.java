package com.carapp.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
public class JwtResponse {
    private String token;

    public JwtResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}