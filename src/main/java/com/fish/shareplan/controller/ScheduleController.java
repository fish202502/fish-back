package com.fish.shareplan.controller;

import com.fish.shareplan.domain.schedule.dto.request.ScheduleItemRequestDto;
import com.fish.shareplan.domain.schedule.dto.request.ScheduleRequestDto;
import com.fish.shareplan.domain.schedule.dto.request.ScheduleUpdateRequestDto;
import com.fish.shareplan.domain.schedule.dto.response.ScheduleItemResponseDto;
import com.fish.shareplan.domain.schedule.dto.response.ScheduleResponseDto;
import com.fish.shareplan.service.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Tag(name = "일정관리", description = "일정관리 관련 API")
    @Operation(summary = "전체일정 추가 및 수정", description = "💡전체 일정을 추가 혹은 수정합니다.")
    // 여행 일정 추가 및 수정
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
    @Tag(name = "일정관리", description = "일정관리 관련 API")
    @Operation(summary = "일정 추가", description = "💡일정을 추가합니다.")
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
    @Tag(name = "일정관리", description = "일정관리 관련 API")
    @Operation(summary = "전체일정 목록 조회", description = "💡전체 일정 목록을 가져옵니다.")
    @GetMapping("/{roomCode}/{url}")
    public ResponseEntity<ScheduleResponseDto> getSchedule(
            @PathVariable String roomCode,
            @PathVariable String url
    ) {
        ScheduleResponseDto scheduleResponseDto = scheduleService.getSchedule(roomCode, url);

        return ResponseEntity.ok().body(scheduleResponseDto);
    }

    // 일정 수정
    @Tag(name = "일정관리", description = "일정관리 관련 API")
    @Operation(summary = "일정 수정", description = "💡일정 내용을 수정합니다.")
    @PutMapping("/{roomCode}/{url}")
    public ResponseEntity<ScheduleItemResponseDto> putSchedule(
            @PathVariable String roomCode,
            @PathVariable String url,
            @RequestBody ScheduleUpdateRequestDto scheduleUpdateRequestDto
    ) {

        ScheduleItemResponseDto scheduleItemResponseDto
                = scheduleService.updateSchedule(roomCode, url, scheduleUpdateRequestDto);

        return ResponseEntity.ok().body(scheduleItemResponseDto);
    }

    // 일정 삭제
    @Tag(name = "일정관리", description = "일정관리 관련 API")
    @Operation(summary = "일정 삭제", description = "💡일정을 삭제합니다.")
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
