package com.fish.shareplan.repository;

import com.fish.shareplan.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage,String> {

    List<ChatMessage> findByChatRoom_RoomIdOrderBySentAtAsc(String id);
}
