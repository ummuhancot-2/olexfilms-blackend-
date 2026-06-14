package com.carapp.service;

import com.carapp.entity.Role;
import com.carapp.entity.User;
import com.carapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public User register(User user) {

        // email kontrol
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // şifre encode (ÇOK ÖNEMLİ)
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // default rol
        user.setRole(Role.ROLE_USER);

        // aktif kullanıcı
        user.setEnabled(true);

        return userRepository.save(user);
    }
}