package com.fish.shareplan.repository;

import com.fish.shareplan.domain.room.entity.Room;
import com.fish.shareplan.domain.schedule.entity.ScheduleItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleItemRepository extends JpaRepository<ScheduleItem,String > {

    List<ScheduleItem> findByScheduleId(String scheduleId);
}
