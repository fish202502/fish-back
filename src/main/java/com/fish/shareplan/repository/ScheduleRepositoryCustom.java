package com.fish.shareplan.repository;

import com.fish.shareplan.domain.schedule.dto.response.ScheduleItemResponseDto;

import java.util.List;

public interface ScheduleRepositoryCustom {

    List<ScheduleItemResponseDto> findAllSchedule(String roomId);
}
