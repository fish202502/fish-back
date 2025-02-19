package com.fish.shareplan.domain.checklist.dto.request;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class CreateCategoryRequestDto {
private String categoryId;
private  String content;
}
