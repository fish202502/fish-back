package com.fish.shareplan.service;

import com.fish.shareplan.domain.checklist.dto.reponse.CheckLIstResponseDto;
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

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CheckListService {

    private final CheckListRepository checkListRepository;
    private final CheckListItemRepository checkListItemRepository;

    private final RoomService roomService;

    // 체크리스트 등록
    public CheckLIstResponseDto addCheckList(
            String roomCode, String url,
            CheckListRequestDto dto
    ) {

        Room room = roomService.isValid(roomCode, url);

        CheckList checkList = CheckListRequestDto.toEntity(dto, room);
        checkListRepository.save(checkList);

        CheckListItem checkListItem = CheckListItem.builder()
                .checklist(checkList)
                .content(dto.getContent())
                .build();

        checkListItemRepository.save(checkListItem);

        return CheckLIstResponseDto.builder()
                .checkListId(checkList.getId())
                .category(checkList.getCategory())
                .content(checkListItem.getContent())
                .isChecked(checkListItem.isChecked())
                .build();
    }


}
