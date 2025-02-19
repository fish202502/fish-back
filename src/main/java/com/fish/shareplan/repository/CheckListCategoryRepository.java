package com.fish.shareplan.repository;

import com.fish.shareplan.domain.checklist.entity.CheckListCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckListCategoryRepository extends JpaRepository<CheckListCategory,String> {
}
