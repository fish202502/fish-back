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
public class ScheduleItemResponseDto {

    private String scheduleItemId;

    private String title;

    private String content;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    // Tuple을 DTO로 변환하는 메서드
    public static ScheduleItemResponseDto from(Tuple tuple) {
        return ScheduleItemResponseDto.builder()
                .scheduleItemId(tuple.get(QScheduleItem.scheduleItem.id))
                .title(tuple.get(QScheduleItem.scheduleItem.title))
                .content(tuple.get(QScheduleItem.scheduleItem.content))
                .startTime(tuple.get(QScheduleItem.scheduleItem.startTime))
                .endTime(tuple.get(QScheduleItem.scheduleItem.endTime))
                .build();
    }
}
