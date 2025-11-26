package com.example.user_service.service;

import com.example.user_service.DTO.RoomDTO;
import com.example.user_service.mapper.RoomMapper;
import com.example.user_service.model.User;
import com.example.user_service.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repository;
    private final RoomMapper mapper;

    public UserService(UserRepository repository, RoomMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<RoomDTO> getRoomsForUser(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getRooms().stream()
                .map(mapper::toDto)
                .toList();
    }
}
