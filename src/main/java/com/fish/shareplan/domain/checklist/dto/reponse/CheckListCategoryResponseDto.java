package com.fish.shareplan.domain.checklist.dto.reponse;

import java.util.List;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class CheckListCategoryResponseDto {

    private String categoryId;

    private List<CheckListItemResponseDto> checkListItemList;

}
