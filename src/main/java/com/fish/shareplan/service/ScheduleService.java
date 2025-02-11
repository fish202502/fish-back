package com.fish.shareplan.service;

import com.fish.shareplan.domain.room.entity.Room;
import com.fish.shareplan.domain.schedule.dto.request.ScheduleRequestDto;
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
        Room room = roomRepository.findByRoomCode(roomCode).orElseThrow();

        // 쓰기 권한이 없을 경우
        if (!room.getWriteUrl().equals(url)) {
            throw new PostException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        // 시작시간이 종료시간보다 후일 경우
        if (scheduleRequestDto.getStartTime().isAfter(scheduleRequestDto.getEndTime())) {
            throw new PostException(ErrorCode.INVALID_START_END_TIME);
        }

        Schedule schedule = Schedule.builder()
                .room(room)
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
}
