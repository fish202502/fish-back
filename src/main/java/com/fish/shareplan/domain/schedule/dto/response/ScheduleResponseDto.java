package com.fish.shareplan.domain.schedule.dto.response;

import com.fish.shareplan.domain.schedule.entity.ScheduleItem;
import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class ScheduleResponseDto {
    private String scheduleId;

    private List<ScheduleItem> scheduleItemList;
}
