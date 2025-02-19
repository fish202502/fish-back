package com.fish.shareplan.repository;

import com.fish.shareplan.domain.checklist.entity.CheckList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheckListRepository extends JpaRepository<CheckList, String>

//        , CheckListRepositoryCustom
{


    Optional<CheckList> findByRoomId(String roomId);
}
