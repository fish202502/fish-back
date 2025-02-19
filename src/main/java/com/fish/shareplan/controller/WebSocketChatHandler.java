package com.fish.shareplan.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class WebSocketChatHandler extends TextWebSocketHandler {

    private final Map<WebSocketSession, String> sessionUserMap = new HashMap<>(); // 세션과 이름 매핑

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 연결 시 특별히 처리할 내용이 필요 없다면 그대로 두어도 됩니다.
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
            // 클라이언트에서 이름을 처음 전송하는 경우
            sessionUserMap.put(session, payload); // 클라이언트 이름 저장
            log.info("User '{}' connected", payload); // 로그로 사용자 이름 출력
            // 처음 이름을 받은 후, 대화 시작 메시지 전송
            session.sendMessage(new TextMessage("Welcome " + payload + "! Start chatting."));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessionUserMap.remove(session); // 연결 종료 시 이름 제거
        log.info("User disconnected");
    }
}
