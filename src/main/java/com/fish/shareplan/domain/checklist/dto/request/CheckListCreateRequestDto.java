package com.fish.shareplan.domain.checklist.dto.request;

import com.fish.shareplan.domain.checklist.dto.reponse.CheckListCategoryResponseDto;
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
    private String categoryId;
    private String assignee;
    private String content;

    public static CheckList toEntity(Room room){
        return CheckList.builder()
                .room(room)
                .build();
    }
}
