package com.example.room_service.mapper;

import com.example.room_service.DTO.RoomResponseDTO;
import com.example.room_service.model.Room;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoomMapper {

    public RoomResponseDTO toDto(Room room) {
        return new RoomResponseDTO(
                room.getId().toString(),
                room.getName(),
                room.getOwnerId(),
                List.copyOf(room.getMembers())
        );
    }

}
