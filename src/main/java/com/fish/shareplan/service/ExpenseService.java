package com.fish.shareplan.service;

import com.fish.shareplan.domain.expense.dto.request.ExpenseRequestDto;
import com.fish.shareplan.domain.expense.dto.response.ExpenseResponseDto;
import com.fish.shareplan.domain.expense.entity.Expense;
import com.fish.shareplan.domain.expense.entity.ExpenseItem;
import com.fish.shareplan.domain.expense.entity.ReceiptImage;
import com.fish.shareplan.domain.room.entity.Room;
import com.fish.shareplan.domain.schedule.dto.request.ScheduleRequestDto;
import com.fish.shareplan.domain.schedule.entity.Schedule;
import com.fish.shareplan.domain.schedule.entity.ScheduleItem;
import com.fish.shareplan.exception.ErrorCode;
import com.fish.shareplan.exception.PostException;
import com.fish.shareplan.repository.ExpenseItemRepository;
import com.fish.shareplan.repository.ExpenseRepository;
import com.fish.shareplan.repository.ReceiptImageRepository;
import com.fish.shareplan.repository.RoomRepository;
import com.fish.shareplan.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ExpenseService {

    private final FileUploadUtil fileUploadUtil;  // 로컬

    private final RoomRepository roomRepository;

    private final ExpenseRepository expenseRepository;
    private final ExpenseItemRepository expenseItemRepository;
    private final ReceiptImageRepository receiptImageRepository;


    //권한 체크 메서드
    public Room isValid(String roomCode, String url) {
        Room room = roomRepository.findByRoomCode(roomCode).orElseThrow(
                () -> new PostException(ErrorCode.NOT_FOUND_CODE)
        );

        // 쓰기 권한이 없을 경우
        if (!room.getWriteUrl().equals(url)) {
            throw new PostException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        return room;
    }

    // 지출 추가
    public ExpenseResponseDto addExpense(
            String roomCode, String url
            , List<MultipartFile> images
            , ExpenseRequestDto expenseRequestDto) {

        Expense expense = Expense.builder()
                .room(isValid(roomCode, url))
                .build();
        expenseRepository.save(expense);

        ExpenseItem expenseItem = ExpenseItem.builder()
                .expense(expense)
                .amount(expenseRequestDto.getAmount())
                .spender(expenseRequestDto.getSpender())
                .description(expenseRequestDto.getDescription())
                .spentAt(expenseRequestDto.getSpendAt())
                .build();
        expenseItemRepository.save(expenseItem);

        // 이미지가 있을 때 영수증 이미지 처리
        if (images != null && !images.isEmpty()) {
            List<Map<String, String>> receiptIdList
                    = processImages(images, expenseRequestDto, expenseItem);

            return ExpenseResponseDto.builder()
                    .expenseId(expense.getId())
                    .expenseItemId(expenseItem.getId())
                    .receipt(receiptIdList)
                    .build();
        } else {
            return ExpenseResponseDto.builder()
                    .expenseId(expense.getId())
                    .expenseItemId(expenseItem.getId())
                    .build();
        }
    }

    // 이미지 처리 메서드
    private List<Map<String, String>> processImages(List<MultipartFile> images
            , ExpenseRequestDto expenseRequestDto, ExpenseItem expenseItem) {
        log.debug("start process Image!!");

        List<Map<String, String>> receiptIdList = new ArrayList<>();
        // 이미지들을 서버(/upload 폴더)에 저장
        if (images != null && !images.isEmpty()) {
            log.debug("save process Image!!");

            int order = 1; // 이미지 순서
            for (MultipartFile image : images) {
                // 파일 서버에 저장
                String uploadedUrl = fileUploadUtil.saveFile(image);

                // 이미지들을 데이터베이스 post_images 테이블에 insert
                log.debug("success to save file at: {}", uploadedUrl);

                // 이미지들을 데이터베이스 post_images 테이블에 insert
                ReceiptImage receiptImage = ReceiptImage.builder()
                        .expenseItem(expenseItem)
                        .imageUrl(uploadedUrl)
                        .build();

                receiptImageRepository.save(receiptImage);
                receiptIdList.add(
                        Map.of(
                                "receiptId", receiptImage.getId(),
                                "receiptUrl", receiptImage.getImageUrl()
                        ));
            }
        }
        return receiptIdList;
    }
}
