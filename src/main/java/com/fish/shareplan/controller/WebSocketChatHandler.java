package com.fish.shareplan.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class WebSocketChatHandler extends TextWebSocketHandler {

    private final Map<WebSocketSession, String> sessionUserMap = new HashMap<>(); // 세션과 이름 매핑

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 클라이언트가 연결되면 이름을 받거나 기본값을 설정
        session.sendMessage(new TextMessage("Please provide your name:"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        if (sessionUserMap.containsKey(session)) {
            // 이름을 이미 받은 경우에는 메시지를 전달
            String userName = sessionUserMap.get(session);
            for (WebSocketSession s : sessionUserMap.keySet()) {
                s.sendMessage(new TextMessage(userName + ": " + payload));
            }
        } else {
            // 이름을 아직 받지 않은 경우
            sessionUserMap.put(session, payload); // 클라이언트 이름 저장
            session.sendMessage(new TextMessage("Welcome " + payload + "!"));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionUserMap.remove(session); // 연결 종료 시 이름 제거
    }
}
