package com.fish.shareplan.controller;


import com.fish.shareplan.domain.expense.dto.request.ExpenseRequestDto;
import com.fish.shareplan.domain.expense.dto.response.ExpenseCreateResponseDto;
import com.fish.shareplan.domain.expense.dto.response.ExpenseItemDto;
import com.fish.shareplan.domain.expense.dto.response.ExpenseResponseDto;
import com.fish.shareplan.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/fish/expense")
public class ExpenseController {

    private final ExpenseService expenseService;

    @Tag(name = "지출경비", description = "지출경비 관련 API")
    @Operation(summary = "지출경비 내역 추가", description = "💡지출경비 내역을 추가합니다.")
    // 지출 등록
    @PostMapping("/{roomCode}/{url}")
    public ResponseEntity<ExpenseCreateResponseDto> addExpense(
            @PathVariable String roomCode,
            @PathVariable String url,
            @RequestPart("expense") ExpenseRequestDto expenseRequestDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {

        ExpenseCreateResponseDto expenseResponseDto
                = expenseService.addExpense(roomCode, url, images, expenseRequestDto);

        return ResponseEntity.ok().body(expenseResponseDto);
    }

    @Tag(name = "지출경비", description = "지출경비 관련 API")
    @Operation(summary = "지출경비 내역 조회", description = "💡지출경비의 목록을 가져옵니다.")
    // 지출 조회
    @GetMapping("/{roomCode}/{url}")
    public ResponseEntity<List<ExpenseResponseDto>> getExpense(
            @PathVariable String roomCode,
            @PathVariable String url
    ) {

        List<ExpenseResponseDto> expense = expenseService.getExpense(roomCode, url);

        return ResponseEntity.ok().body(expense);
    }

    @Tag(name = "지출경비", description = "지출경비 관련 API")
    @Operation(summary = "지출경비 내역 수정", description = "💡지출경비 내역을 수정합니다.")
    // 지출 내용 수정
    @PutMapping("/{roomCode}/{url}/{expenseItemId}")
    public ResponseEntity<ExpenseItemDto> updateExpense(
            @PathVariable String roomCode,
            @PathVariable String url,
            @PathVariable String expenseItemId,
            @RequestPart("expense") ExpenseRequestDto expenseRequestDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {

        ExpenseItemDto expenseItemDto
                = expenseService.updateExpense(roomCode, url, expenseItemId, images, expenseRequestDto);

        return ResponseEntity.ok().body(expenseItemDto);
    }

    @Tag(name = "지출경비", description = "지출경비 관련 API")
    @Operation(summary = "지출경비 내역 삭제", description = "💡지출경비 내역을 삭제합니다.")
    // 지출 삭제
    @DeleteMapping("/{roomCode}/{url}/{expenseItemId}")
    public ResponseEntity<Map<String, Object>> deleteSchedule(
            @PathVariable String roomCode,
            @PathVariable String url,
            @PathVariable String expenseItemId
    ) {

        boolean deleted = expenseService.deleteSchedule(roomCode, url, expenseItemId);
        return ResponseEntity.ok().body(Map.of(
                "successes", deleted));
    }

}
