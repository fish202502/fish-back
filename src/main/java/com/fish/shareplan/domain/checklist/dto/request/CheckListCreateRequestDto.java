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
public class CheckListCreateRequestDto {
    private String category;
    private String content;

    public static CheckList toEntity(CheckListCreateRequestDto dto, Room room){
        return CheckList.builder()
                .room(room)
                .build();
    }
}
