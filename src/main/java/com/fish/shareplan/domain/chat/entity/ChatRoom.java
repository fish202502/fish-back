package com.fish.shareplan.domain.chat.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "messages")
@Builder
@Table(name = "tbl_chat_room")
@Entity
public class ChatRoom {

    @Id
    @Column(name = "id", length = 36, nullable = false)
    private final String id = UUID.randomUUID().toString(); // 방 번호 (UUID)

    @Column(name = "room_id", nullable = false)
    private String roomId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // 방 생성 시간

    @Builder.Default
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> messages = new ArrayList<>(); // 채팅 메시지 (1:N 관계)

    // 생성자, getter, setter 생략
}
