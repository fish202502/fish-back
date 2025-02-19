package com.fish.shareplan.domain.checklist.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class CategoryRequestDto {
    private String categoryId;
    @JsonProperty("content")
    private String category;
}
