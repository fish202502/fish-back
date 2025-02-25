package com.fish.shareplan.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fish.shareplan.domain.chat.dto.response.ChatResponseDto;
import com.fish.shareplan.domain.chat.entity.ChatMessage;
import com.fish.shareplan.domain.chat.entity.ChatRoom;
import com.fish.shareplan.exception.ErrorCode;
import com.fish.shareplan.exception.PostException;
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
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketChatHandler extends TextWebSocketHandler {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    private final Map<WebSocketSession, String> sessionUserMap = new HashMap<>(); // 세션과 이름 매핑

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String path = session.getUri().getPath();
        String roomCode = path.substring(path.lastIndexOf('/') + 1);

        // 세션에 연결된 사용자 정보 저장
        sessionUserMap.put(session, ""); // 이름은 나중에 handleTextMessage에서 설정
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        // JSON으로 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> data = objectMapper.readValue(payload, new TypeReference<Map<String, String>>() {
        });

        String name = data.get("name");
        String roomCode = data.get("roomCode");
        String textMessage = data.get("message");

        String sessionId = session.getId();

        log.info("Received data - Name: {}, RoomCode: {}, Message: {}", name, roomCode, textMessage);

        // 클라이언트에서 이름을 처음 전송하는 경우
        if (sessionUserMap.containsKey(session) && sessionUserMap.get(session).isEmpty()) {
            sessionUserMap.put(session, name); // 클라이언트 이름 저장
            log.info("User '{}' connected", name); // 로그로 사용자 이름 출력

            // 기존 메시지들만 보내도록 변경 (새로 접속한 클라이언트에게만 이전 메시지를 전송)
            ChatRoom chatRoom = chatRoomRepository.findByRoomCode(roomCode).orElseThrow(
                    () -> new PostException(ErrorCode.NOT_FOUND_CODE)
            );
            List<ChatMessage> messageList = chatMessageRepository.findByChatRoom_RoomCodeOrderBySentAtAsc(roomCode);

            // 기존 메시지를 해당 세션에 전송
            for (ChatMessage chatMessage : messageList) {
//                String existingMessage = chatMessage.getSender() + ": " + sessionId + ":" + chatMessage.getMessage();
                String existingMessage
                        = ChatResponseDto.toMessageDto(
                        chatMessage.getSender(), chatMessage.getSessionId(), chatMessage.getMessage(), "M");
                session.sendMessage(new TextMessage(existingMessage));
            }

            // 대화 시작 메시지 전송
            String existingMessage
                    = ChatResponseDto.toMessageDto(
                    name, sessionId, "안녕하세요😊 " + name + "님!", "H");
            session.sendMessage(new TextMessage(existingMessage));
        }

        // 연결된 세션들에 메시지 전송
        for (WebSocketSession s : sessionUserMap.keySet()) {
            if (textMessage == null) return;
            String existingMessage
                    = ChatResponseDto.toMessageDto(
                    name, sessionId, textMessage, "M");
            s.sendMessage(new TextMessage(existingMessage));
        }

        // DB에 메시지 저장
        ChatRoom chatRoom = chatRoomRepository.findByRoomCode(roomCode).orElse(null);
        if (chatRoom == null) {
            chatRoom = ChatRoom.builder()
                    .roomCode(roomCode)
                    .build();
            chatRoomRepository.save(chatRoom);
        }

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(name)
                .message(textMessage)
                .sessionId(sessionId)
                .build();
        chatMessageRepository.save(chatMessage);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userName = sessionUserMap.get(session);
        sessionUserMap.remove(session); // 연결 종료 시 세션과 이름 제거
        log.info("User '{}' disconnected", userName);
    }

}
