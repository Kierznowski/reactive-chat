package com.example.user_service.controller;

import com.example.user_service.DTO.CreateRoomRequest;
import com.example.user_service.DTO.RoomDTO;
import com.example.user_service.mapper.RoomMapper;
import com.example.user_service.model.Room;
import com.example.user_service.model.User;
import com.example.user_service.repository.RoomRepository;
import com.example.user_service.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    public final RoomRepository roomRepository;
    public final UserRepository userRepository;
    public final RoomMapper mapper;

    public RoomController(RoomRepository roomRepository, UserRepository userRepository, RoomMapper mapper) {
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }


    @PostMapping
    public RoomDTO createRoom(@RequestBody CreateRoomRequest request) {
        Optional<User> optOwner = userRepository.findByEmail(request.userId());
        if(optOwner.isEmpty()) throw new UsernameNotFoundException("User not found");

        User owner = optOwner.get();

        Room room = new Room();
        room.setOwner(owner);
        room.setName(request.roomName());
        owner.getRooms().add(room);

        roomRepository.save(room);
        userRepository.save(owner);
        return mapper.toDto(room);
    }
}
