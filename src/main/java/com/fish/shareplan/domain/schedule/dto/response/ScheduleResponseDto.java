package com.fish.shareplan.domain.schedule.dto.response;

import com.fish.shareplan.domain.schedule.entity.QSchedule;
import com.fish.shareplan.domain.schedule.entity.QScheduleItem;
import com.querydsl.core.Tuple;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class ScheduleResponseDto {

    private String id;

    private String title;

    private String content;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    // Tuple을 DTO로 변환하는 메서드
    public static ScheduleResponseDto from(Tuple tuple) {
        return ScheduleResponseDto.builder()
                .id(tuple.get(QSchedule.schedule.id))
                .title(tuple.get(QScheduleItem.scheduleItem.title))
                .content(tuple.get(QScheduleItem.scheduleItem.content))
                .startTime(tuple.get(QScheduleItem.scheduleItem.startTime))
                .endTime(tuple.get(QScheduleItem.scheduleItem.endTime))
                .build();
    }
}
