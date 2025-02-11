package com.fish.shareplan.service;

import com.fish.shareplan.domain.room.dto.RoomResponseDto;
import com.fish.shareplan.domain.room.entity.Room;
import com.fish.shareplan.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomResponseDto createRoom(){
        Room newRoom = roomRepository.save(new Room());
        return new RoomResponseDto(newRoom.getRoomCode(), newRoom.getReadUrl(), newRoom.getWriteUrl());
    }
}
