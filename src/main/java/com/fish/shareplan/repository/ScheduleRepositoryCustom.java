package com.fish.shareplan.repository;

import com.fish.shareplan.domain.schedule.dto.response.ScheduleResponseDto;
import com.fish.shareplan.domain.schedule.entity.Schedule;

import java.util.List;

public interface ScheduleRepositoryCustom {

    List<ScheduleResponseDto> findAllSchedule(String roomId);
}
