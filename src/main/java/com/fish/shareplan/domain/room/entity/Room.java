package com.fish.shareplan.domain.room.entity;

import com.fish.shareplan.domain.chat.entity.ChatRoom;
import com.fish.shareplan.domain.checklist.entity.CheckList;
import com.fish.shareplan.domain.expense.entity.Expense;
import com.fish.shareplan.domain.photo.entity.Photo;
import com.fish.shareplan.domain.schedule.entity.Schedule;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbl_room")
public class Room {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36) DEFAULT UUID()")
    private final String id = UUID.randomUUID().toString();  // UUID를 기본 값으로 설정

    @Builder.Default
    @Column(name = "room_code", nullable = false, unique = true, length = 50)
    private String roomCode = generateRoomCode();  // 방 코드

    @Builder.Default
    @Column(name = "read_url", nullable = false, length = 255)
    private String readUrl = generateUrl();  // 읽기 전용 URL

    @Builder.Default
    @Column(name = "write_url", nullable = false, length = 255)
    private String writeUrl = generateUrl();  // 쓰기 권한 URL

    @Column(name = "created_at", nullable = false, updatable = false)
    private final LocalDateTime createdAt = LocalDateTime.now();  // 생성 시간 (현재 시간)

    @OneToOne(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private Expense expense;

    @OneToOne(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private Photo photo;

    @OneToOne(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private CheckList checkList;

    @OneToOne(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private Schedule schedule;

    public static String generateUrl() {
        long timestamp = System.currentTimeMillis();

        // UUID (8자리)
        String uuidPart = UUID.randomUUID().toString().substring(0, 8);

        // 타임스탬프(10자리) + UUID 앞 8자리
        return Long.toString(timestamp).substring(0, 10) + uuidPart;
    }

    public static String generateRoomCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public void update(Room room){
        this.roomCode = room.getRoomCode();
        this.writeUrl = room.getWriteUrl();
        this.readUrl = room.getReadUrl();
    }
}
