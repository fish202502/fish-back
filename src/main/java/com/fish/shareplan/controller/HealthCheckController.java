package com.fish.shareplan.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@Slf4j
public class HealthCheckController {

    @Tag(name = "서버", description = "서버 상태 관련 API")
    @Operation(summary = "서버 상태관리", description = "💡서버 상태를 반환합니다.")
    @GetMapping("/status")
    public ResponseEntity<?> healthCheck() {
        String checkMessage = "server is running....";
        log.info(checkMessage);
        return ResponseEntity.ok()
                .body(Map.of(
                        "message", checkMessage
                ));
    }
}