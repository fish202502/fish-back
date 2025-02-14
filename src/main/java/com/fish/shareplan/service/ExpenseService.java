package com.fish.shareplan.service;

import com.fish.shareplan.domain.expense.dto.request.ExpenseRequestDto;
import com.fish.shareplan.domain.expense.dto.response.ExpenseCreateResponseDto;
import com.fish.shareplan.domain.expense.dto.response.ExpenseItemDto;
import com.fish.shareplan.domain.expense.dto.response.ExpenseResponseDto;
import com.fish.shareplan.domain.expense.dto.response.ReceiptDto;
import com.fish.shareplan.domain.expense.entity.Expense;
import com.fish.shareplan.domain.expense.entity.ExpenseItem;
import com.fish.shareplan.domain.expense.entity.ReceiptImage;
import com.fish.shareplan.domain.room.entity.Room;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public ExpenseCreateResponseDto addExpense(
            String roomCode, String url
            , List<MultipartFile> images
            , ExpenseRequestDto expenseRequestDto) {


        Room room = isValid(roomCode, url);

        Expense foundExpense = expenseRepository.findByRoomId(room.getId()).orElse(null);

        if (foundExpense != null) {
            ExpenseItem expenseItem = ExpenseItem.builder()
                    .expense(foundExpense)
                    .amount(expenseRequestDto.getAmount())
                    .spender(expenseRequestDto.getSpender())
                    .description(expenseRequestDto.getDescription())
                    .spentAt(expenseRequestDto.getSpendAt())
                    .build();
            expenseItemRepository.save(expenseItem);

            // 이미지가 있을 때 영수증 이미지 처리
            if (images != null && !images.isEmpty()) {
                List<ReceiptImage> receiptList
                        = processImages(images, expenseRequestDto, expenseItem);

                return ExpenseCreateResponseDto.builder()
                        .expenseId(foundExpense.getId())
                        .expenseItemId(expenseItem.getId())
                        .receipt(receiptList.stream().map(ReceiptImage::toDto).toList())
                        .build();
            } else {
                return ExpenseCreateResponseDto.builder()
                        .expenseId(foundExpense.getId())
                        .expenseItemId(expenseItem.getId())
                        .build();
            }
        } else {
            Expense newExpense = Expense.builder()
                    .room(room)
                    .build();
            expenseRepository.save(newExpense);

            ExpenseItem expenseItem = ExpenseItem.builder()
                    .expense(newExpense)
                    .amount(expenseRequestDto.getAmount())
                    .spender(expenseRequestDto.getSpender())
                    .description(expenseRequestDto.getDescription())
                    .spentAt(expenseRequestDto.getSpendAt())
                    .build();
            expenseItemRepository.save(expenseItem);

            // 이미지가 있을 때 영수증 이미지 처리
            if (images != null && !images.isEmpty()) {
                List<ReceiptImage> receiptList
                        = processImages(images, expenseRequestDto, expenseItem);

                return ExpenseCreateResponseDto.builder()
                        .expenseId(newExpense.getId())
                        .expenseItemId(expenseItem.getId())
                        .receipt(receiptList.stream().map(ReceiptImage::toDto).toList())
                        .build();
            } else {
                return ExpenseCreateResponseDto.builder()
                        .expenseId(newExpense.getId())
                        .expenseItemId(expenseItem.getId())
                        .build();
            }
        }

    }

    // 이미지 처리 메서드
    private List<ReceiptImage> processImages(List<MultipartFile> images
            , ExpenseRequestDto expenseRequestDto, ExpenseItem expenseItem) {
        log.debug("start process Image!!");

        List<ReceiptImage> receiptList = new ArrayList<>();
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
                receiptList.add(receiptImage);
            }
        }
        return receiptList;
    }

    // 지출 조회
    public List<ExpenseResponseDto> getExpense(String roomCode, String url) {

        Room room = roomRepository.findByRoomCode(roomCode).orElseThrow(
                () -> new PostException(ErrorCode.NOT_FOUND_CODE)
        );

        String roomId = room.getId();

        return expenseRepository.findAllExpense(roomId);
    }

    // 지출 내역 수정
    public ExpenseItemDto updateExpense(
            String roomCode, String url
            , String expenseId
            , List<MultipartFile> images
            , ExpenseRequestDto expenseRequestDto) {

        isValid(roomCode, url);

        ExpenseItem expense = expenseItemRepository.findById(expenseId).orElseThrow(
                () -> new PostException(ErrorCode.NOT_FOUND_EXPENSE)
        );

        // 이미지 수정이 필요할 때 삭제 후 다시 save
        if (images != null && !images.isEmpty()) {
            receiptImageRepository.deleteByExpenseItemId(expense.getExpense().getId());
            List<ReceiptImage> imageList = processImages(images, expenseRequestDto, expense);

            expense.update(expenseRequestDto);

            ExpenseItemDto dto = ExpenseItem.toDto(expense);
            dto.setReceiptList(imageList.stream().map(ReceiptImage::toDto).toList());

            return dto;
        }

        expense.update(expenseRequestDto);
        expenseItemRepository.save(expense);

        return ExpenseItem.toDto(expense, expense.getReceiptImages());
    }

    // 지출 삭제
    public boolean deleteSchedule(String roomCode, String url, String expenseId) {
        Room room = roomRepository.findByRoomCode(roomCode).orElseThrow(
                () -> new PostException(ErrorCode.NOT_FOUND_CODE)
        );

        // 쓰기 권한이 없을 경우
        if (!room.getWriteUrl().equals(url)) {
            throw new PostException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        // 지출이 존재하는 지 먼저 확인
        expenseRepository.findById(expenseId).orElseThrow(
                () -> new PostException(ErrorCode.NOT_FOUND_EXPENSE)
        );

        expenseRepository.deleteById(expenseId);

        return true;
    }

}
