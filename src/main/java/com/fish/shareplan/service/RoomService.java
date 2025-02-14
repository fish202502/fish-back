package com.fish.shareplan.service;

import com.fish.shareplan.domain.room.dto.RoomResponseDto;
import com.fish.shareplan.domain.room.entity.Room;
import com.fish.shareplan.enums.ChangeType;
import com.fish.shareplan.exception.ErrorCode;
import com.fish.shareplan.exception.PostException;
import com.fish.shareplan.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RoomService {

    private final RoomRepository roomRepository;

    // 방 생성
    public RoomResponseDto createRoom() {
        Room newRoom = roomRepository.save(new Room());
        return new RoomResponseDto(newRoom.getRoomCode(), newRoom.getReadUrl(), newRoom.getWriteUrl());
    }

    // 권한 조회
    public boolean hasEditPermission(String roomCode, String url) {

        Room room = roomRepository.findByRoomCode(roomCode).orElseThrow(
                () -> new PostException(ErrorCode.NOT_FOUND_CODE)
        );
        // 쓰기 권한 - true / 읽기 권한 - false
        return url.equals(room.getWriteUrl());
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
}
