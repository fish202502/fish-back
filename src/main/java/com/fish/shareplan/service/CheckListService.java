package com.fish.shareplan.service;

import com.fish.shareplan.domain.checklist.dto.reponse.CategoryResponseDto;
import com.fish.shareplan.domain.checklist.dto.reponse.CheckListItemResponseDto;
import com.fish.shareplan.domain.checklist.dto.reponse.CheckListResponseDto;
import com.fish.shareplan.domain.checklist.dto.request.CategoryRequestDto;
import com.fish.shareplan.domain.checklist.dto.request.CheckListCreateRequestDto;
import com.fish.shareplan.domain.checklist.dto.request.CheckListRequestDto;
import com.fish.shareplan.domain.checklist.entity.CheckList;
import com.fish.shareplan.domain.checklist.entity.CheckListCategory;
import com.fish.shareplan.domain.checklist.entity.CheckListItem;
import com.fish.shareplan.domain.room.entity.Room;
import com.fish.shareplan.exception.ErrorCode;
import com.fish.shareplan.exception.PostException;
import com.fish.shareplan.repository.CheckListCategoryRepository;
import com.fish.shareplan.repository.CheckListItemRepository;
import com.fish.shareplan.repository.CheckListRepository;
import com.fish.shareplan.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CheckListService {

    private final CheckListRepository checkListRepository;
    private final CheckListItemRepository checkListItemRepository;
    private final CheckListCategoryRepository checkListCategoryRepository;

    private final RoomRepository roomRepository;
    private final RoomService roomService;

    // 카테고리 등록
    public CategoryResponseDto addCheckListCategory(
            String roomCode, String url,
            CategoryRequestDto dto
    ) {

        Room room = roomService.isValid(roomCode, url);

        CheckList foundCheckList = checkListRepository.findByRoomId(room.getId()).orElse(null);

        if (foundCheckList != null) {
            CheckListCategory category = CheckListCategory.builder()
                    .content(dto.getCategory())
                    .checkList(foundCheckList)
                    .build();

            checkListCategoryRepository.save(category);
            return CheckListCategory.toDto(category);

            // 체크리스트가 최초 생성되었을때
        } else {
            CheckList checkList = CheckListCreateRequestDto.toEntity(room);
            checkListRepository.save(checkList);

            CheckListCategory category = CheckListCategory.builder()
                    .content(dto.getCategory())
                    .checkList(checkList)
                    .build();

            checkListCategoryRepository.save(category);
            return CheckListCategory.toDto(category);
        }
    }

    //     체크리스트 등록
    public CheckListItemResponseDto addCheckList(
            String roomCode, String url,
            CheckListCreateRequestDto dto
    ) {

        Room room = roomService.isValid(roomCode, url);

        CheckListCategory category = checkListCategoryRepository.findById(dto.getCategoryId()).orElseThrow(
                () -> new PostException(ErrorCode.NOT_FOUND_CHECKLIST_CATEGORY)
        );

        CheckListItem checkListItem = CheckListItem.builder()
                .assignee(dto.getAssignee())
                .content(dto.getContent())
                .category(category)
                .build();

        checkListItemRepository.save(checkListItem);

        return CheckListItemResponseDto.builder()
                .checkListItemId(checkListItem.getId())
                .category(category.getContent())
                .content(checkListItem.getContent())
                .isChecked(checkListItem.getIsChecked())
                .build();
    }


    // 체크리스트 카테고리 수정
    public CategoryResponseDto updateCategory(
            String roomCode, String url
            , CategoryRequestDto dto) {

        roomService.isValid(roomCode, url);

        CheckListCategory category = checkListCategoryRepository.findById(dto.getCategoryId()).orElseThrow(
                () -> new PostException(ErrorCode.NOT_FOUND_CHECKLIST)
        );

        category.update(dto);
        checkListCategoryRepository.save(category);

        return CategoryResponseDto.builder()
                .categoryId(category.getId())
                .content(category.getContent())
                .build();
    }

    // 체크리스트 수정
    public CheckListItemResponseDto updateCheckList(
            String roomCode, String url
            , CheckListRequestDto dto) {

        roomService.isValid(roomCode, url);

        CheckListItem foundCheckList = checkListItemRepository.findById(dto.getCheckListItemId()).orElseThrow(
                () -> new PostException(ErrorCode.NOT_FOUND_CHECKLIST)
        );

        foundCheckList.update(dto);
        checkListItemRepository.save(foundCheckList);

        return CheckListItem.toDto(foundCheckList);

    }

    // 체크리스트 카테고리 조회
    public List<CategoryResponseDto> getCategory(
            String roomCode, String url
    ) {
        Room room = roomRepository.findByRoomCode(roomCode).orElseThrow(
                () -> new PostException(ErrorCode.NOT_FOUND_CODE));

        CheckList checkList = checkListRepository.findByRoomId(room.getId()).orElse(null);

        if(checkList != null){
            List<CheckListCategory> categoryList = checkList.getCheckListCategories();
            return categoryList.stream().map(CheckListCategory::toDto).toList();
        }
        return null;
    }


    // 체크리스트 조회
    public CheckListResponseDto getCheckList(
            String roomCode, String url
    ) {
        Room room = roomRepository.findByRoomCode(roomCode).orElseThrow(
                () -> new PostException(ErrorCode.NOT_FOUND_CODE));

        CheckList checkList = checkListRepository.findByRoomId(room.getId()).orElse(null);

        if (checkList != null) {

            List<CheckListCategory> categoryList = checkList.getCheckListCategories();
            List<CategoryResponseDto> categoryDtoList
                    = categoryList.stream()
                    .map(category -> {
                        CategoryResponseDto categoryDto = CheckListCategory.toDto(category);
                        List<CheckListItem> checkListItemList = category.getCheckListItemList();
                        List<CheckListItemResponseDto> checkListItemDto
                                = checkListItemList.stream().map(CheckListItem::toDto).toList();
                        categoryDto.setCheckListItemList(checkListItemDto);
                        return categoryDto;
                    }).toList();

            return CheckListResponseDto.builder()
                    .checkListId(checkList.getId())
                    .category(categoryDtoList)
                    .build();
        }
        return null;
    }

    // 체크리스트 카테고리 삭제
    public boolean deleteCategory(String roomCode, String url, String categoryId) {
        Room room = roomService.isValid(roomCode, url);

        // 체크리스트가 존재하는 지 먼저 확인
        CheckListCategory category = checkListCategoryRepository.findById(categoryId).orElseThrow(
                () -> new PostException(ErrorCode.NOT_FOUND_CHECKLIST_CATEGORY)
        );

        checkListCategoryRepository.deleteById(categoryId);

        return true;
    }

    // 체크리스트 삭제
    public boolean deleteCheckList(String roomCode, String url, String checkListId) {
        Room room = roomService.isValid(roomCode, url);

        // 체크리스트가 존재하는 지 먼저 확인
        CheckListItem checkListItem = checkListItemRepository.findById(checkListId).orElseThrow(
                () -> new PostException(ErrorCode.NOT_FOUND_CHECKLIST)
        );

        checkListItemRepository.deleteById(checkListId);

        return true;
    }
}

