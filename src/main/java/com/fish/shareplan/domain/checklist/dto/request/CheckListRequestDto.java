package com.fish.shareplan.domain.checklist.dto.request;

import com.fish.shareplan.domain.checklist.entity.CheckList;
import com.fish.shareplan.domain.room.entity.Room;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class CheckListRequestDto {
    private String category;
    private String content;
    private Boolean isChecked;
}