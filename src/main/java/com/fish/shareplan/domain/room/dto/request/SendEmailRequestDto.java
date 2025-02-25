package com.fish.shareplan.domain.room.dto.request;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class SendEmailRequestDto {
private String email;
private String roomCode;
private String readUrl;
private String writeUrl;
}
