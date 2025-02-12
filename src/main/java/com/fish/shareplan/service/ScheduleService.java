package com.fish.shareplan.service;

import com.fish.shareplan.domain.room.entity.Room;
import com.fish.shareplan.domain.schedule.dto.request.ScheduleRequestDto;
import com.fish.shareplan.domain.schedule.dto.request.ScheduleUpdateRequestDto;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ScheduleService {

    private final RoomRepository roomRepository;

    private final ScheduleRepository scheduleRepository;
    private final ScheduleItemRepository scheduleItemRepository;

    // 일정 추가
    public String addSchedule(String roomCode, String url, ScheduleRequestDto scheduleRequestDto) {

        // 시작시간이 종료시간보다 후일 경우
        if (scheduleRequestDto.getStartTime().isAfter(scheduleRequestDto.getEndTime())) {
            throw new PostException(ErrorCode.INVALID_START_END_TIME);
        }

        Schedule schedule = Schedule.builder()
                .room(isValid(roomCode,url))
                .build();
        scheduleRepository.save(schedule);
        scheduleItemRepository.save(
                ScheduleItem.builder()
                        .title(scheduleRequestDto.getTitle())
                        .content(scheduleRequestDto.getContent())
                        .startTime(scheduleRequestDto.getStartTime())
                        .endTime(scheduleRequestDto.getEndTime())
                        .schedule(schedule)
                        .build()
        );

        return schedule.getId();
    }

    // 일정 조회
    public List<ScheduleResponseDto> getSchedule(String roomCode, String url) {

        Room room = roomRepository.findByRoomCode(roomCode).orElseThrow(
                () -> new PostException(ErrorCode.NOT_FOUND_CODE)
        );
        String roomId = room.getId();

        return scheduleRepository.findAllSchedule(roomId);
    }

    // 일정 수정
    public String updateSchedule(String roomCode, String url, ScheduleUpdateRequestDto scheduleUpdateRequestDto) {

        // 권한 체크
        isValid(roomCode, url);

        // 시작시간이 종료시간보다 후일 경우
        if (scheduleUpdateRequestDto.getStartTime().isAfter(scheduleUpdateRequestDto.getEndTime())) {
            throw new PostException(ErrorCode.INVALID_START_END_TIME);
        }

        ScheduleItem scheduleItem = scheduleItemRepository.findByScheduleId(scheduleUpdateRequestDto.getId())
                .orElseThrow(
                        () -> new PostException(ErrorCode.NOT_FOUND_SCHEDULE)
                );

        scheduleItem.setTitle(scheduleUpdateRequestDto.getTitle());
        scheduleItem.setContent(scheduleUpdateRequestDto.getContent());
        scheduleItem.setStartTime(scheduleUpdateRequestDto.getStartTime());
        scheduleItem.setEndTime(scheduleUpdateRequestDto.getEndTime());

        scheduleItemRepository.save(scheduleItem);

        return scheduleItem.getId();
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
