package com.fish.shareplan.controller;

import com.fish.shareplan.domain.schedule.dto.request.ScheduleRequestDto;
import com.fish.shareplan.domain.schedule.dto.response.ScheduleResponseDto;
import com.fish.shareplan.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/fish/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/{roomCode}/{url}")
    public ResponseEntity<Map<String, String>> addSchedule(
            @PathVariable String roomCode,
            @PathVariable String url,
            @RequestBody ScheduleRequestDto scheduleRequestDto
    ) {

        String scId = scheduleService.addSchedule(roomCode, url, scheduleRequestDto);
        return ResponseEntity.ok().body(Map.of("message", "일정이 추가되었습니다.",
                "scheduleId", scId));
    }

    @GetMapping("/{roomCode}/{url}")
    public ResponseEntity<Map<String, Object>> getSchedule(
            @PathVariable String roomCode,
            @PathVariable String url
    ) {
        List<ScheduleResponseDto> schedule = scheduleService.getSchedule(roomCode, url);

        return ResponseEntity.ok().body(Map.of("scheduleList", schedule));
    }

}
