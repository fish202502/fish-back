package com.fish.shareplan.domain.room.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;
import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tbl_room")
public class Room {

    @Id
    @Column(name = "id", columnDefinition = "CHAR(36) DEFAULT UUID()")
    @Builder.Default
    private String id = UUID.randomUUID().toString();  // UUID를 기본 값으로 설정

    @Column(name = "room_code", nullable = false, unique = true, length = 50)
    private String roomCode;  // 방 코드

    @Column(name = "read_url", nullable = false, length = 255)
    private String readUrl;  // 읽기 전용 URL

    @Column(name = "write_url", nullable = false, length = 255)
    private String writeUrl;  // 쓰기 권한 URL

    @Builder.Default
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();  // 생성 시간 (현재 시간)


}
