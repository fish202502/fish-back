package com.fish.shareplan.domain.schedule.entity;

import com.fish.shareplan.domain.room.entity.Room;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Column(name = "created_at", nullable = false, updatable = false)
    private final LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "schedule",cascade = CascadeType.REMOVE,orphanRemoval = true) // fetchType default = LAZY
    @Builder.Default
    // 리스트 객체 선언해줘야함
    private List<ScheduleItem> schedules = new ArrayList<>();
}
