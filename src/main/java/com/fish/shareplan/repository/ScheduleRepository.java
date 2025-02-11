package com.fish.shareplan.repository;

import com.fish.shareplan.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule,String>, ScheduleRepositoryCustom {


}
