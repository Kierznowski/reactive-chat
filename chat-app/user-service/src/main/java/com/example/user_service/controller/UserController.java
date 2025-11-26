package com.example.user_service.controller;

import com.example.user_service.repository.UserRepository;
import com.example.user_service.DTO.RoomDTO;
import com.example.user_service.mapper.RoomMapper;
import com.example.user_service.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    public final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public void loginUser() {
    }

    @PostMapping("/register")
    public void registerUser() {
    }

    @GetMapping("/{email}/rooms")
    public List<RoomDTO> getUserRooms(@PathVariable("email") String email) {
        return service.getRoomsForUser(email);
    }
}
