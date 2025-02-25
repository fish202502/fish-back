package com.fish.shareplan.domain.chat.dto.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class ChatResponseDto {
    private String sender;
    private String sessionId;
    private String message;

    private String type;
    public static String toMessageDto(
            String sender, String sessionId, String message,String type) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        ChatResponseDto responseDto = ChatResponseDto.builder()
                .sender(sender)
                .sessionId(sessionId)
                .message(message)
                .type(type)
                .build();
        return objectMapper.writeValueAsString(responseDto);
    }
}
