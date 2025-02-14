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
    public CheckListItemResponseDto addCheckList(
            String roomCode, String url,
            CheckListCreateRequestDto dto
    ) {

        Room room = roomService.isValid(roomCode, url);

        CheckList foundCheckList = checkListRepository.findByRoomId(room.getId()).orElse(null);

        if (foundCheckList != null) {
            CheckListItem checkListItem = CheckListItem.builder()
                    .checklist(foundCheckList)
                    .category(dto.getCategory())
                    .content(dto.getContent())
                    .build();

            checkListItemRepository.save(checkListItem);
            return CheckListItem.toDto(checkListItem);

            // 체크리스트가 최초 생성되었을때
        } else {
            CheckList checkList = CheckListCreateRequestDto.toEntity(dto, room);
            checkListRepository.save(checkList);

            CheckListItem checkListItem = CheckListItem.builder()
                    .checklist(checkList)
                    .category(dto.getCategory())
                    .content(dto.getContent())
                    .build();

            checkListItemRepository.save(checkListItem);
            return CheckListItem.toDto(checkListItem);
        }
    }

    // 체크리스트 수정
    public CheckListItemResponseDto updateCheckList(
            String roomCode, String url, String checkListItemId
            , CheckListRequestDto dto) {

        roomService.isValid(roomCode, url);

        CheckListItem foundCheckList = checkListItemRepository.findById(checkListItemId).orElseThrow(
                () -> new PostException(ErrorCode.NOT_FOUND_CHECKLIST)
        );
        foundCheckList.update(dto);

        return CheckListItem.toDto(foundCheckList);

    }

    // 체크리스트 조회
    public List<CheckListItemResponseDto> getCheckList(
            String roomCode, String url
    ) {
        Room room = roomService.isValid(roomCode, url);

        CheckList checkList = checkListRepository.findByRoomId(room.getId()).orElse(null);

        if (checkList != null) {
            List<CheckListItem> checkListItem = checkList.getCheckListItem();
            return checkListItem.stream().map(CheckListItem::toDto).toList();
        }
        return null;
    }
}
