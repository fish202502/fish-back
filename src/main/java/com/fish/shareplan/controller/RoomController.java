package com.fish.shareplan.controller;

import com.fish.shareplan.domain.room.dto.RoomResponseDto;
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
    public ResponseEntity<RoomResponseDto> createRoom(){

        RoomResponseDto room = roomService.createRoom();

        return ResponseEntity.ok().body(room);
    }

    // 권한 조회
    @PostMapping("/{roomCode}/{url}")
    public ResponseEntity<Map<String,Object>> permissionCheck(
            @PathVariable String roomCode,
            @PathVariable String url
    ){
        // 쓰기 권한 - true / 읽기 권한 - false
        boolean editPermission = roomService.hasEditPermission(roomCode, url);

        String permission = editPermission ? "read" : "writer";

        return ResponseEntity.ok().body(
                Map.of("permission",permission
                        ,"type",editPermission));
    }

    // 방 url 변경
    @PutMapping("/{roomCode}/{url}")
    public ResponseEntity<RoomResponseDto> changeUrl(
            @PathVariable String roomCode,
            @PathVariable String url,
            @RequestParam String type
    ){
        RoomResponseDto roomResponseDto = roomService.changeUrl(roomCode, url, type);

        return ResponseEntity.ok().body(roomResponseDto);
    }
}
