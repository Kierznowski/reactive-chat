package com.example.room_service.controller;

import com.example.room_service.DTO.CreateRoomRequestDTO;
import com.example.room_service.DTO.RoomResponseDTO;
import com.example.room_service.mapper.RoomMapper;
import com.example.room_service.model.Room;
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
    private final RoomMapper mapper;

    @PostMapping
    public ResponseEntity<RoomResponseDTO> createRoom(@RequestBody CreateRoomRequestDTO request) {
        UUID ownerId = UUID.fromString(request.ownerId());
        Room room = service.createRoom(request.roomName(), ownerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(room));
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<RoomResponseDTO>> getByUser(@PathVariable("userId") String userId) {
        UUID uuid = UUID.fromString(userId);
        List<RoomResponseDTO> rooms = service.getRoomsForUser(uuid)
                .stream().map(mapper::toDto).toList();
        if(rooms.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomResponseDTO> getRoom(@PathVariable("roomId") String roomId) {
        return service.getRoom(UUID.fromString(roomId))
                .map(room -> {
                    RoomResponseDTO response = mapper.toDto(room);
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
