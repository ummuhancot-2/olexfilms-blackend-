package com.carapp.initializer;

import com.carapp.entity.Role;
import com.carapp.entity.User;
import com.carapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (userRepository.existsByEmail("admin@carapp.com")) {
            return;
        }

        User admin = new User();
        admin.setEmail("admin@carapp.com");
        admin.setPassword(passwordEncoder.encode("Admin123*"));
        admin.setRole(Role.ADMIN);
        admin.setEnabled(true);


        userRepository.save(admin);
    }
}