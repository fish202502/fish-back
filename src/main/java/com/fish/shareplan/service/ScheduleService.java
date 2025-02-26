package com.fish.shareplan.service;

import com.fish.shareplan.domain.room.entity.Room;
import com.fish.shareplan.domain.schedule.dto.request.ScheduleItemRequestDto;
import com.fish.shareplan.domain.schedule.dto.request.ScheduleRequestDto;
import com.fish.shareplan.domain.schedule.dto.request.ScheduleUpdateRequestDto;
import com.fish.shareplan.domain.schedule.dto.response.ScheduleItemResponseDto;
import com.fish.shareplan.domain.schedule.dto.response.ScheduleResponseDto;
import com.fish.shareplan.domain.schedule.entity.Schedule;
import com.fish.shareplan.domain.schedule.entity.ScheduleItem;
import com.fish.shareplan.exception.ErrorCode;
import com.fish.shareplan.exception.PostException;
import com.fish.shareplan.repository.RoomRepository;
import com.fish.shareplan.repository.ScheduleItemRepository;
import com.fish.shareplan.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ScheduleService {

    private final RoomRepository roomRepository;

    private final ScheduleRepository scheduleRepository;
    private final ScheduleItemRepository scheduleItemRepository;

    // 일정 추가
    public Map<String, String> addOrUpdateItinerary(String roomCode, String url, ScheduleRequestDto dto) {

        // 시작시간이 종료시간보다 후일 경우
        if (dto.getStartTime().isAfter(dto.getEndTime())) {
            throw new PostException(ErrorCode.INVALID_START_END_TIME);
        }

        Room room = isValid(roomCode, url);

        Schedule foundSchedule = scheduleRepository.findByRoomId(room.getId()).orElse(null);

        Schedule schedule = Schedule.builder()
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .room(room)
                .build();

        // 이미 존재하면 업데이트
        if (foundSchedule != null) {
            foundSchedule.update(schedule);
            return Map.of("U", foundSchedule.getId());
            // 아니면 추가
        } else {
            scheduleRepository.save(schedule);
            return Map.of("C", schedule.getId());
        }
    }


    // 일정 추가
    public String addSchedule(String roomCode, String url, ScheduleItemRequestDto dto) {

        // 시작시간이 종료시간보다 후일 경우
        if (dto.getStartTime().isAfter(dto.getEndTime())) {
            throw new PostException(ErrorCode.INVALID_START_END_TIME);
        }

        Room room = isValid(roomCode, url);

        Schedule foundSchedule = scheduleRepository.findByRoomId(room.getId()).orElseThrow(
                () -> new PostException(ErrorCode.NOT_FOUND_SCHEDULE)
        );

        if (foundSchedule.getId().equals(dto.getScheduleId())) {
            ScheduleItem scheduleItem = ScheduleItem.builder()
                    .schedule(foundSchedule)
                    .title(dto.getTitle())
                    .content(dto.getContent())
                    .startTime(dto.getStartTime())
                    .endTime(dto.getEndTime())
                    .build();
            scheduleItemRepository.save(scheduleItem);
            return scheduleItem.getId();
        }
        throw new PostException(ErrorCode.FAIL_TO_RESISTER);
    }


    // 일정 조회
    public ScheduleResponseDto getSchedule(String roomCode, String url) {

        Room room = roomRepository.findByRoomCode(roomCode).orElseThrow(
                () -> new PostException(ErrorCode.NOT_FOUND_CODE)
        );
        Schedule schedule = scheduleRepository.findByRoomId(room.getId()).orElse(
               Schedule.builder()
                       .room(room)
                       .build()
        );
        List<ScheduleItem> scheduleItemList = scheduleItemRepository.findByScheduleId(schedule.getId());
        return Schedule.toDto(schedule, scheduleItemList);
    }

    // 일정 수정
    public ScheduleItemResponseDto updateSchedule(String roomCode, String url
            , ScheduleUpdateRequestDto scheduleUpdateRequestDto) {

        // 권한 체크
        isValid(roomCode, url);

        // 시작시간이 종료시간보다 후일 경우
        if (scheduleUpdateRequestDto.getStartTime().isAfter(scheduleUpdateRequestDto.getEndTime())) {
            throw new PostException(ErrorCode.INVALID_START_END_TIME);
        }

        ScheduleItem scheduleItem = scheduleItemRepository.findById(scheduleUpdateRequestDto.getScheduleId())
                .orElseThrow(
                        () -> new PostException(ErrorCode.NOT_FOUND_SCHEDULE)
                );

        scheduleItem.modify(scheduleUpdateRequestDto);
        scheduleItemRepository.save(scheduleItem);

        return ScheduleItem.toDto(scheduleItem);
    }

    // 일정 삭제
    public boolean deleteSchedule(String roomCode, String url, String scheduleItemId) {
        Room room = roomRepository.findByRoomCode(roomCode).orElseThrow(
                () -> new PostException(ErrorCode.NOT_FOUND_CODE)
        );

        // 쓰기 권한이 없을 경우
        if (!room.getWriteUrl().equals(url)) {
            throw new PostException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        // 일정이 존재하는 지 먼저 확인
        ScheduleItem scheduleItem = scheduleItemRepository.findById(scheduleItemId).orElseThrow(
                () -> new PostException(ErrorCode.NOT_FOUND_SCHEDULE)
        );

        scheduleItemRepository.deleteById(scheduleItemId);

        return true;
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
}
