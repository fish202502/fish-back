package com.fish.shareplan.controller;

import com.fish.shareplan.domain.room.dto.RoomResponseDto;
import com.fish.shareplan.domain.room.dto.request.SendEmailRequestDto;
import com.fish.shareplan.service.RoomService;
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

    // 방 생성
    @PostMapping
    public ResponseEntity<RoomResponseDto> createRoom() {

        RoomResponseDto room = roomService.createRoom();

        return ResponseEntity.ok().body(room);
    }

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
    @PostMapping("/mail")
    public ResponseEntity<?> sendEmail(
            @RequestBody SendEmailRequestDto dto
    ) {
        roomService.sendEmail(dto);

        return ResponseEntity.ok().body(Map.of("success", true));
    }

    // 방 삭제
    @DeleteMapping("/{roomCode}/{url}")
    public ResponseEntity<?> deleteRoom(
            @PathVariable String roomCode,
            @PathVariable String url
    ) {
        boolean success = roomService.deleteRoom(roomCode, url);

        return ResponseEntity.ok().body(Map.of("success", true));
    }
}
