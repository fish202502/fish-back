package com.fish.shareplan.service;

import com.fish.shareplan.domain.checklist.dto.reponse.CheckListItemResponseDto;
import com.fish.shareplan.domain.checklist.dto.reponse.CheckListResponseDto;
import com.fish.shareplan.domain.checklist.dto.request.CheckListCreateRequestDto;
import com.fish.shareplan.domain.checklist.dto.request.CheckListRequestDto;
import com.fish.shareplan.domain.checklist.entity.CheckList;
import com.fish.shareplan.domain.checklist.entity.CheckListItem;
import com.fish.shareplan.domain.room.entity.Room;
import com.fish.shareplan.exception.ErrorCode;
import com.fish.shareplan.exception.PostException;
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

    private final RoomService roomService;

    // 체크리스트 등록
    public CheckListResponseDto addCheckList(
            String roomCode, String url,
            CheckListCreateRequestDto dto
    ) {

        Room room = roomService.isValid(roomCode, url);

        CheckList checkList = CheckListCreateRequestDto.toEntity(dto, room);
        checkListRepository.save(checkList);

        CheckListItem checkListItem = CheckListItem.builder()
                .checklist(checkList)
                .category(dto.getCategory())
                .content(dto.getContent())
                .build();

        checkListItemRepository.save(checkListItem);

        List<CheckListItemResponseDto> checkListItemList = new ArrayList<>();

        checkListItemList.add(
                CheckListItemResponseDto.builder()
                        .category(checkListItem.getCategory())
                        .content(checkListItem.getContent())
                        .isChecked(checkListItem.getIsChecked())
                        .build());

        ;
        return CheckListResponseDto.builder()
                .checkListId(checkList.getId())
                .checkListItem(checkListItemList).build();

    }

    // 체크리스트 수정
    public CheckListResponseDto updateCheckList(
            String roomCode, String url, String checkListId
            , CheckListRequestDto dto) {

        roomService.isValid(roomCode, url);

        CheckList checkList = checkListRepository.findById(checkListId).orElseThrow(
                () -> new PostException(ErrorCode.NOT_FOUND_CHECKLIST)
        );
        List<CheckListItem> checkListItem1 = checkList.getCheckListItem();
        CheckListItem checkListItem = (CheckListItem) checkListItem1;
        checkListItem.update(dto);

        checkList.update(dto, checkListItem);

        checkListRepository.save(checkList);
        checkListItemRepository.save(checkListItem);

        List<CheckListItemResponseDto> checkListItemList = new ArrayList<>();

        checkListItemList.add(
                CheckListItemResponseDto.builder()
                        .category(checkListItem.getCategory())
                        .content(checkListItem.getContent())
                        .isChecked(checkListItem.getIsChecked())
                        .build());

        ;
        return CheckListResponseDto.builder()
                .checkListId(checkList.getId())
                .checkListItem(checkListItemList).build();
    }

    // 체크리스트 조회
    public CheckListResponseDto getCheckList(
            String roomCode, String url
    ) {
        Room room = roomService.isValid(roomCode, url);

        CheckList checkList = checkListRepository.findByRoomId(room.getId());

        return CheckListResponseDto.builder()
                .checkListItem(checkList.getCheckListItem().stream()
                        .map(CheckListItem::toDto).toList())
                .checkListId(checkList.getId())
                .build();
    }


}
