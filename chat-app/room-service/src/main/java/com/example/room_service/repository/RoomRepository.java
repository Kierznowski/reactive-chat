package com.example.room_service.repository;

import com.example.room_service.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("select r from Room r join r.members m where m = :memberId")
    List<Room> findByMemberId(@Param("memberId") UUID memberId);
}
