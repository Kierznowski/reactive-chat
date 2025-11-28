package com.example.room_service.mapper;

import com.example.room_service.DTO.RoomDTO;
import com.example.room_service.model.Room;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoomMapper {

    public RoomDTO toDto(Room room) {
        return new RoomDTO(
                room.getId(),
                room.getName(),
                room.getOwnerId(),
                List.copyOf(room.getMembers())
        );
    }

}
