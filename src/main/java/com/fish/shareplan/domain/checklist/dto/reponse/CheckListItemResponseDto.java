package com.fish.shareplan.domain.checklist.dto.reponse;


import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class CheckListItemResponseDto {

    private String checkListItemId;

    private String category;

    private String assignee;

    private String content;

    private Boolean isChecked;
}
