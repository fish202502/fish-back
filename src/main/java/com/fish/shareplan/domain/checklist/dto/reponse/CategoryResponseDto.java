package com.fish.shareplan.domain.checklist.dto.reponse;

import java.util.List;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class CategoryResponseDto {

    private String categoryId;

    private String content;

    private List<CheckListItemResponseDto> checkListItemList;

}
