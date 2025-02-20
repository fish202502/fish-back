package com.fish.shareplan.domain.chat.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "chatRoom")
@Builder
@Entity
@Table(name = "tbl_chat_message")
public class ChatMessage {

    @Id
    @Column(name = "id", length = 36, nullable = false)
    private final String id = UUID.randomUUID().toString(); // 메시지 ID (UUID)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom chatRoom; // 방 번호 (FK, `ChatRoom`과 연결)

    @Column(name = "sender", nullable = false)
    private String sender; // 메시지 보낸 사람

    @Column(name = "message", columnDefinition = "TEXT", nullable = false)
    private String message; // 메시지 내용

    @CreationTimestamp
    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt; // 보낸 시간

    // Getters, Setters, Constructor
}