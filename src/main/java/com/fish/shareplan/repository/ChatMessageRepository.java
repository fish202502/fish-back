package com.fish.shareplan.repository;

import com.fish.shareplan.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,String> {
}
