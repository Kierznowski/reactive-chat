package com.example.room_service.controller;

import com.example.room_service.DTO.CreateRoomRequest;
import com.example.room_service.DTO.RoomDTO;
import com.example.room_service.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService service;

    @PostMapping
    public ResponseEntity<RoomDTO> createRoom(@RequestBody CreateRoomRequest request) {
        UUID owner = UUID.fromString(request.ownerId());
        RoomDTO dto = service.createRoom(request.roomName(), owner);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/by-user/{userId}")
    public List<RoomDTO> getByUser(@PathVariable String userId) {
        return service.getRoomsForUser(UUID.fromString(userId));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDTO> getRoom(@PathVariable("roomId") Long roomId) {
        return service.getRoom(roomId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
