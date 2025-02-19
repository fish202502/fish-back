package com.fish.shareplan.domain.checklist.dto.request;

import com.fish.shareplan.domain.checklist.dto.reponse.CheckListResponseDto;
import com.fish.shareplan.domain.checklist.entity.CheckList;
import com.fish.shareplan.domain.checklist.entity.CheckListItem;
import com.fish.shareplan.domain.room.entity.Room;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class CheckListRequestDto {
    private String checkListItemId;
    private String assignee;
    private String content;
    private Boolean isChecked;

//    public static CheckListItem toEntity(CheckListRequestDto dto){
//        return CheckListItem.builder()
//                .category(dto.getCategory())
//                .content(dto.getContent())
//                .isChecked(dto.getIsChecked())
//                .build();
//    }
}