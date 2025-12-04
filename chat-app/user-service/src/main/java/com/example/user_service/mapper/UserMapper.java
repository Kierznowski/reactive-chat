package com.example.user_service.mapper;

import com.example.user_service.DTO.UserAuthResponseDTO;
import com.example.user_service.DTO.UserResponseDTO;
import com.example.user_service.model.User;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserMapper {

    public UserResponseDTO toUserDTO(User user) {
        return new UserResponseDTO(user.getId().toString(), user.getEmail(), user.getUsername());
    }

    public UserAuthResponseDTO toUserAuthDTO(User user) {
        return new UserAuthResponseDTO(user.getId().toString(), user.getUsername(), user.getEmail(), user.getPasswordHash(), Set.copyOf(user.getRoles()));
    }
}
