package com.fish.shareplan.repository;

import com.fish.shareplan.domain.photo.entity.Photo;
import com.fish.shareplan.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotoRepository extends JpaRepository<Photo,String> {

    Optional<Photo> findByRoomId(String roomId);
}
