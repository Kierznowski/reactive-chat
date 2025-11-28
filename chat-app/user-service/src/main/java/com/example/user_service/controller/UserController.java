package com.example.user_service.controller;

import com.example.user_service.DTO.RegisterRequest;
import com.example.user_service.DTO.UserAuthDTO;
import com.example.user_service.DTO.UserDTO;
import com.example.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody RegisterRequest request) {
        UserDTO userDTO = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @GetMapping("/internal/users/by-username/{username}")
    public ResponseEntity<UserAuthDTO> getByUsername(@PathVariable("username") String username) {
        return userService.getAuthUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/internal/users/by-email/{email}")
    public ResponseEntity<UserAuthDTO> getByEmail(@PathVariable("email") String email) {
        return userService.getAuthUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
