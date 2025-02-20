package com.fish.shareplan.domain.schedule.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fish.shareplan.domain.schedule.entity.ScheduleItem;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class ScheduleResponseDto {
    private String scheduleId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private List<ScheduleItemResponseDto> scheduleItemList;
}
