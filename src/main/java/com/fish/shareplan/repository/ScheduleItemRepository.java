package com.fish.shareplan.repository;

import com.fish.shareplan.domain.room.entity.Room;
import com.fish.shareplan.domain.schedule.entity.ScheduleItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScheduleItemRepository extends JpaRepository<ScheduleItem,String > {
    Optional<ScheduleItem> findByScheduleId(String roomCode); // scchedulId로 Room 조회
}
