package com.fish.shareplan.domain.checklist.dto.request;

import com.fish.shareplan.domain.checklist.entity.CheckList;
import com.fish.shareplan.domain.room.entity.Room;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class CheckListRequestDto {
    private String category;
    private String content;

    public static CheckList toEntity(CheckListRequestDto dto, Room room){
        return CheckList.builder()
                .room(room)
                .category(dto.category)
                .build();
    }
}
