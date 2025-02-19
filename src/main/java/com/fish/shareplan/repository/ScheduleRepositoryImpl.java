package com.fish.shareplan.repository;

import com.fish.shareplan.domain.schedule.dto.response.ScheduleResponseDto;
import com.fish.shareplan.domain.schedule.entity.QSchedule;
import com.fish.shareplan.domain.schedule.entity.QScheduleItem;
import com.fish.shareplan.domain.schedule.entity.Schedule;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.fish.shareplan.domain.schedule.entity.QSchedule.schedule;
import static com.fish.shareplan.domain.schedule.entity.QScheduleItem.scheduleItem;

@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepositoryCustom {

    private final JPAQueryFactory factory;

    @Override
    public List<ScheduleResponseDto> findAllSchedule(String roomId) {

        List<Tuple> tupleList = factory.select(
                        schedule.id,
                        scheduleItem.title,
                        scheduleItem.content,
                        scheduleItem.startTime,
                        scheduleItem.endTime
                )
                .from(schedule)
                .innerJoin(schedule.schedules,scheduleItem)
                .where(schedule.room.id.eq(roomId))
                .fetch();

        return tupleList
                .stream()
                .map(ScheduleResponseDto::from)
                .collect(Collectors.toList());
    }
}
