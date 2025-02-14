package com.fish.shareplan.controller;

import com.fish.shareplan.domain.checklist.dto.reponse.CheckListResponseDto;
import com.fish.shareplan.domain.checklist.dto.request.CheckListCreateRequestDto;
import com.fish.shareplan.domain.checklist.dto.request.CheckListRequestDto;
import com.fish.shareplan.service.CheckListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/fish/check")
public class CheckListController {

    private final CheckListService checkListService;

    // 체크리스트 등록
    @PostMapping("/{roomCode}/{url}")
    public ResponseEntity<?> addCheckList(
            @PathVariable String roomCode,
            @PathVariable String url,
            @RequestBody CheckListCreateRequestDto dto
    ) {
        CheckListResponseDto checkLIstResponseDto = checkListService.addCheckList(roomCode, url, dto);

        return ResponseEntity.ok().body(checkLIstResponseDto);
    }

    // 체크리스트 수정
    @PostMapping("/{roomCode}/{url}/{itemId}")
    public ResponseEntity<?> updateCheckList(
            @PathVariable String roomCode,
            @PathVariable String url,
            @PathVariable String itemId,
            @RequestBody CheckListRequestDto dto
    ) {
        CheckListResponseDto checkListResponseDto = checkListService.updateCheckList(roomCode, url, itemId, dto);

        return ResponseEntity.ok().body(checkListResponseDto);

    }

}
