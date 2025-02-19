package com.fish.shareplan.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketController {

    @GetMapping("/ws/connect")
    public String connectWebSocket() {
        // 여기에 웹소켓 연결을 위한 처리를 할 수도 있습니다.
        // 예: 클라이언트에게 웹소켓 엔드포인트 정보나 인증 정보를 제공
        return "WebSocket 연결을 시작합니다.";
    }
}