package com.fish.shareplan.controller;

import com.fish.shareplan.domain.room.dto.RoomResponseDto;
import com.fish.shareplan.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
