package com.fish.shareplan.controller;

import com.fish.shareplan.domain.checklist.dto.reponse.CheckListItemResponseDto;
import com.fish.shareplan.domain.checklist.dto.reponse.CheckListResponseDto;
import com.fish.shareplan.domain.checklist.dto.request.CheckListCreateRequestDto;
import com.fish.shareplan.domain.checklist.dto.request.CheckListRequestDto;
import com.fish.shareplan.service.CheckListService;
import lombok.Getter;
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
    public ResponseEntity<CheckListItemResponseDto> addCheckList(
            @PathVariable String roomCode,
            @PathVariable String url,
            @RequestBody CheckListCreateRequestDto dto
    ) {
        CheckListItemResponseDto checkListItemResponseDto = checkListService.addCheckList(roomCode, url, dto);

        return ResponseEntity.ok().body(checkListItemResponseDto);
    }

    // 체크리스트 수정
    @PostMapping("/{roomCode}/{url}/{itemId}")
    public ResponseEntity<CheckListItemResponseDto> updateCheckList(
            @PathVariable String roomCode,
            @PathVariable String url,
            @PathVariable String itemId,
            @RequestBody CheckListRequestDto dto
    ) {
        CheckListItemResponseDto checkListItemResponseDto = checkListService.updateCheckList(roomCode, url, itemId, dto);

        return ResponseEntity.ok().body(checkListItemResponseDto);

    }

    // 체크리스트 조회
    @GetMapping("/{roomCode}/{url}")
    public ResponseEntity<?> getCheckList(
            @PathVariable String roomCode,
            @PathVariable String url
    ){
        CheckListResponseDto checkList = checkListService.getCheckList(roomCode, url);

        return ResponseEntity.ok().body(checkList);
    }

}
