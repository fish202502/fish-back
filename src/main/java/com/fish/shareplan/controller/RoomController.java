package com.fish.shareplan.controller;

import com.fish.shareplan.domain.room.dto.RoomResponseDto;
import com.fish.shareplan.domain.room.dto.request.SendEmailRequestDto;
import com.fish.shareplan.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/fish/rooms")
public class RoomController {

    private final RoomService roomService;

    @Tag(name = "URL", description = "URL 관련 API")
    @Operation(summary = "URL 생성", description = "💡새로운 방의 URL과 코드를 생성합니다.")
    // 방 생성
    @PostMapping
    public ResponseEntity<RoomResponseDto> createRoom() {

        RoomResponseDto room = roomService.createRoom();

        return ResponseEntity.ok().body(room);
    }

    @Tag(name = "URL", description = "URL 관련 API")
    @Operation(summary = "URL 권한 조회", description = "💡방의 URL에 따른 권한을 가져옵니다.")
    // 권한 조회
    @PostMapping("/{roomCode}/{url}")
    public ResponseEntity<Map<String, Object>> permissionCheck(
            @PathVariable String roomCode,
            @PathVariable String url
    ) {
        // 쓰기 권한 - true / 읽기 권한 - false
        Map<String, Object> permission = roomService.hasEditPermission(roomCode, url);

        return ResponseEntity.ok().body(permission);
    }

    // 방 url 변경
    @Tag(name = "URL", description = "URL 관련 API")
    @Operation(summary = "URL 변경", description = "💡기존 방의 URL과 코드를 변경합니다.")
    @PutMapping("/{roomCode}/{url}")
    public ResponseEntity<RoomResponseDto> changeUrl(
            @PathVariable String roomCode,
            @PathVariable String url,
            @RequestParam String type
    ) {
        RoomResponseDto roomResponseDto = roomService.changeUrl(roomCode, url, type);

        return ResponseEntity.ok().body(roomResponseDto);
    }

    // url 메일 전송
    @Tag(name = "URL", description = "URL 관련 API")
    @Operation(summary = "URL 메일 전송", description = "💡방의 url 정보를 메일로 전송해줍니다.")
    @PostMapping("/mail")
    public ResponseEntity<?> sendEmail(
            @RequestBody SendEmailRequestDto dto
    ) {
        roomService.sendEmail(dto);

        return ResponseEntity.ok().body(Map.of("success", true));
    }

    // 방 삭제
    @Tag(name = "URL", description = "URL 관련 API")
    @Operation(summary = "URL 삭제", description = "💡방의 모든 정보를 삭제합니다.")
    @DeleteMapping("/{roomCode}/{url}")
    public ResponseEntity<?> deleteRoom(
            @PathVariable String roomCode,
            @PathVariable String url
    ) {
        boolean success = roomService.deleteRoom(roomCode, url);

        return ResponseEntity.ok().body(Map.of("success", true));
    }
}
