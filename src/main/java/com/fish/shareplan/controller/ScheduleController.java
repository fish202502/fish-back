package com.fish.shareplan.controller;

import com.fish.shareplan.domain.schedule.dto.request.ScheduleRequestDto;
import com.fish.shareplan.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/fish/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/{roomCode}/{url}")
    public ResponseEntity<?> addSchedule(
            @PathVariable  String roomCode,
            @PathVariable  String url,
            @RequestBody ScheduleRequestDto scheduleRequestDto
    ) {

        log.info("url입니다"+url);
        log.info(roomCode);
        String scId = scheduleService.addSchedule(roomCode, url, scheduleRequestDto);
        return ResponseEntity.ok().body(Map.of("message", "일정이 추가되었습니다.",
                "scheduleId", scId));
    }

}
