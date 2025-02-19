package com.fish.shareplan.domain.checklist.dto.reponse;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<CheckListItemResponseDto> checkListItemList;

}
