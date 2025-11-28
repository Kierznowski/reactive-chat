package com.example.user_service.mapper;

import com.example.user_service.DTO.UserAuthDTO;
import com.example.user_service.DTO.UserDTO;
import com.example.user_service.model.User;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserMapper {

    public UserDTO toUserDTO(User user) {
        return new UserDTO(user.getId(), user.getEmail(), user.getUsername());
    }

    public UserAuthDTO toUserAuthDTO(User user) {
        return new UserAuthDTO(user.getId(), user.getUsername(), user.getEmail(), user.getPasswordHash(), Set.copyOf(user.getRoles()));
    }
}
