package com.example.user_service.service;

import com.example.user_service.DTO.RegisterUserRequestDTO;
import com.example.user_service.model.User;
import com.example.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public User registerUser(RegisterUserRequestDTO request) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(request.email());
        user.setUsername(request.username());
        user.setPasswordHash(encoder.encode(request.password()));
        user.setEnabled(true);
        user.setCreated_at(Instant.now());

        return repository.save(user);
    }

    public Optional<User> getAuthUserByUsername(String username) {
        return repository.findByUsername(username);
    }

    public Optional<User> getAuthUserByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Optional<String> getUsernameByUserId(String userId) {
        Optional<User> user = repository.findById(UUID.fromString(userId));
        return user.map(User::getUsername);
    }

}
