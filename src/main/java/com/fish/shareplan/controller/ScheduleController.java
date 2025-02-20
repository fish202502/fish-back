package com.fish.shareplan.controller;

import com.fish.shareplan.domain.schedule.dto.request.ScheduleItemRequestDto;
import com.fish.shareplan.domain.schedule.dto.request.ScheduleRequestDto;
import com.fish.shareplan.domain.schedule.dto.request.ScheduleUpdateRequestDto;
import com.fish.shareplan.domain.schedule.dto.response.ScheduleItemResponseDto;
import com.fish.shareplan.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/fish/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    // 여행 일정 추가
    @PostMapping("/trip/{roomCode}/{url}")
    public ResponseEntity<Map<String, String>> addItinerary(
            @PathVariable String roomCode,
            @PathVariable String url,
            @RequestBody ScheduleRequestDto dto
    ) {

        Map<String, String> result = scheduleService.addOrUpdateItinerary(roomCode, url, dto);

        Map.Entry<String, String> entry = result.entrySet().iterator().next();
        String key = entry.getKey();
        String scheduleId = entry.getValue();

        String message = key.equals("U") ? "전체 여행일이 변경되었습니다." : "전체 여행일이 등록되었습니다.";

        return ResponseEntity.ok().body(Map.of("message", message, "scheduleId", scheduleId));
    }


    // 일정 추가
    @PostMapping("/{roomCode}/{url}")
    public ResponseEntity<Map<String, String>> addSchedule(
            @PathVariable String roomCode,
            @PathVariable String url,
            @RequestBody ScheduleItemRequestDto scheduleItemRequestDto
    ) {

        String scheduleId = scheduleService.addSchedule(roomCode, url, scheduleItemRequestDto);
        return ResponseEntity.ok().body(Map.of("message", "일정이 추가되었습니다.",
                "scheduleId", scheduleId));
    }


    // 일정 조회
    @GetMapping("/{roomCode}/{url}")
    public ResponseEntity<Map<String, Object>> getSchedule(
            @PathVariable String roomCode,
            @PathVariable String url
    ) {
        List<ScheduleItemResponseDto> schedule = scheduleService.getSchedule(roomCode, url);

        return ResponseEntity.ok().body(Map.of("scheduleList", schedule));
    }

    // 일정 수정
    @PutMapping("/{roomCode}/{url}")
    public ResponseEntity<Map<String, String>> putSchedule(
            @PathVariable String roomCode,
            @PathVariable String url,
            @RequestBody ScheduleUpdateRequestDto scheduleUpdateRequestDto
    ) {

        String scheduleItemId = scheduleService.updateSchedule(roomCode, url, scheduleUpdateRequestDto);
        return ResponseEntity.ok().body(Map.of("message", "일정이 수정되었습니다.",
                "scheduleItemId", scheduleItemId));
    }

    // 일정 삭제
    @DeleteMapping("/{roomCode}/{url}/{scheduleItemId}")
    public ResponseEntity<Map<String, Object>> deleteSchedule(
            @PathVariable String roomCode,
            @PathVariable String url,
            @PathVariable String scheduleItemId
    ) {

        boolean deleted = scheduleService.deleteSchedule(roomCode, url, scheduleItemId);
        return ResponseEntity.ok().body(Map.of(
                "successes", deleted));
    }


}
