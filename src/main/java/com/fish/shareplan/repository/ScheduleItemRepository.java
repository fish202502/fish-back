package com.fish.shareplan.repository;

import com.fish.shareplan.domain.schedule.entity.ScheduleItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleItemRepository extends JpaRepository<ScheduleItem,Long> {
}
