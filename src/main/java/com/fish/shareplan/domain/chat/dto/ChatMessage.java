package com.fish.shareplan.domain.chat.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private String sender;
    private String content;
    private String type; // 메시지 타입 (예: CHAT, JOIN, LEAVE)
}
