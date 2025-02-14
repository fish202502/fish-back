package com.fish.shareplan.domain.checklist.dto.reponse;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class CheckLIstResponseDto {
    private String checkListId;

    private String category;

    private String content;

    private boolean isChecked;
}
