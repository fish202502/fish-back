package com.fish.shareplan.controller;

import com.fish.shareplan.domain.checklist.dto.reponse.CategoryResponseDto;
import com.fish.shareplan.domain.checklist.dto.reponse.CheckListItemResponseDto;
import com.fish.shareplan.domain.checklist.dto.reponse.CheckListResponseDto;
import com.fish.shareplan.domain.checklist.dto.request.CategoryRequestDto;
import com.fish.shareplan.domain.checklist.dto.request.CheckListCreateRequestDto;
import com.fish.shareplan.domain.checklist.dto.request.CheckListRequestDto;
import com.fish.shareplan.service.CheckListService;
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
@RequestMapping("/api/fish/check")
public class CheckListController {

    private final CheckListService checkListService;


    @Tag(name = "체크리스트", description = "체크리스트 관련 API")
    @Operation(summary = "체크리스트 카테고리 추가", description = "💡체크리스트의 카테고리를 추가합니다.")
    // 체크리스트 카테고리 추가
    @PostMapping("/category/{roomCode}/{url}")
    public ResponseEntity<CategoryResponseDto> addCheckList(
            @PathVariable String roomCode,
            @PathVariable String url,
            @RequestBody CategoryRequestDto dto
    ) {
        CategoryResponseDto checkListItemResponseDto
                = checkListService.addCheckListCategory(roomCode, url, dto);

        return ResponseEntity.ok().body(checkListItemResponseDto);
    }


    @Tag(name = "체크리스트", description = "체크리스트 관련 API")
    @Operation(summary = "체크리스트 추가", description = "💡체크리스트를 추가합니다.")
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

    @Tag(name = "체크리스트", description = "체크리스트 관련 API")
    @Operation(summary = "체크리스트 카테고리 수정", description = "💡체크리스트의 카테고리를 수정합니다.")
    // 체크리스트 카테고리 수정
    @PutMapping("/category/{roomCode}/{url}")
    public ResponseEntity<CategoryResponseDto> updateCheckList(
            @PathVariable String roomCode,
            @PathVariable String url,
            @RequestBody CategoryRequestDto dto
    ) {
        CategoryResponseDto categoryResponseDto
                = checkListService.updateCategory(roomCode, url, dto);

        return ResponseEntity.ok().body(categoryResponseDto);

    }

    @Tag(name = "체크리스트", description = "체크리스트 관련 API")
    @Operation(summary = "체크리스트 수정", description = "💡체크리스트를 수정합니다.")
    // 체크리스트 수정
    @PutMapping("/{roomCode}/{url}")
    public ResponseEntity<CheckListItemResponseDto> updateCheckList(
            @PathVariable String roomCode,
            @PathVariable String url,
            @RequestBody CheckListRequestDto dto
    ) {
        CheckListItemResponseDto checkListItemResponseDto = checkListService.updateCheckList(roomCode, url, dto);

        return ResponseEntity.ok().body(checkListItemResponseDto);

    }

    @Tag(name = "체크리스트", description = "체크리스트 관련 API")
    @Operation(summary = "체크리스트 목록 조회", description = "💡체크리스트의 목록을 가져옵니다.")
    // 체크리스트 조회
    @GetMapping("/{roomCode}/{url}")
    public ResponseEntity<CheckListResponseDto> getCheckList(
            @PathVariable String roomCode,
            @PathVariable String url
    ) {
        CheckListResponseDto checkList = checkListService.getCheckList(roomCode, url);

        return ResponseEntity.ok().body(checkList);
    }


    @Tag(name = "체크리스트", description = "체크리스트 관련 API")
    @Operation(summary = "체크리스트 카테고리 목록 조회", description = "💡체크리스트의 카테고리 목록을 가져옵니다.")
    // 체크리스트 카테고리 조회
    @GetMapping("/category/{roomCode}/{url}")
    public ResponseEntity<List<CategoryResponseDto>> getCategoryList(
            @PathVariable String roomCode,
            @PathVariable String url
    ) {
        List<CategoryResponseDto> category = checkListService.getCategory(roomCode, url);

        return ResponseEntity.ok().body(category);
    }

    @Tag(name = "체크리스트", description = "체크리스트 관련 API")
    @Operation(summary = "체크리스트 카테고리 삭제", description = "💡체크리스트의 카테고리를 삭제합니다.")
    // 체크리스트 카테고리 삭제
    @DeleteMapping("/category/{roomCode}/{url}/{categoryId}")
    public ResponseEntity<Map<String, Object>> deleteCategory(
            @PathVariable String roomCode,
            @PathVariable String url,
            @PathVariable String categoryId
    ) {

        boolean deleted = checkListService.deleteCategory(roomCode, url, categoryId);
        return ResponseEntity.ok().body(Map.of(
                "successes", deleted));
    }

    @Tag(name = "체크리스트", description = "체크리스트 관련 API")
    @Operation(summary = "체크리스트 삭제", description = "💡체크리스트를 삭제합니다.")
    // 체크리스트 삭제
    @DeleteMapping("/{roomCode}/{url}/{checkListItemId}")
    public ResponseEntity<Map<String, Object>> deleteCheckList(
            @PathVariable String roomCode,
            @PathVariable String url,
            @PathVariable String checkListItemId
    ) {

        boolean deleted = checkListService.deleteCheckList(roomCode, url, checkListItemId);
        return ResponseEntity.ok().body(Map.of(
                "successes", deleted));
    }
}
