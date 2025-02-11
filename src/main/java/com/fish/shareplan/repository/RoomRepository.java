package com.fish.shareplan.repository;

import com.fish.shareplan.domain.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    Optional<Room> findByRoomCode(String roomCode); // roomCode로 Room 조회

}
