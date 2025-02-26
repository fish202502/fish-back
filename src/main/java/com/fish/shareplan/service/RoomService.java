package com.fish.shareplan.service;

import com.fish.shareplan.domain.chat.entity.ChatRoom;
import com.fish.shareplan.domain.room.dto.RoomResponseDto;
import com.fish.shareplan.domain.room.dto.request.SendEmailRequestDto;
import com.fish.shareplan.domain.room.entity.Room;
import com.fish.shareplan.enums.ChangeType;
import com.fish.shareplan.exception.ErrorCode;
import com.fish.shareplan.exception.PostException;
import com.fish.shareplan.repository.ChatRoomRepository;
import com.fish.shareplan.repository.RoomRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RoomService {

    private final RoomRepository roomRepository;

    private final ChatRoomRepository chatRoomRepository;

    @Value("${spring.mail.username}")
    private String mailHost;
    // 이메일 발송을 위한 객체
    private final JavaMailSender mailSender;

    // 방 생성
    public RoomResponseDto createRoom() {
        Room newRoom = roomRepository.save(new Room());
        return new RoomResponseDto(newRoom.getRoomCode(), newRoom.getReadUrl(), newRoom.getWriteUrl());
    }

    // 권한 조회
    public Map<String, Object> hasEditPermission(String roomCode, String url) {

        Room room = roomRepository.findByRoomCode(roomCode).orElseThrow(
                () -> new PostException(ErrorCode.NOT_FOUND_CODE)
        );

        if(!url.equals(room.getWriteUrl()) && !url.equals(room.getReadUrl())){
            throw new PostException(ErrorCode.NOT_FOUND_CODE);
        }
        boolean permissionFlag = url.equals(room.getWriteUrl());

        String permission = permissionFlag ? "writer" : "read";

        // 쓰기 권한 - true / 읽기 권한 - false
        return Map.of(
                "permission", permission,
                "type", permissionFlag,
                "roomCode", room.getRoomCode(),
                "readUrl", room.getReadUrl(),
                "writeUrl",room.getWriteUrl()
        );
    }

    //권한 체크 메서드
    public Room isValid(String roomCode, String url) {
        Room room = roomRepository.findByRoomCode(roomCode).orElseThrow(
                () -> new PostException(ErrorCode.NOT_FOUND_CODE)
        );

        // 쓰기 권한이 없을 경우
        if (!room.getWriteUrl().equals(url)) {
            throw new PostException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        return room;
    }

    // url 변경
    public RoomResponseDto changeUrl(String roomCode, String url, String type) {
        ChangeType changeType;

        Room room = isValid(roomCode, url);

        // String을 ChangeType enum으로 변환
        changeType = ChangeType.valueOf(type.toUpperCase());

        switch (changeType) {
            case ALL:
                room.update(Room.builder()
                        .roomCode(generateRoomCode())
                        .readUrl(generateUrl())
                        .writeUrl(generateUrl())
                        .build());
                roomRepository.save(room);
                break;
            case ROOM:
                room.update(Room.builder()
                        .roomCode(generateRoomCode())
                        .readUrl(room.getReadUrl())
                        .writeUrl(room.getWriteUrl())
                        .build());
                roomRepository.save(room);
                break;
            case WRITE:
                room.update(Room.builder()
                        .roomCode(roomCode)
                        .readUrl(room.getReadUrl())
                        .writeUrl(generateUrl())
                        .build());
                roomRepository.save(room);
                break;
            case READ:
                room.update(Room.builder()
                        .roomCode(roomCode)
                        .readUrl(generateUrl())
                        .writeUrl(room.getWriteUrl())
                        .build());
                roomRepository.save(room);
                break;
            default:
                throw new PostException(ErrorCode.INVALID_TYPE_NAME);
        }
        return RoomResponseDto.builder()
                .roomCode(room.getRoomCode())
                .writeUrl(room.getWriteUrl())
                .readUrl(room.getReadUrl())
                .build();
    }

    public static String generateUrl() {
        long timestamp = System.currentTimeMillis();

        // UUID (8자리)
        String uuidPart = UUID.randomUUID().toString().substring(0, 8);

        // 타임스탬프(10자리) + UUID 앞 8자리
        return Long.toString(timestamp).substring(0, 10) + uuidPart;
    }

    public static String generateRoomCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }


    // 이메일로 방번호 발송하기
    public void sendEmail(SendEmailRequestDto dto) {

        // 메일 전송 로직
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper messageHelper
                    = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            // 누구에게 이메일을 보낼지
            messageHelper.setTo(dto.getEmail());

            // 누가 보내는 건지
            messageHelper.setFrom(mailHost);

            // 이메일 제목 설정
            messageHelper.setSubject("생성된 url 정보입니다.");
            // 이메일 내용 설정
            messageHelper.setText(
                    "<div style=\"font-family: Arial, sans-serif; font-size: 16px; color: #333;\">" +
                    "<p>방번호: <b style=\"font-size: 18px; color: #007bff;\">" + dto.getRoomCode() + "</b></p>" +
                    "<p>읽기전용 URL: <a href=\"" + dto.getReadUrl() + "\" style=\"color: #28a745;\">" + dto.getReadUrl() + "</a></p>" +
                    "<p>수정가능 URL: <a href=\"" + dto.getWriteUrl() + "\" style=\"color: #dc3545;\">" + dto.getWriteUrl() + "</a></p>" +
                    "</div>",
                    true
            );

            // 메일 보내기
            mailSender.send(mimeMessage);

            log.info("{} 님에게 이메일이 발송되었습니다.", dto.getEmail());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("메일 발송에 실패했습니다.");
        }

    }

    public boolean deleteRoom(String roomCode, String url) {
        Room room = isValid(roomCode, url);

        ChatRoom chatRoom = chatRoomRepository.findByRoomId(room.getId()).orElse(null);
        if(chatRoom !=null){
            chatRoomRepository.delete(chatRoom);
        }
        roomRepository.delete(room);

        return true;
    }
}
