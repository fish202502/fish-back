package com.fish.shareplan.repository;

import com.fish.shareplan.domain.room.entity.Room;
import com.fish.shareplan.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule,String>, ScheduleRepositoryCustom {

}
