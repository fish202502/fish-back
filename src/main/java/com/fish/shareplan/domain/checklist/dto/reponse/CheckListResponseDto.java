package com.fish.shareplan.domain.checklist.dto.reponse;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class CheckListResponseDto {
    private String checkListId;

    private List<CategoryResponseDto> category;
}
