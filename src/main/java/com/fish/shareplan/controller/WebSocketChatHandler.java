package com.fish.shareplan.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fish.shareplan.domain.chat.entity.ChatMessage;
import com.fish.shareplan.domain.chat.entity.ChatRoom;
import com.fish.shareplan.repository.ChatMessageRepository;
import com.fish.shareplan.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketChatHandler extends TextWebSocketHandler {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    private final Map<WebSocketSession, String> sessionUserMap = new HashMap<>(); // 세션과 이름 매핑

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        // JSON으로 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> data
                = objectMapper.readValue(payload, new TypeReference<Map<String, String>>() {
        });
        log.info("test data : {}",data);

        String name = data.get("name");
        String roomCode = data.get("roomCode");
        String textMessage = data.get("message");

        // 서버에서 받은 데이터로 처리 (예: 로그로 출력)
        System.out.println("Received name: " + name + ", roomCode: " + roomCode);

        if (sessionUserMap.containsKey(session)) {
            // 이름을 이미 받은 경우
            String userName = sessionUserMap.get(session);
            for (WebSocketSession s : sessionUserMap.keySet()) {
                s.sendMessage(new TextMessage(userName + ": " + textMessage));
            }

            // DB 저장
            ChatRoom chatRoom = chatRoomRepository.findByRoomCode(roomCode).orElse(
                    chatRoomRepository.save(ChatRoom.builder()
                            .roomCode(roomCode)
                            .build())
            );

            ChatMessage chatMessage = ChatMessage.builder()
                    .chatRoom(chatRoom)
                    .sender(name)
                    .message(textMessage)
                    .build();
            chatMessageRepository.save(chatMessage);

        } else {
            // 클라이언트에서 이름을 처음 전송하는 경우
            sessionUserMap.put(session, name); // 클라이언트 이름 저장
            log.info("User '{}' connected", name); // 로그로 사용자 이름 출력
            // 처음 이름을 받은 후, 대화 시작 메시지 전송
            session.sendMessage(new TextMessage("안녕하세요😊 " + name + "님!"));
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionUserMap.remove(session); // 연결 종료 시 이름 제거
        log.info("User disconnected");
    }
}
