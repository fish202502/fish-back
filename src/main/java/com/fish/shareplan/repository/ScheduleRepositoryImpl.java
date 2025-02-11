package com.fish.shareplan.repository;

import com.fish.shareplan.domain.schedule.entity.Schedule;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom {

    private final JPAQueryFactory factory;

    @Override
    public List<Schedule> findAllSchedule() {


        return List.of();
    }
}
