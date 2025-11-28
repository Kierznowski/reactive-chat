package com.example.user_service.service;

import com.example.user_service.DTO.RegisterRequest;
import com.example.user_service.DTO.UserAuthDTO;
import com.example.user_service.DTO.UserDTO;
import com.example.user_service.mapper.UserMapper;
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
    private final UserMapper mapper;

    public UserDTO registerUser(RegisterRequest request) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(request.email());
        user.setUsername(request.username());
        user.setPasswordHash(encoder.encode(request.password()));
        user.setEnabled(true);
        user.setCreated_at(Instant.now());

        User savedUser = repository.save(user);

        return mapper.toUserDTO(savedUser);
    }

    public Optional<UserAuthDTO> getAuthUserByUsername(String username) {
        return repository.findByUsername(username).map(mapper::toUserAuthDTO);
    }

    public Optional<UserAuthDTO> getAuthUserByEmail(String email) {
        return repository.findByEmail(email).map(mapper::toUserAuthDTO);
    }

}
