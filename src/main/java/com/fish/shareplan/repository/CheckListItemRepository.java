package com.fish.shareplan.repository;

import com.fish.shareplan.domain.checklist.entity.CheckListItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckListItemRepository extends JpaRepository<CheckListItem,String> {
}
