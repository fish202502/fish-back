package com.fish.shareplan.service;

import com.fish.shareplan.domain.checklist.dto.reponse.CheckListCategoryResponseDto;
import com.fish.shareplan.domain.checklist.dto.reponse.CheckListItemResponseDto;
import com.fish.shareplan.domain.checklist.dto.reponse.CheckListResponseDto;
import com.fish.shareplan.domain.checklist.dto.request.CheckListCategoryRequestDto;
import com.fish.shareplan.domain.checklist.dto.request.CheckListCreateRequestDto;
import com.fish.shareplan.domain.checklist.dto.request.CheckListRequestDto;
import com.fish.shareplan.domain.checklist.entity.CheckList;
import com.fish.shareplan.domain.checklist.entity.CheckListCategory;
import com.fish.shareplan.domain.checklist.entity.CheckListItem;
import com.fish.shareplan.domain.room.entity.Room;
import com.fish.shareplan.domain.schedule.entity.ScheduleItem;
import com.fish.shareplan.exception.ErrorCode;
import com.fish.shareplan.exception.PostException;
import com.fish.shareplan.repository.CheckListCategoryRepository;
import com.fish.shareplan.repository.CheckListItemRepository;
import com.fish.shareplan.repository.CheckListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CheckListService {

    private final CheckListRepository checkListRepository;
    private final CheckListItemRepository checkListItemRepository;
    private final CheckListCategoryRepository checkListCategoryRepository;

    private final RoomService roomService;

    // 카테고리 등록
    public CheckListCategoryResponseDto addCheckListCategory(
            String roomCode, String url,
            CheckListCategoryRequestDto dto
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
}

//    // 체크리스트 수정
//    public CheckListItemResponseDto updateCheckList(
//            String roomCode, String url, String checkListItemId
//            , CheckListRequestDto dto) {
//
//        roomService.isValid(roomCode, url);
//
//        CheckListItem foundCheckList = checkListItemRepository.findById(checkListItemId).orElseThrow(
//                () -> new PostException(ErrorCode.NOT_FOUND_CHECKLIST)
//        );
//        foundCheckList.update(dto);
//
//        return CheckListItem.toDto(foundCheckList);
//
//    }

//    // 체크리스트 조회
//    public List<CheckListItemResponseDto> getCheckList(
//            String roomCode, String url
//    ) {
//        Room room = roomService.isValid(roomCode, url);
//
//        CheckList checkList = checkListRepository.findByRoomId(room.getId()).orElse(null);
//
//        if (checkList != null) {
//            List<CheckListItem> checkListItem = checkList.getCheckListItem();
//            return checkListItem.stream().map(CheckListItem::toDto).toList();
//        }
//        return null;
//    }
//
//    // 체크리스트 삭제
//    public boolean deleteCheckList(String roomCode, String url, String checkListId) {
//        Room room = roomService.isValid(roomCode, url);
//
//        // 체크리스트가 존재하는 지 먼저 확인
//        CheckListItem checkListItem = checkListItemRepository.findById(checkListId).orElseThrow(
//                () -> new PostException(ErrorCode.NOT_FOUND_SCHEDULE)
//        );
//
//        checkListItemRepository.deleteById(checkListId);
//
//        return true;
//    }


