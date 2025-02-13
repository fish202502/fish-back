package com.fish.shareplan.controller;


import com.fish.shareplan.domain.expense.dto.request.ExpenseRequestDto;
import com.fish.shareplan.domain.expense.dto.response.ExpenseCreateResponseDto;
import com.fish.shareplan.domain.expense.dto.response.ExpenseItemDto;
import com.fish.shareplan.domain.expense.dto.response.ExpenseResponseDto;
import com.fish.shareplan.service.ExpenseService;
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


    // 지출 조회
    @GetMapping("/{roomCode}/{url}")
    public ResponseEntity<List<ExpenseResponseDto>> getExpense(
            @PathVariable String roomCode,
            @PathVariable String url
    ) {

        List<ExpenseResponseDto> expense = expenseService.getExpense(roomCode, url);

        return ResponseEntity.ok().body(expense);
    }

    // 지출 내용 수정
    @PutMapping("/{roomCode}/{url}/{expenseId}")
    public ResponseEntity<ExpenseItemDto> updateExpense(
            @PathVariable String roomCode,
            @PathVariable String url,
            @PathVariable String expenseId,
            @RequestPart("expense") ExpenseRequestDto expenseRequestDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) {

        ExpenseItemDto expenseItemDto
                = expenseService.updateExpense(roomCode, url, expenseId, images, expenseRequestDto);

        return ResponseEntity.ok().body(expenseItemDto);
    }

}
