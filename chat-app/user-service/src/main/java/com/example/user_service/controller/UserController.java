package com.example.user_service.controller;

import com.example.user_service.DTO.RegisterUserRequestDTO;
import com.example.user_service.DTO.UserAuthResponseDTO;
import com.example.user_service.DTO.UserResponseDTO;
import com.example.user_service.mapper.UserMapper;
import com.example.user_service.model.User;
import com.example.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/auth/register")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody RegisterUserRequestDTO request) {
        User user = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toUserDTO(user));
    }

    @GetMapping("/internal/users/by-username/{username}")
    public ResponseEntity<UserAuthResponseDTO> getByUsername(@PathVariable("username") String username) {
        return userService.getAuthUserByUsername(username)
                .map(user -> ResponseEntity.ok(userMapper.toUserAuthDTO(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/internal/users/by-email/{email}")
    public ResponseEntity<UserAuthResponseDTO> getByEmail(@PathVariable("email") String email) {
        return userService.getAuthUserByEmail(email)
                .map(user -> ResponseEntity.ok(userMapper.toUserAuthDTO(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}/username")
    public ResponseEntity<String> getUsernameById(@PathVariable("userId") String userId) {
        return userService.getUsernameByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
