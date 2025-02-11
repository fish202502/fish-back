package com.fish.shareplan.domain.room.dto;

import jakarta.persistence.Column;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class RoomResponseDto {

    private String roomCode;  // 방 코드

    private String readUrl;  // 읽기 전용 URL

    private String writeUrl;  // 쓰기 권한 URL
}
