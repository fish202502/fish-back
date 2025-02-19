package com.fish.shareplan.controller;

import com.fish.shareplan.domain.checklist.dto.reponse.CheckListCategoryResponseDto;
import com.fish.shareplan.domain.checklist.dto.reponse.CheckListItemResponseDto;
import com.fish.shareplan.domain.checklist.dto.reponse.CheckListResponseDto;
import com.fish.shareplan.domain.checklist.dto.request.CheckListCategoryRequestDto;
import com.fish.shareplan.domain.checklist.dto.request.CheckListCreateRequestDto;
import com.fish.shareplan.domain.checklist.dto.request.CheckListRequestDto;
import com.fish.shareplan.service.CheckListService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/fish/check")
public class CheckListController {

    private final CheckListService checkListService;

        // 체크리스트 카테고리 추가
        @PostMapping("/category/{roomCode}/{url}")
    public ResponseEntity<CheckListCategoryResponseDto> addCheckList(
            @PathVariable String roomCode,
            @PathVariable String url,
            @RequestBody CheckListCategoryRequestDto dto
    ) {
        CheckListCategoryResponseDto checkListItemResponseDto
                = checkListService.addCheckListCategory(roomCode, url, dto);

        return ResponseEntity.ok().body(checkListItemResponseDto);
    }

//    // 체크리스트 등록
//    @PostMapping("/{roomCode}/{url}")
//    public ResponseEntity<CheckListItemResponseDto> addCheckList(
//            @PathVariable String roomCode,
//            @PathVariable String url,
//            @RequestBody CheckListCreateRequestDto dto
//    ) {
//        CheckListItemResponseDto checkListItemResponseDto = checkListService.addCheckList(roomCode, url, dto);
//
//        return ResponseEntity.ok().body(checkListItemResponseDto);
//    }
//
//    // 체크리스트 수정
//    @PutMapping("/{roomCode}/{url}/{itemId}")
//    public ResponseEntity<CheckListItemResponseDto> updateCheckList(
//            @PathVariable String roomCode,
//            @PathVariable String url,
//            @PathVariable String itemId,
//            @RequestBody CheckListRequestDto dto
//    ) {
//        CheckListItemResponseDto checkListItemResponseDto = checkListService.updateCheckList(roomCode, url, itemId, dto);
//
//        return ResponseEntity.ok().body(checkListItemResponseDto);
//
//    }

//    // 체크리스트 조회
//    @GetMapping("/{roomCode}/{url}")
//    public ResponseEntity<List<CheckListItemResponseDto>> getCheckList(
//            @PathVariable String roomCode,
//            @PathVariable String url
//    ){
//        List<CheckListItemResponseDto> checkList = checkListService.getCheckList(roomCode, url);
//
//        return ResponseEntity.ok().body(checkList);
//    }
//
//    // 체크리스트 삭제
//    @DeleteMapping("/{roomCode}/{url}")
//    public ResponseEntity<Map<String, Object>> deleteCheckList(
//            @PathVariable String roomCode,
//            @PathVariable String url,
//            @RequestParam String checkListItemId
//    ) {
//
//        boolean deleted = checkListService.deleteCheckList(roomCode, url, checkListItemId);
//        return ResponseEntity.ok().body(Map.of(
//                "successes", deleted));
//    }
}
