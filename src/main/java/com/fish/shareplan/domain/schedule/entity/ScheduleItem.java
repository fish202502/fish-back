package com.fish.shareplan.domain.schedule.entity;

import com.fish.shareplan.domain.schedule.dto.request.ScheduleUpdateRequestDto;
import com.fish.shareplan.domain.schedule.dto.response.ScheduleItemResponseDto;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tbl_schedule_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "schedule")
@Builder
public class ScheduleItem {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36) DEFAULT UUID()")
    private final String id = UUID.randomUUID().toString();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // updated 편의 메서드
    public void modify(ScheduleUpdateRequestDto dto){
        this.title =dto.getTitle();
        this.content = dto.getContent();
        this.startTime = dto.getStartTime();
        this.endTime = dto.getEndTime();
        this.updatedAt = LocalDateTime.now();
    }
    public static ScheduleItemResponseDto toDto(ScheduleItem scheduleItem){
        return ScheduleItemResponseDto.builder()
                .scheduleItemId(scheduleItem.getId())
                .title(scheduleItem.getTitle())
                .content(scheduleItem.getContent())
                .startTime(scheduleItem.getStartTime())
                .endTime(scheduleItem.getEndTime())
                .build();
    }

}
