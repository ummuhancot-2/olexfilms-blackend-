package com.carapp.service;



import com.carapp.entity.Role;
import com.carapp.entity.User;
import com.carapp.payload.mapper.UserMapper;
import com.carapp.payload.request.UserRequest;
import com.carapp.payload.response.UserResponse;
import com.carapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserResponse createUser(UserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = UserMapper.toEntity(request);

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        User savedUser = userRepository.save(user);

        return UserMapper.toResponse(savedUser);
    }


    public UserResponse updateUser(Long id, UserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔥 SADECE GÖNDERİLEN ALANLARI UPDATE ET

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // ❗ CRITICAL FIX: role zorunlu değil
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        return UserMapper.toResponse(userRepository.save(user));
    }

    public ResponseEntity<UserResponse> deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔥 response'a çevirmek için önce kaydet
        UserResponse response = UserMapper.toResponse(user);

        userRepository.delete(user);

        return ResponseEntity.ok(response);
    }

    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserMapper.toResponse(user);
    }


    public Page<UserResponse> getUsers(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<User> users = userRepository.findAll(pageable);

        return users.map(UserMapper::toResponse);
    }


}
