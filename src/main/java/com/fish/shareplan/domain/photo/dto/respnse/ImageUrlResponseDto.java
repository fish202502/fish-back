package com.fish.shareplan.domain.photo.dto.respnse;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class ImageUrlResponseDto {

    private String imageId;

    private String url;
}
