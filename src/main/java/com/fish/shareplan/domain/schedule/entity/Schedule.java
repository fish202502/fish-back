package com.fish.shareplan.domain.schedule.entity;

import com.fish.shareplan.domain.room.entity.Room;
import com.fish.shareplan.domain.schedule.dto.response.ScheduleResponseDto;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "tbl_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "schedules")
@Builder
public class Schedule {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36) DEFAULT UUID()")
    private final String id = UUID.randomUUID().toString();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ScheduleItem> schedules = new ArrayList<>();

    public void update(Schedule schedule) {
        this.startTime = schedule.getStartTime();
        this.endTime = schedule.getEndTime();
    }

    public static ScheduleResponseDto toDto(Schedule schedule, List<ScheduleItem> scheduleItemList) {
        return ScheduleResponseDto.builder()
                .scheduleId(schedule.getId())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .scheduleItemList(scheduleItemList.stream()
                        .map(ScheduleItem::toDto)
                        .toList())
                .build();
    }
}
