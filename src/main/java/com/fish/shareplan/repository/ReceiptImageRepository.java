package com.fish.shareplan.repository;

import com.fish.shareplan.domain.expense.entity.ReceiptImage;
import com.fish.shareplan.domain.schedule.entity.ScheduleItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReceiptImageRepository extends JpaRepository<ReceiptImage,String>{
}
